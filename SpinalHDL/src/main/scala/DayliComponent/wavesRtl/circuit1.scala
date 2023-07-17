package DayliComponent.wavesRtl

import spinal.core._
import spinal.lib._

// From Wave to the rtl
// https://hdlbits.01xz.net/wiki/Sim/circuit1

class circuit1 extends Component {
  noIoPrefix()
  val io = new Bundle{
    val a = in(Bool())
    val b = in(Bool())
    val q = out(Bool())
  }
  io.q := io.a && io.b
}

object circuit1 extends App{
  SpinalVerilog(new circuit1)
}