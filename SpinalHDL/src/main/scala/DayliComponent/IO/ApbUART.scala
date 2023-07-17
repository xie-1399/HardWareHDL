package DayliComponent.IO

//learn use Uart Pro
import spinal.core._
import spinal.lib._
import spinal.lib.com.uart._
import spinal.lib.bus.amba3.apb._
import spinal.lib.bus.amba4.axi._
import DayliComponent.bus.myAPB._
import spinal.lib.soc.pinsec.PinsecTimerCtrl

class ApbUART extends Component {
  val uartCtrlConfig = UartCtrlMemoryMappedConfig(
    uartCtrlConfig = UartCtrlGenerics(
      dataWidthMax = 8,
      clockDividerWidth = 20,
      preSamplingSize = 1,
      samplingSize = 5, //rxd size
      postSamplingSize = 2 //drop
    ),
    txFifoDepth = 16,
    rxFifoDepth = 16
  )
  val io = new Bundle{
    val myaxi4 = slave(Axi4(busconfig.axiConfig))
    val uart = master(Uart())
  }
  val bridge = Axi4SharedToApb3Bridge(
    addressWidth = 32,
    dataWidth = 32,
    idWidth = 4
  )
  io.myaxi4.toShared() <> bridge.io.axi
  //change Apb3 cmd to the UART
  val uartctrl = myApb3UartCtrl(uartCtrlConfig)
//  bridge.io.apb <> uartctrl.io.apb

  //Todo for the ApbDecoder
  val apbDecoder =
    Apb3Decoder(
    master = bridge.io.apb,
    slaves = List(
      uartctrl.io.apb -> (0x10000,4 KiB) // Todo
    )
  )
  io.uart <> uartctrl.io.uart
}


// how use the uart
class UartCtrlUsageExample extends Component{
  val io = new Bundle{
    val uart = master(Uart())
    val switchs = in Bits(8 bits)
    val leds = out Bits(8 bits)
  }

  val uartCtrl: UartCtrl = UartCtrl(
    config = UartCtrlInitConfig(
      baudrate = 921600,
      dataLength = 7,  // 8 bits
      parity = UartParityType.NONE,
      stop = UartStopType.ONE
    )
  )
  uartCtrl.io.uart <> io.uart

  //Assign io.led with a register loaded each time a byte is received
  io.leds := uartCtrl.io.read.toFlow.toReg()

  //Write the value of switch on the uart each 4000 cycles
  val write = Stream(Bits(8 bits))
  write.valid := CounterFreeRun(2000).willOverflow
  write.payload := io.switchs
  write >-> uartCtrl.io.write

  //Write the 0x55 and then the value of switch on the uart each 4000 cycles
  //  val write = Stream(Fragment(Bits(8 bits)))
  //  write.valid := CounterFreeRun(4000).willOverflow
  //  write.fragment := io.switchs
  //  write.last := True
  //  write.m2sPipe().insertHeader(0x55).toStreamOfFragment >> uartCtrl.io.write
}