package ip

import chisel3._

  /*
  always use the memory in a sync way like this:
 */


  /*
   this is a simple forwarding mem w/r way
  */
class UsingMemory extends Module{

  val io = IO(new Bundle{
    val raddr = Input(UInt(10.W))
    val waddr = Input(UInt(10.W))
    val wr = Input(Bool())
    val wdata = Input(UInt(8.W))
    val rdata = Output(UInt(8.W))
  })

  val mem = SyncReadMem(1024,UInt(8.W))
  val writeNext = RegNext(io.waddr === io.raddr && io.wr)
  val memData = mem.read(io.raddr)
  when(io.wr){
    mem.write(io.waddr,io.wdata)
  }

  io.rdata := Mux(writeNext,RegNext(io.wdata),memData)
}
