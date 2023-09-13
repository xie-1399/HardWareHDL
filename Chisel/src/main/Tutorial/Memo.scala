package Tutorial

import chisel3._

  /*
  Implement a dual port memory of 256 8-bit words.
  details at :
  */

class Memo extends Module {
  val io = IO( new Bundle{
    val wen = Input(Bool())
    val wrAddr = Input(UInt(8.W))
    val wrData = Input(UInt(8.W))
    val ren = Input(Bool())
    val rdAddr = Input(UInt(8.W))
    val rdData = Output(UInt(8.W))
  })

  val mem = Mem(256,UInt(8.W))
  when(io.wen){
    mem.write(io.wrAddr,io.wrData)
  }
  io.rdData := 0.U
  when(io.ren){
    io.rdData := mem.read(io.rdAddr)
  }

}
