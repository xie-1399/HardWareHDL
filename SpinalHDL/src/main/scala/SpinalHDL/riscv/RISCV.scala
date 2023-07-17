package SpinalHDL.riscv

/*定义相关的指令集，指令集匹配的规范,M可以简单理解为一种模式匹配的方式
object RISCV extends App{
  val mybits = Bits(8 bits)
  val itMatch = mybits === M"00-100--"
}
How about the SpinalEnum
object Enumeration extends SpinalEnum {
  val element0, element1, ..., elementN = newElement()
}
*/

import spinal.core._

object Opcodes {
  val ADD = M"0000000----------000-----0110011"
  val SUB = M"0100000----------000-----0110011"
  val SLT = M"0000000----------010-----0110011"
  val SLTU = M"0000000----------011-----0110011"
  val XOR = M"0000000----------100-----0110011"
  val OR = M"0000000----------110-----0110011"
  val AND = M"0000000----------111-----0110011"
  val ADDI = M"-----------------000-----0010011"
  val SLTI = M"-----------------010-----0010011"
  val SLTIU = M"-----------------011-----0010011"
  val XORI = M"-----------------100-----0010011"
  val ORI = M"-----------------110-----0010011"
  val ANDI = M"-----------------111-----0010011"
  val LUI = M"-------------------------0110111"
  val AUIPC = M"-------------------------0010111"

  val SLLI = M"0000000----------001-----0010011"
  val SRLI = M"0000000----------101-----0010011"
  val SRAI = M"0100000----------101-----0010011"
  val SLL = M"0000000----------001-----0110011"
  val SRL = M"0000000----------101-----0110011"
  val SRA = M"0100000----------101-----0110011"

  val JAL = M"-------------------------1101111"
  val JALR = M"-----------------000-----1100111"
  val BEQ = M"-----------------000-----1100011"
  val BNE = M"-----------------001-----1100011"
  val BLT = M"-----------------100-----1100011"
  val BGE = M"-----------------101-----1100011"
  val BLTU = M"-----------------110-----1100011"
  val BGEU = M"-----------------111-----1100011"

  val LB = M"-----------------000-----0000011"
  val LH = M"-----------------001-----0000011"
  val LW = M"-----------------010-----0000011"
  val LBU = M"-----------------100-----0000011"
  val LHU = M"-----------------101-----0000011"
  val SB = M"-----------------000-----0100011"
  val SH = M"-----------------001-----0100011"
  val SW = M"-----------------010-----0100011"

  val CSRRW = M"-----------------001-----1110011"
  val CSRRS = M"-----------------010-----1110011"
  val CSRRC = M"-----------------011-----1110011"
  val CSRRWI = M"-----------------101-----1110011"
  val CSRRSI = M"-----------------110-----1110011"
  val CSRRCI = M"-----------------111-----1110011"

  val ECALL = M"00000000000000000000000001110011"
  val EBREAK = M"00000000000100000000000001110011"
  val MRET = M"00110000001000000000000001110011"

  val MUL = M"0000001----------000-----0110011"
  val MULH = M"0000001----------001-----0110011"
  val MULHSU = M"0000001----------010-----0110011"
  val MULHU = M"0000001----------011-----0110011"
  val DIV = M"0000001----------100-----0110011"
  val DIVU = M"0000001----------101-----0110011"
  val REM = M"0000001----------110-----0110011"
  val REMU = M"0000001----------111-----0110011"
}

case class Extension(char: Char){
  assert(char >= 'A' && char <= 'Z')
}
object Extension{
  //最后是返回指令集模块的名称
  def apply(baseIsa:BaseIsa):Extension = new Extension(baseIsa match {
    case BaseIsa.RV32E => 'E'
    case _ => 'I'
  })
}
//密封特质，这样智能在本源文件中进行继承，在源文件中定义对应的子类型
sealed trait InstructionFormat
object InstructionFormat{
  /*
  根据指令的类型判断对应的源操作数，这种方式实现起来比较清晰
  */
  case object R extends InstructionFormat //基础加减移位运算
  case object I extends InstructionFormat //立即数
  case object S extends InstructionFormat //存储
  case object B extends InstructionFormat //分支
  case object U extends InstructionFormat //lui 、 auipc
  case object J extends InstructionFormat //跳转
}

object RegisterType extends SpinalEnum{
  //The Enumeration type corresponds to a list of named values.
  val NONE,GPR = newElement()
}

abstract class InstructionType(
    val format : InstructionFormat,
    val rs1type :  SpinalEnumElement[RegisterType.type ],
    val rs2type :  SpinalEnumElement[RegisterType.type ],
    val rdtype :  SpinalEnumElement[RegisterType.type ]
)

object InstructionType{
  case object R
      extends InstructionType(
        InstructionFormat.R,
        RegisterType.GPR,
        RegisterType.GPR,
        RegisterType.GPR
      )
  case object I
      extends InstructionType(
        InstructionFormat.I,
        RegisterType.GPR,
        RegisterType.NONE,
        RegisterType.GPR
      )
  case object S
      extends InstructionType(
        InstructionFormat.S,
        RegisterType.GPR,
        RegisterType.GPR,
        RegisterType.NONE
      )
  case object B
      extends InstructionType(
        InstructionFormat.B,
        RegisterType.GPR,
        RegisterType.GPR,
        RegisterType.NONE
      )
  case object U
      extends InstructionType(
        InstructionFormat.U,
        RegisterType.NONE,
        RegisterType.NONE,
        RegisterType.GPR
      )
  case object J
      extends InstructionType(
        InstructionFormat.J,
        RegisterType.NONE,
        RegisterType.NONE,
        RegisterType.GPR
      )
}


abstract class TrapCause(val isInterrupt:Boolean,val code:Int,val mtval:UInt = null) //指定中断入口地址

abstract class InterruptCause(code:Int) extends TrapCause(true,code = code)

abstract class ExceptionCause(code:Int,mtval:UInt = null) extends TrapCause(false,code, mtval)

object TrapCause{
  //指定哪些中断类型
  case object UserSoftwareInterrupt extends InterruptCause(0)
  case object SupervisorSoftwareInterrupt extends InterruptCause(1)
  case object MachineSoftwareInterrupt extends InterruptCause(3)
  case object UserTimerInterrupt extends InterruptCause(4)
  case object SupervisorTimerInterrupt extends InterruptCause(5)
  case object MachineTimerInterrupt extends InterruptCause(7)
  case object UserExternalInterrupt extends InterruptCause(8)
  case object SupervisorExternalInterrupt extends InterruptCause(9)
  case object MachineExternalInterrupt extends InterruptCause(11)

  //指定异常类型
  case class InstructionAddressMisaligned(address: UInt) extends ExceptionCause(0, address)
  case class InstructionAccessFault(address: UInt) extends ExceptionCause(1, address)
  case class IllegalInstruction(ir: UInt) extends ExceptionCause(2, ir)
  case object Breakpoint extends ExceptionCause(3)
  case class LoadAddressMisaligned(address: UInt) extends ExceptionCause(4, address)
  case class LoadAccessFault(address: UInt) extends ExceptionCause(5, address)
  case class StoreAddressMisaligned(address: UInt) extends ExceptionCause(6, address)
  case class StoreAccessFault(address: UInt) extends ExceptionCause(7, address)
  case object EnvironmentCallFromUMode extends ExceptionCause(8)
  case object EnvironmentCallFromSMode extends ExceptionCause(9)
  case object EnvironmentCallFromMMode extends ExceptionCause(11)
  case class InstructionPageFault(address: UInt) extends ExceptionCause(12, address)
  case class LoadPageFault(address: UInt) extends ExceptionCause(13, address)
  case class StorePageFault(address: UInt) extends ExceptionCause(15, address)

}
