package DayliComponent.Counter

//Clint，用于产生局部中断，也就是时钟中断软件中断
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba4.axi.{Axi4, Axi4Config, Axi4SlaveFactory}
import spinal.lib.bus.misc.BusSlaveFactory

case class MyClint(hartCount: Int) extends Area {
  val stop = False
  val time = Reg(UInt(64 bits)) init (0)
  when(!stop) {
    time := time + 1
  }

  // for every harts produce the timeInterrupt and software Interrupt
  val harts = for (hartId <- 0 until hartCount) yield new Area {
    val cmp = Reg(UInt(64 bits))
    val timeInterrupt = RegNext(time >= cmp)
    val softwareInterrupt = RegInit(False)
  }

  def driverFrom(bus: BusSlaveFactory, bufferTime: Boolean = false) = new Area {
    val IPI_ADDR = 0x0000
    val CMP_ADDR = 0x4000
    val TIME_ADDR = 0xBFF8

    bufferTime match {
      //need time buffer?
      case false => bus.readMultiWord(time, TIME_ADDR)
      case true => new Composite(this) {
        assert(bus.busDataWidth == 32)
        val timeMsb = RegNextWhen(time(63 downto 32), bus.isReading(TIME_ADDR))
        bus.read(time(31 downto 0), TIME_ADDR)
        bus.read(timeMsb, TIME_ADDR + 4)
      }
    }
    val hartsMapping = for (hartId <- 0 until hartCount) yield new Area {
      bus.writeMultiWord(harts(hartId).cmp, CMP_ADDR + 8 * hartId)
      bus.readAndWrite(harts(hartId).softwareInterrupt, IPI_ADDR + 4 * hartId, bitOffset = 0)
    }
  }
}

case class Axi4Clint(hartCount: Int) extends Component {
    val io = new Bundle {
      val bus = slave(Axi4(axiclint.axiconfig))
      val timerInterrupt = out Bits (hartCount bits)
      val softwareInterrupt = out Bits (hartCount bits)
      val time = out UInt (64 bits)
    }

    val factory = Axi4SlaveFactory(io.bus)
    val logic = MyClint(hartCount)
    logic.driverFrom(factory)

    for (hartId <- 0 until hartCount) {
      io.timerInterrupt(hartId) := logic.harts(hartId).timeInterrupt
      io.softwareInterrupt(hartId) := logic.harts(hartId).softwareInterrupt
    }

    io.time := logic.time
  }

object axiclint{
  val axiconfig = Axi4Config(
    addressWidth = 32,
    dataWidth = 32,
    idWidth = 2
  )
}
