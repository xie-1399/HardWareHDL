package SpinalHDL.ip

import spinal.core._

//how to convert the SInt with the bits(complement)

class HW_SInt extends Component {
  val io = new Bundle{
    val input = in SInt(10 bits)
    val output = out Bits(10 bits)
  }
  io.output := io.input.asBits
}
