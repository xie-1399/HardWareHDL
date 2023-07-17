package SpinalHDL.ip

import spinal.core._
import spinal.lib._

case class signal() extends Bundle{
  // just wire Bundle in the component
  val a = UInt(8 bits)
  val b = UInt(16 bits)
}

class clock extends Component {
  //external clock
  val clkA = ClockDomain.external("clkA")
  val clkB = ClockDomain.external("clkB")
  clkB.setSynchronousWith(clkA)

  val regA = clkA(Reg(UInt(8 bits)))
  //val regB = clkB(Reg(UInt(8 bits))).addTag(crossClockDomain)
  val regB = clkB(Reg(UInt(8 bits)))
  val tmp = regA + regA
  regB := tmp
  regA := 1
  val sig = signal()
}

class syncRead2Write extends Component{
  val io = new Bundle{
    val pushClock,pushRst = in Bool()
    val readPtr = in UInt(8 bits)
  }
  val pushCC = new ClockingArea(ClockDomain(io.pushClock,io.pushRst)){
    val pushGray = RegNext(toGray(io.readPtr)) init(0)
  }

}

//about internal clock
class Pll extends Component{
  val io = new Bundle {
    val clkIn = in Bool()
    val clkOut  = out Bool()
    val reset  = out Bool()
  }
  io.clkOut := io.clkIn
  io.reset  := False
}
class InternalClockWithPllExample extends Component {
  val io = new Bundle {
    val clk100M = in Bool()
    val aReset  = in Bool()
    val result  = out UInt (4 bits)
  }
  // myClockDomain.clock will be named myClockName_clk
  // myClockDomain.reset will be named myClockName_reset
  val myClockDomain = ClockDomain.internal("myClockName")

  // Instanciate a PLL (probably a BlackBox)
  val pll = new Pll()
  pll.io.clkIn := io.clk100M

  // Assign myClockDomain signals with something
  myClockDomain.clock := pll.io.clkOut
  myClockDomain.reset := io.aReset || !pll.io.reset

  // Do whatever you want with myClockDomain
  val myArea = new ClockingArea(myClockDomain){
    val myReg = Reg(UInt(4 bits)) init(7)
    myReg := myReg + 1
    io.result := myReg
  }
}

class ExternalClockExample extends Component {
  val io = new Bundle {
    val result = out UInt (4 bits)
  }

  // On top level you have two signals  :
  //     myClockName_clk and myClockName_reset
  val myClockDomain = ClockDomain.external("myClockName")
  val myArea = new ClockingArea(myClockDomain){
    val myReg = Reg(UInt(4 bits)) init(7)
    myReg := myReg + 1
    io.result := myReg
  }
}


class memclock extends Component{
  val io = new Bundle{
    val CLK = in Bool()
    val a = in Bool()
  }
  val Rega = RegInit(False)
  val cd = ClockDomain(io.CLK)

}


object clock extends App{
  SpinalVerilog(new memclock)
}