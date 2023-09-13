package ip

import chisel3._
import chisel3.util._
import firrtl.Utils.False
  /*
  this show some simple examples about how to use scala to gen the hardware
  */

class UsingGenerator extends Module{
  val io = IO(new Bundle{
    val sel1 = Input(UInt(3.W))
    val sel2 = Input(UInt(3.W))
  })

  def compare(a:UInt,b:UInt) = {
    val equal = a===b
    val gt = a > b
    (equal,gt)
  }

  /* define own mux function*/
  def muMux[T <: Data](sel:Bool,tPath:T,fPath:T) = {
    val ret = Wire(fPath.cloneType) //use the Wire Default also can use
    ret := fPath
    when(sel){
      ret := tPath
    }
    ret
  }

  val cmp = compare(io.sel1,io.sel2)
  val eqlres = cmp._1
  val gtres = cmp._2

  val msg = "Hello World"
  val text = VecInit(msg.map(_.U))

  /* even can print some simple value*/
  print(text.length)

  val min = text.reduceTree((x,y) => Mux(x < y,x,y))
}

  /*
   also can use the Inheritance
  */

abstract class Ticker(n:Int) extends Module{
  val io = IO(new Bundle{
    val tick = Output(Bool())
  })
}

class UpTicker(n:Int) extends Ticker(n){
  val cntReg = RegInit(0.U(4.W))
  cntReg := cntReg + 1.U

  val tick = cntReg === 4.U
  io.tick := tick
}
