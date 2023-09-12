package Tutorial

import chisel3._

  /*
  a counter with the enable and increase the amt value each time
 */

object Counter{
  def counter(max:UInt,en:Bool,amt:UInt):UInt = {
    val x = RegInit(0.U(max.getWidth.W))
    when(en){
      x := Mux(x + amt > max,0.U,x + amt)
    }
    x
  }
}

class Counter extends Module{
  val io = IO(new Bundle{
    val inc = Input(Bool())
    val amt = Input(UInt(4.W))
    val tot = Output(UInt(8.W))
  })
  io.tot := Counter.counter(255.U,io.inc,io.amt)
}
