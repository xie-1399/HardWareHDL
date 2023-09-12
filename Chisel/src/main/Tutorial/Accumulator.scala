package Tutorial

import chisel3._
import ip.ChiselSim
/*
counter incoming trues -> the branch when exercise
details at : https://github.com/ucb-bar/chisel-tutorial/blob/release/src/main/scala/problems/Accumulator.scala
*/

class Accumulator extends Module {
  val io = IO(new Bundle{
    val in = Input(UInt(1.W))
    val output = Output(UInt(8.W))
  })

  val value = RegInit(0.U(8.W))
  value := Mux(io.in === 1.U,value + 1.U,value)
  io.output := value

  //  val accumulator = RegInit(0.U(8.W))
  //  accumulator := accumulator + io.in
  //  io.output := accumulator
  //  value := Mux(io.in === 1.U,value + 1.U,value)
}

object Accumulator extends App{
  ChiselSim.Gen(new Accumulator)
}