package ip

import chisel3._
import chisel3.util._

  /*
  the connect should be notice: <> will error if missing signal
 */

class ModuleOne extends Module{
  val io = IO(new Bundle{
    val valid = Input(Bool())
    val inst = Output(UInt(32.W))
    val halt = Input(Bool())
  })
  io.inst := 0x80000000l.U
}

class ModuleTwo extends Module{
  val io = IO(new Bundle{
    val inst = Input(UInt(32.W))
    val halt = Output(Bool())
    val valid = Output(Bool())
  })
  io.halt := true.B
  io.valid := true.B
}



class UsingModule extends Module {

  /*
  notice that : must wrapper it with the Module
  */

  val one = Module(new ModuleOne)
  val two = Module(new ModuleTwo)
  one.io <> two.io
}

object UsingModule extends App{
  ChiselSim.Gen(new UsingModule)
}