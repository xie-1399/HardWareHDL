package ip

import chisel3._

  /*
  the structure signal contains Bundle and Vec for diff purpose
  */

case class Channel() extends Bundle{
  val data = UInt(32.W)
  val valid = Bool()
}

class UsingStructure extends Module {
  val io = IO(new Bundle{
    val output = Output (Channel())
  })

  val v = Wire(Vec(3,UInt(4.W)))
  v(0) := 1.U
  v(1) := 2.U
  v(2) := 3.U

  val defVec = VecInit(1.U,2.U,3.U) //with initial value

  val resetRegFile = RegInit(VecInit(Seq.fill(32)(0.U(32.W)))) //initial all vector

  io.output.data := 0.U
  val lowByte = io.output.data(7,0)
  io.output.valid := false.B
  when(lowByte === 0.U){
    io.output.valid := true.B
  }
}

object UsingStructure extends App{
  ChiselSim.Gen(new UsingStructure())
}