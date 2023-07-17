package DayliComponent.wavesRtl
import spinal.core._

// Look at the https://hdlbits.01xz.net/wiki/Sim/circuit7

class circuit7 extends Component {
  noIoPrefix()
  val io = new Bundle {
    val a = in(Bool())
    val q = out(Bool())
  }

//  val reg = RegInit(False)
//  reg := !io.a
//  io.q := reg
  val reg = RegNext(!io.a) //no value for the reset
  io.q := reg
}


object circuit7 extends App{
  SpinalVerilog(new circuit7)
}