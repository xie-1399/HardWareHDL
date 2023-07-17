package SpinalHDL.riscv

import spinal.core._
import spinal.lib._
/*
工具类：
*/

object Utils {
  def signExtend[T <: BitVector](data: T,width:Int): T= { //T: UInt SInt Bits
    val dataWidth = data.getBitsWidth
    // the situation must be true
    assert(dataWidth <= width && dataWidth > 0)
    (B((width-1-dataWidth downto 0) -> data(dataWidth - 1)) ## data).as(data.clone().setWidth(width)) //符号位扩展
    //val extend = B(((width-1-dataWidth downto 0) -> data(dataWidth - 1)) ## data)
  }

  def zeroExtend[T <: BitVector](data:T,width:Int):T = {
    val dataWidth = data.getBitsWidth
    assert(dataWidth <= width && dataWidth > 0)
    (B((width - 1 - dataWidth downto 0) -> False) ## data).as(data.clone().setWidth(width))  //0符号位宽展还是0
  }

  def twosComplement(data:UInt):UInt = ~data + 1 //补码

  def delay(cycles:Int)(logic: => Unit): Unit = { //Todo 代码块
    assert(cycles >= 0)
    val delayCounter = Counter(cycles + 1)
    when(delayCounter.willOverflowIfInc){
     logic
    }
    delayCounter.increment()
  }


}
