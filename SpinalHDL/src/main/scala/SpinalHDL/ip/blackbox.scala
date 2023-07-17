package SpinalHDL.ip

import spinal.core._
import spinal.lib._
import spinal.core.BlackBox
//如何使用blackbox，将实现进行封装

class Ram_1w_1r(wordwidth:Int,wordCount:Int) extends BlackBox{
  addGeneric("wordwidth",wordwidth)
  addGeneric("wordCount",wordCount)

  //other way
//  val general = new Generic{
//    val wordwidth = Ram_1w_1r.this.wordwidth
//    val wordCount = Ram_1w_1r.this.wordCount
//  }

  val io = new Bundle{
    val clk = in Bool()
    val wr = new Bundle{
      val en = in Bool()
      val data = in Bits(wordwidth bits)
      val addr = in UInt(log2Up(wordCount) bits)
    }
    val rd = new Bundle{
      val en = in Bool()
      val data = out Bits (wordwidth bits)
      val addr = in UInt (log2Up(wordCount) bits)
    }
  }
  mapCurrentClockDomain(clock = io.clk)
}

class useBlackbox extends Component{
  val io = new Bundle {
    val wr = new Bundle {
      val en = in Bool()
      val data = in Bits (8 bits)
      val addr = in UInt (4 bits)
    }
    val rd = new Bundle {
      val en = in Bool()
      val data = out Bits (8 bits)
      val addr = in UInt (4 bits)
    }
  }
  val ram = new Ram_1w_1r(8,16)
  io.wr <> ram.io.wr
  io.rd <> ram.io.rd
}

object Main{
  def main(args: Array[String]): Unit = {
    SpinalVerilog(new useBlackbox)
  }
}