package SpinalHDL.ip

import spinal.core._
import spinal.core.fiber._
//case class shiftRegister [T <: Data](datatype:T) extends Component{
//  val io = new Bundle{
//    val input = in (cloneOf(datatype))
//    val output = out (cloneOf(datatype))
//  }
//  io.output := io.input
//}

case class ShiftRegister[T <: Data](dataType: HardType[T],idwidth: BitCount) extends Component {
  val io = new Bundle {
    val input  = in (dataType())
    val output = out(dataType())
    val bitcounter = in Bits(idwidth)
  }
  val temp = dataType()
  io.output := temp
}

class HardTypetest extends Component{
  val temp = ShiftRegister(Bits(32 bits),32 bits)
}

object shiftRegister extends App{
//  val x,y = Handle[Int]
//  val xPlus2 = x.produce(x.get + 2)
//  x.load(2)
//  y.load(2)
//  println(xPlus2)
  SpinalVerilog(new HardTypetest)
}
