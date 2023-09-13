package ip

import chisel3._
import chisel3.util._

  /*
  for the sequential circuit only update the reg at rising edge
  */


class UsingSequential extends Module {

  val io = IO(new Bundle{
    val enable = Input(Bool())
    val value = Input(UInt(3.W))
    val usereg = Output(UInt(3.W))
  })
  /*
  reg with init value and enable signal
  */
  val enableReg = RegEnable(io.value,0.U(3.W),io.enable)
  io.usereg := enableReg
}

class DefineCounter extends Module{

  def genCounter(n:Int) = {
    val cntReg = RegInit(0.U(8.W))
    cntReg := Mux(cntReg === n.U,0.U,cntReg + 1.U)
    cntReg
  }

  val myCounter = genCounter(100)

}