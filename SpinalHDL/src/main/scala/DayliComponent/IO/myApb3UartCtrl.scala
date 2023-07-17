package DayliComponent.IO

import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba3.apb.{Apb3, Apb3Config, Apb3SlaveFactory}
import spinal.lib.com.uart._

object myApb3UartCtrl{
  def getApb3Config = Apb3Config(
    addressWidth = 5,
    dataWidth = 32,
    selWidth = 1,
    useSlaveError = false
  )
}

case class myApb3UartCtrl(config : UartCtrlMemoryMappedConfig) extends Component{
  val io = new Bundle{
    val apb =  slave(Apb3(myApb3UartCtrl.getApb3Config))
    val uart = master(Uart(ctsGen = config.uartCtrlConfig.ctsGen, rtsGen = config.uartCtrlConfig.rtsGen))
    val interrupt = out Bool()
  }

  val uartCtrl = new UartCtrl(config.uartCtrlConfig)
  io.uart <> uartCtrl.io.uart

  val busCtrl = Apb3SlaveFactory(io.apb)
  val bridge = uartCtrl.driveFrom32(busCtrl,config)
  io.interrupt := bridge.interruptCtrl.interrupt
}