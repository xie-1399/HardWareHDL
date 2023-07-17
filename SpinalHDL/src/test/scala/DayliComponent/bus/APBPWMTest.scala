package DayliComponent.bus

import org.scalatest.FunSuite
import spinal.core._
import spinal.core.sim._
import spinal.lib.bus.amba3.apb.{Apb3, Apb3Config}
import scala.util.Random
import Common.SpinalSimConfig
import myAPB._
//IMPORTANT this testbench use hardware signal reflection to avoid compilation issues when the dut isn't implemented

//Run this scala test to generate and check that your RTL work correctly
class APBPWMTest extends FunSuite{
  var compiled: SimCompiled[myAPBPWM] = null

  test("compile") {
    compiled = SpinalSimConfig().compile(
      new myAPBPWM(
        apbConfig = ApbConfig(
          addressWidth = 8,
          dataWidth = 32,
          selWidth = 1
        ),
        timerWidth = 8
      )
    )
  }

  test("testbench") {
    compiled.doSim{dut =>
      dut.clockDomain.forkStimulus(10)
      val io = dut.io.flatten.map(e => e.getName().replace("io_","") -> e).toMap

      def apbWrite(address : BigInt, data : BigInt) : Unit = {
        io("apb_PSEL").assignBigInt(1)
        io("apb_PENABLE").assignBigInt(0)
        io("apb_PWRITE").assignBigInt(1)
        io("apb_PADDR").assignBigInt(address)
        io("apb_PWDATA").assignBigInt(data)
        dut.clockDomain.waitSampling()
        io("apb_PENABLE").assignBigInt(1)
        dut.clockDomain.waitSamplingWhere(io("apb_PREADY").toBigInt == 1)
        io("apb_PSEL").assignBigInt(0)
        io("apb_PENABLE").randomize()
        io("apb_PADDR").randomize()
        io("apb_PWDATA").randomize()
        io("apb_PWRITE").randomize()
      }

      def apbRead(address : BigInt) : BigInt = {
        io("apb_PSEL").assignBigInt(1)
        io("apb_PENABLE").assignBigInt(0)
        io("apb_PADDR").assignBigInt(address)
        io("apb_PWRITE").assignBigInt(0)
        dut.clockDomain.waitSampling()
        io("apb_PENABLE").assignBigInt(1)
        dut.clockDomain.waitSamplingWhere(io("apb_PREADY").toBigInt == 1)
        io("apb_PSEL").assignBigInt(0)
        io("apb_PENABLE").randomize()
        io("apb_PADDR").randomize()
        io("apb_PWDATA").randomize()
        io("apb_PWRITE").randomize()
        io("apb_PRDATA").toBigInt
      }

      def apbReadAssert(address : BigInt, data : BigInt, mask : BigInt, message : String) : Unit =  assert((apbRead(address) & mask) == data, message)

      def checkDutyCycle(value : Int): Unit ={
        dut.clockDomain.waitSampling(256*6)
        var highs, toggles = 0
        var last = io("pwm").toBigInt
        for(_ <- 0 until 256){
          dut.clockDomain.waitSampling()
          val pwm = io("pwm").toBigInt
          if(pwm == 1){
            highs += 1
          }
          //做了一次变换
          if(last != pwm) toggles += 1
          last = pwm
        }

        assert(highs == value, "Wrong PWM signal")
        assert(toggles == 2, "Wrong PWM signal")
      }

      apbWrite(0x04,0x000000C0);
      apbWrite(0x00,0x00000001);
      checkDutyCycle(0xC0);
      apbWrite(0x04,0x00000040);
      checkDutyCycle(0x40);
      apbReadAssert(0x00,0x00000001,0x00000001,"APB pwm enable read doesn't work properly.");
      apbReadAssert(0x04,0x00000040,0x000000FF,"APB duty cycle read doesn't work properly.");
      apbWrite(0x00,0x00000000);

      fork{
        val last = io("pwm").toBigInt
        waitUntil(io("pwm").toBigInt != last)
        simFailure("The enable register isn't effective")
      }
      dut.clockDomain.waitSampling(256*3)
    }
  }
  def doCmd(cmd : String) : Int = {
    import scala.sys.process._
    println(cmd)
    cmd !
  }
}


