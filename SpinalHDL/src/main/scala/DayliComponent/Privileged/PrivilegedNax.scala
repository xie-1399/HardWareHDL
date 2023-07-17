package DayliComponent.Privileged

package DayliComponent.StateMachine

// CSR plugin stands out the priviliged of Riscv
// Copy from Naxriscv PrivilegedPlugin

import spinal.core._
import spinal.lib._

import scala.collection.mutable
import scala.collection.mutable._


trait PrivilegedService{
  def hasMachinePriv : Bool
  def hasSupervisorPriv : Bool
  def getPrivilege() : UInt
  //U S M Mode
  def isUser():Bool = getPrivilege() === 0
  def isSupervisor():Bool = getPrivilege() === 1
  def isMachine():Bool = getPrivilege() === 3

  def implementUser : Boolean
  def implementSupervisor : Boolean
  def implementUserTrap:Boolean

  def xretAwayFromMachine:Bool
  def addMisa(id:Int):Unit
  def addMisa(id:Char):Unit = addMisa(id - 'A')
  def setFpDirty() : Unit
  def isFpuEnabled() : Bool
}

case class JumpCmd(pcWidth:Int) extends Bundle{
  val pc = UInt(pcWidth bits)
}

//Create a jump interface by priority
trait JumpService{
  def createJumpInterface(priority:Int,aggregationPriority:Int = 0) : Flow[JumpCmd]
}

object globalconfig{
  val XLEN = 64
  val RVF = true
}


case class PrivilegedConfig(withSupervisor:Boolean,
                            withUser: Boolean,
                            withUserTrap: Boolean,
                            withRdTime: Boolean,
                            withDebug: Boolean,
                            debugTriggers: Int,
                            vendorId: Int, //制造商相关的ID
                            archId: Int,  //系统架构的标识符
                            impId: Int,   //处理器实现版本的唯一编码
                            hartId: Int) //核心的编号，至少有一个是0并且都是唯一的编号


class myCSR(var p : PrivilegedConfig) extends Component with PrivilegedService {
  override def hasMachinePriv: Bool = setup.withMachinePrivilege
  override def hasSupervisorPriv: Bool = setup.withSupervisorPrivilege
  override def getPrivilege(): UInt = setup.privilege // current pri

  override def implementUser: Boolean = p.withUser
  override def implementSupervisor: Boolean = p.withSupervisor
  override def implementUserTrap: Boolean = p.withUserTrap

  override def xretAwayFromMachine: Bool = setup.xretAwayFromMachine
  override def setFpDirty(): Unit = {
    setup.setFpDirty := True
  }
  override def isFpuEnabled(): Bool = setup.isFpuEnabled

  //Add Isa Type
  val misaIds = mutable.LinkedHashSet[Int]()
  override def addMisa(id: Int): Unit = {
    misaIds += id
  }
  override def addMisa(id: Char): Unit = {
    super.addMisa(id)
  }

  val io = new Area {
    val int = new Area {  //interrupt
      val machine = new Area{
        val timer = in Bool()
        val software = in Bool()
        val external = in Bool()
      }
      val supervisor = p.withSupervisor generate new Area {
        val external = in Bool()
      }
      val user = p.withUserTrap generate new Area {
        val external = in Bool()
      }
    }
    val rdtime = in UInt(64 bits) //是一个特殊的寄存器，用于记录CPU自启动以来的时钟周期数
  }

  val setup = new Area {
    val debugMode = p.withDebug generate(Bool())
    val privilege = RegInit(U"11")
    val withMachinePrivilege = privilege >= U"11"
    val withSupervisorPrivilege = privilege >= U"01"
    val xretAwayFromMachine = False
    val trapEvent = False
    val redoTriggered = False
    val setFpDirty = False
    val isFpuEnabled = False

    //Todo Add More ISA like C / F / D
    addMisa('I')
    if(p.withUser) addMisa('U')
    if(p.withSupervisor) addMisa('S')
  }

  val logic = new Area {
    //val csr = setup.csr
    case class Delegator(var enable:Bool,privilege:Int) //根据不同的异常和中断信号委托不同的模块进行处理？
    case class InterruptSpec(var cond:Bool,id:Int,privilege:Int,delegators: List[Delegator])
    case class ExceptionSpec(id:Int,delegator: List[Delegator])
    val interruptSpecs = ArrayBuffer[InterruptSpec]()
    val exceptionSpecs = ArrayBuffer[ExceptionSpec]()

    // add the interrupt
    def addInterrupt(cond : Bool, id : Int, privilege : Int, delegators : List[Delegator]):Unit = {
      interruptSpecs += InterruptSpec(cond,id,privilege, delegators)
    }
  }

  val withFs = globalconfig.RVF || p.withSupervisor

  val machine = new Area {
    val cause = new Area {  //mcause寄存器（异常 or 中断）
      val interrupt = RegInit(False)
      val commitrescheduleCauseWidth = 5
      val code = Reg(UInt(commitrescheduleCauseWidth bits)) // Todo , maybe better from the commit service
    }
    //mstatus
    val mstatus = new Area{
        val mie,mpie = RegInit(False) //前任中断位
        val mpp = RegInit(U"00") //表示运行的特权模式
        val fs = withFs generate RegInit(U"00") //表示浮点寄存器的精度，是F or D
        val sd = False //在S模式下的访存
        if(globalconfig.RVF) setup.isFpuEnabled setWhen(fs =/= 0 )
        if(withFs) sd setWhen(fs === 3)
    }

    val misaExt = misaIds.map(1l << _).reduce(_ | _) //按照位或的方式进行合并
    val misaMxl = globalconfig.XLEN match {
      case 32 => BigInt(1) << globalconfig.XLEN - 2
      case 64 => BigInt(2) << globalconfig.XLEN - 2
    }
    val misa = misaMxl | misaExt // 中间的位置保留为0
    val mie = new Area {
      val meie,mtie,msie = RegInit(False)
    }
    val mip = new Area {
      val meip = RegNext(io.int.machine.external) init(False)
      val msip = RegNext(io.int.machine.software) init(False)
      val mtip = RegNext(io.int.machine.timer) init(False)
    }

    val mideleg = new Area{
      val st,se,ss = RegInit(False)  //委托中断交由S模式进行处理
    }

    val medeleg = p.withSupervisor generate new Area {
      //对应特权级文档的异常类型（P36）
      val iam, bp, eu, es, ipf, lpf, spf = RegInit(False)
      val mapping = mutable.LinkedHashMap(0 -> iam, 3 -> bp, 8 -> eu, 9 -> es, 12 -> ipf, 13 -> lpf, 15 -> spf)
    }
    //为机器模式下的CSR寄存器分配对应的权限（读 or 读写 or 写）

  }

}

