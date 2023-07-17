package DayliComponent.tools
import spinal.core._
import spinal.core.internals.Operator
import spinal.lib._

// Flow and Stream can convert(Just using the to)

case class IR(width:Int) extends Bundle{
  val ir = UInt(width bits)
}

case class FilterConfig(hwFreq:HertzNumber = 200 MHz,
                        sampleFreq:HertzNumber = 1.92 MHz)

class Filter extends Component{
  val in = slave Flow(IR(8))
  val out = master Stream(IR(8))
  //How about the stream queue(just a Stram fifo)
  //when the fifo pop valid is true -> send the data out
  in.toStream.queue(16) >> out
}

class FilterCC extends Component{
  val input = slave Flow (Bits (8 bits))
  val output = master Stream (Bits (8 bits))
  val flush = in Bool()

  val clockSMP = ClockDomain.external("smp")  //sample clock
  val clockHW = ClockDomain.external("hw")  //real clock Freq

  val u_fifo_in = StreamFifoCC(
    dataType = Bits(8 bits),
    depth = 8,
    pushClock = clockSMP,
    popClock = clockDomain
  )
  //see clock dirve
  input.toStream >-> u_fifo_in.io.push
  output << u_fifo_in.io.pop
}

class StreamUntil extends Component{
  val input = in Bits( 8 bits)
  val inputvalid = in Bool()
  val output = master Stream(Bits( 8 bits))
  val fifo = new StreamFifo(Bits( 8 bits),16)

  val temp = Stream(input) //set valid and payload
  temp.valid := inputvalid
  temp.payload := input

  temp >> fifo.io.push
  fifo.io.pop >> output

}


class Streamfifo extends Component {
  //like this
  val fifo =  Stream(UInt(8 bits)).queue(4)
  //Fragement(using this to transpose some big thing)
  val a = slave Flow(Fragment(UInt(8 bits)))
  val b = out UInt()
  val c = out UInt()
  b := a.payload.fragment
  c := a.fragment
  //can use last and some function to know the trans
  when(a.payload.last){
    ???
  }
}

object Streamfifo extends App{
  SpinalVerilog(new StreamUntil)
}