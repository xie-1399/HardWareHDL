//package DayliComponent.IO
//import org.scalatest.FunSuite
//
//import workshop.common.WorkshopSimConfig
//import spinal.core._
//import spinal.core.sim._
//import spinal.lib.com.uart.sim._
//import spinal.lib.sim._
//import scala.util.Random
//
//class ApbUARTTest extends FunSuite{
//  var compiled:SimCompiled[ApbUART] = null
//
//  test("compiled") {
//    compiled = WorkshopSimConfig().compile
//    {
//      val dut = SpinalConfig(
//          mode = Verilog,
//          defaultClockDomainFrequency = FixedFrequency(50 MHz)
//        ).generate(new UartCtrlUsageExample)
//        dut
//    }
//  }
//
//  test("testbench"){
//    compiled.doSimUntilVoid{
//      dut =>
//        //simulation bus to send value to the uart
//        val clockperiod = (1e12 / (50 MHz).toDouble).toLong
//        dut.clockDomain.forkStimulus(clockperiod)
//        val uartBaudRate = 115200
//        val uartBaudPeriod = (1e12 / uartBaudRate).toLong
//        //UART sim
////        val uartTx = UartDecoder(
////          uartPin = dut.io.uart.txd,
////          baudPeriod = uartBaudPeriod
////        )
////        val uartRx = UartEncoder(
////          uartPin = dut.io.uart.rxd,
////          baudPeriod = uartBaudPeriod
////        )
//        def init(dut:ApbUART): Unit = {
//          dut.io.myaxi4.ar.valid #= false
//          dut.io.myaxi4.aw.valid #= false
//          dut.io.myaxi4.w.valid #= false
//          dut.io.myaxi4.b.ready #= true
//          dut.io.myaxi4.r.ready #= false
//        }
//        def drivebus(data:BigInt,address:BigInt): Unit = {
//          //send a write request
//          dut.io.myaxi4.aw.valid #= false
//          dut.io.myaxi4.aw.payload.addr #= BigInt(32,Random)
//          dut.io.myaxi4.aw.payload.burst #= Random.nextInt(3)
//          dut.io.myaxi4.aw.payload.len #= 0
//          dut.io.myaxi4.aw.payload.size #= 0
//          dut.clockDomain.waitSampling()
//
//          dut.io.myaxi4.aw.valid #= true
//          dut.io.myaxi4.aw.payload.addr #= address
//          dut.io.myaxi4.aw.payload.burst #= 0
//          dut.io.myaxi4.aw.payload.len #= 0
//          dut.io.myaxi4.aw.payload.size #= 2
//          //success state
//          dut.io.myaxi4.w.valid #= true
//          dut.io.myaxi4.w.ready #= true
//          dut.io.myaxi4.w.payload.data #= data
//          dut.io.myaxi4.w.payload.strb #= 15
//          dut.io.myaxi4.w.payload.last #= true
//          dut.clockDomain.waitSampling(2)
//        }
//
//        init(dut)
//        drivebus(0x00000068,0x00010000)
//        dut.clockDomain.waitSampling(5200)
////        drivebus(0x00000065,0x00010004)
////        drivebus(0x0000006C,0x00010008)
////        drivebus(0x0000006C,0x0001000C)
//    }
//
//
//  }
//}
