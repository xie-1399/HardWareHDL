package SpinalHDL.lib.Flow

import spinal.core._
import spinal.lib._

//the lib flow usage

class UseFlow extends Component {
  val io = new Bundle{
    val flowA = slave Flow(Bits(32 bits))
  }


}


object UseFlow extends App{
  SpinalVerilog(new UseFlow)
}