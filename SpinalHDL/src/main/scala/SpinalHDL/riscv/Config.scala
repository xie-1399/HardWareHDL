package SpinalHDL.riscv

/*指令集和总线位宽的配置文件
有必要说明一下RV32E,RV32E和I的指令编码差别不大，区别主要在于只用了x0-x15这16个寄存器
object Config extends App{
  val config = new Config(RV64I)
  println("xlen:"+ config.xlen)
  println("numRegs:" + config.numRegs)
}
*/
import SpinalHDL.riscv.BaseIsa.RV64I
import spinal.core._
//封装保证只有文件内的子类能有继承关系
sealed trait BaseIsa{
  val xlen:Int
  val numRegs:Int
}

object BaseIsa{
  case object RV32I extends BaseIsa{
    override val xlen: Int = 32
    override val numRegs: Int = 32
  }

  case object RV32E extends BaseIsa{
    override val xlen: Int = 32
    override val numRegs: Int = 16
  }
  case object RV64I extends BaseIsa{
    override val xlen: Int = 64
    override val numRegs: Int = 32
  }
}

class Config(val baseIsa: BaseIsa,val debug:Boolean = true) {
  def xlen = baseIsa.xlen
  def numRegs = baseIsa.numRegs
  //Todo May be you should set the bus config here
}

