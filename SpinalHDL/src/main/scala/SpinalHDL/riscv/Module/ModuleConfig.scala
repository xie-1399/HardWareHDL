package SpinalHDL.riscv.Module

import spinal.core._

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

class ModuleConfig(val baseIsa: BaseIsa,val debug:Boolean = true) {
  def xlen = baseIsa.xlen
  def numRegs = baseIsa.numRegs
  //Todo May be you should set the bus config here
}


object MyCPUConfig{
  val myconfig = new ModuleConfig(BaseIsa.RV64I)
}