package DayliComponent.Counter

import spinal.core._
import spinal.lib._


case class Clint(time:Int) extends Component{
  /*
  定时器，根据某个比较结果来响应事件的触发
  */
  val io = new Bundle{
    val output = out Bool()
    val tick1 = in Bool()
    val tick2 = in Bool()
  }
  val mtime = Counter(time)
  val ctime = Counter(time)
  io.output := False
  when(io.tick1){
    mtime.increment() //下一拍才会将定时器的值加1
  }
  when(io.tick2){
    ctime.increment()
  }

  when(mtime.value === ctime.value){
    io.output := True
  }

}

case class counter(width:Int) extends Component{
  val io = new Bundle{
    val test = in UInt(width bits)
    val clear = in Bool()
    val value = out UInt(width bits)
    val full = out Bool()
  }
  io.full := False
  val counter = Counter(1 << width)
  when(io.clear){
    counter.clear()
  }otherwise{
    counter.increment()
  }
  when(counter.willOverflow){
    io.full := True
  }
  io.value := counter.value
}
