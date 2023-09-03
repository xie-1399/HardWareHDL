package ip
import chisel3._

class SimpleHello extends Module {

  /* simple hello demo with the led ~ */

  val io = IO(new Bundle{
    val led = Output(UInt(1.W))
  })
  val MAX = (5000000/2 -1).U
  val cntReg = RegInit(0.U(32.W))
  val blkReg = RegInit(0.U(1.W))

  cntReg := cntReg + 1.U
  when(cntReg === MAX){
    cntReg := 0.U
    blkReg := ~blkReg
  }
  io.led := blkReg
}

 /*
 a simple way to generate the Rtl code
 */
object SimpleHello extends App{
   emitVerilog(new SimpleHello)
 }

/*
some opt like generate verilog in file path like this
 */
object genWay extends App{
  emitVerilog(new SimpleHello(), Array("--target-dir", "rtl"))
}
 /*
 show the string format
 */

object genString extends App{
  println(getVerilogString(new SimpleHello))
}