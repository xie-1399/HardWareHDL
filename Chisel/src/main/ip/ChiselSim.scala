package ip

import chisel3._

object ChiselSim {

  def Gen[T <: Module](top: => RawModule) = {
    emitVerilog(top,Array("--target-dir", "rtl"))
  }

}
