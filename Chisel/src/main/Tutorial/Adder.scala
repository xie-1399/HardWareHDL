package Tutorial

import chisel3._
  /*
  the chisel-tutorial about the adder
  details at : https://github.com/ucb-bar/chisel-tutorial/blob/release/src/main/scala/problems/Adder.scala
  */

class Adder(val w:Int) extends Module {
  val io = IO(new Bundle {
    val in0 = Input(UInt(w.W))
    val in1 = Input(UInt(w.W))
    val out = Output(UInt(w.W))
  })

  // no need for the carry
  io.out := io.in0 + io.in1
}
