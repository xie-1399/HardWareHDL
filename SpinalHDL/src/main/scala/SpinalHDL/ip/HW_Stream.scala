package SpinalHDL.ip

import spinal.core._
import spinal.lib._
import spinal.lib.bus.bmb.BmbUnburstify
/*
how to use and test Stream
*/

case class RGB(channelWidth:Int) extends Bundle{
  //is just wire
  val red = UInt(channelWidth bits)
  val green = UInt(channelWidth bits)
  val blue = UInt(channelWidth bits)

  def isBlack:Bool = red === 0 && green===0 && blue === 0
}

class sourceTosink extends Component{
  val source = Stream(RGB(32)) // if you want to use Stream()，the class should extends Bundle
  val sink = Stream(RGB(32))

  sink <-< source.throwWhen(source.payload.isBlack)
  when(sink.fire){
    ???
  }
  when(sink.isStall){
    ???
  }
  val newsink = sink.queue(2)
  val newsource = source.m2sPipe()

}

class stream extends Component {
  val io = new Bundle{
    val source = master(Stream(RGB(32)))   // master in(ready) out(valid payload)
    val sink = slave(Stream(RGB(32)))     // slave  out(ready) in(valid payload)
  }
  val len = Mux(io.source.payload.green === 0,U(0),U(1))
  io.source <> io.sink
}

class flow extends Component{
  val io = new Bundle{
    val source = master(Flow(RGB(32))) // master in(ready) out(valid payload)
    val sink = slave(Flow(RGB(32))) // slave  out(ready) in(valid payload)
  }
  val regdata = io.source.toReg()
  when(io.source.valid){
    ???
  }
  io.source <>  io.sink
}

case class carryData() extends Bundle{
  val data = Bits(10 bits)
  val signal = Bool()
}

class controlStream extends Component{
  val io = new Bundle {
    val request = slave Stream (carryData())
    val data = out Bits (10 bits)
    val signal = out Bool()
    val stall = out Bool()
    val stop = in Bool()
  }
  //m2sPipe
  val ms = io.request.m2sPipe()
  ms.ready := True

  io.stall := ms.isStall  //valid && !ready
  io.data := ms.payload.data
  io.signal := ms.payload.signal
}

//halt the request
class haltStream extends Component{
  val io = new Bundle{
    val request = slave Stream (carryData())
    val data = out Bits(10 bits)
    val signal = out Bool()
    val stall = out Bool()
    val stop = in Bool()
  }
  val halt = io.request.haltWhen(io.stop)
  halt.ready := True

  io.stall := halt.isStall  //valid && !ready
  io.data := halt.payload.data
  io.signal := halt.payload.signal
}