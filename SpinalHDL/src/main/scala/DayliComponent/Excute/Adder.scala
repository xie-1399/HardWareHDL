package DayliComponent.Excute
import spinal.core._
import spinal.lib._


class CarryAdder(size:Int) extends Component{
  val io = new Bundle{
    val a = in UInt(size bits)
    val b = in UInt(size bits)
    val result = in UInt(size bits)
  }
  val carry = False  //进位
  for(i <- 0 until size){
    val a = io.a(i)
    val b = io.b(i)
    io.result(i) := a ^ b ^ carry
    carry := (a & b) | (a & carry) | (b & carry)
  }
}




class Adder {

}
