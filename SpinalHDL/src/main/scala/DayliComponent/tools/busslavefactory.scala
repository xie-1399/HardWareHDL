package DayliComponent.tools
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba3.apb._
import spinal.lib.bus.misc.BusSlaveFactory

//see how to use busslave
//总结一下，其实这一部分主要是为了方便总线的地址映射，可以方便创造寄存器并映射一个地址，并且可以将模块内的寄存器的值进行有效的返回，
//并且可以较为方便的捕获总线对某个地址的写行为，方便了总线对某个地址的读写行为


//Create a timer
case class Timer(width : Int) extends Component {
  val io = new Bundle {
    val tick  = in Bool()
    val clear = in Bool()
    val limit = in UInt(width bits)

    val full  = out Bool()
    val value = out UInt(width bits)
  }

  val counter = Reg(UInt(width bits))
  when(io.tick && !io.full) {
    counter := counter + 1
  }
  when(io.clear) {
    counter := 0
  }

  io.full := counter === io.limit && io.tick
  io.value := counter

  //完成寄存器的地址映射

  def driveFrom(busCtrl: BusSlaveFactory, baseAddress: BigInt)(ticks: Seq[Bool], clears: Seq[Bool]) = new Area {
    // Offset 0 => clear/tick masks + bus
    val ticksEnable = busCtrl.createReadAndWrite(Bits(ticks.length bits), baseAddress + 0, 0) init (0) //放置于字的bit offset处
    val clearsEnable = busCtrl.createReadAndWrite(Bits(clears.length bits), baseAddress + 0, 16) init (0)
//    PADDR = 0 :
//    io_apb_PRDATA[2: 0] = bridgeA_ticksEnable;
//    io_apb_PRDATA[17: 16] = bridgeA_clearsEnable;

    val busClearing = False

    io.clear := (clearsEnable & clears.asBits).orR | busClearing
    io.tick := (ticksEnable & ticks.asBits).orR

    // Offset 4 => read/write limit (+ auto clear)
    busCtrl.driveAndRead(io.limit, baseAddress + 4)
    busClearing.setWhen(busCtrl.isWriting(baseAddress + 4))

    // Offset 8 => read timer value / write => clear timer value
    busCtrl.read(io.value, baseAddress + 8) //read会将读出来的数据传递给PRDATA
    busClearing.setWhen(busCtrl.isWriting(baseAddress + 8))
  }
}

case class busslavefactory() extends Component{
  val io = new Bundle{
    val apb = slave(Apb3(addressWidth = 8,dataWidth = 32))
    val fullA = out Bool()
    val fullB = out Bool()
    val external = new Bundle{
      val tick  = in Bool()
      val clear = in Bool()
    }
  }

  val clockDivider = new Area{
    val counter = Reg(UInt(4 bits)) init(0)
    counter := counter + 1
    val full = counter === 0xF
  }

  val apbCtrl = Apb3SlaveFactory(io.apb)
  val timerA  = Timer(width = 16)
  val bridgeA = timerA.driveFrom(apbCtrl,0x00)(
    ticks  = List(True,clockDivider.full,io.external.tick),
    clears = List(timerA.io.full,io.external.clear)
  )

  val timerB  = Timer(width = 8)
  val bridgeB = timerB.driveFrom(apbCtrl,0x10)(
    ticks  = List(True,clockDivider.full,io.external.tick),
    clears = List(timerB.io.full,io.external.clear)
  )

  io.fullA := timerA.io.full
  io.fullB := timerB.io.full
}

object busslavefactory extends App{
  SpinalVerilog(busslavefactory())
}

