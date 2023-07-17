package DayliComponent.wavesRtl
import spinal.core._
import spinal.lib._

import scala.collection.mutable._
//maybe should learn about the Boolean Table
// Look at : https://hdlbits.01xz.net/wiki/Sim/circuit2

class circuit2 extends Component {
  //如何实现一个函数统计输入中的True数量
  noIoPrefix()
  val io = new Bundle {
    val a = in(Bool())
    val b = in(Bool())
    val c = in(Bool())
    val d = in(Bool())
    val q = out(Bool())
  }
  //Todo just learn how to use a new function
  val value = CountOne(io.a ## io.b ## io.c ## io.d)
  io.q :=  value % 2 === 0  // get the function value
}


object circuit2 extends App{
  SpinalVerilog(new circuit2)
}