package DayliComponent.bus

import org.scalatest.{FunSuite, durations}
import spinal.core._
import spinal.core.sim._
import Common.SpinalSimConfig
import spinal.lib.bus.amba3.apb.{Apb3, Apb3Config}

import scala.util.Random
import myAPB._
import spinal.core

class APBStateMachineTest extends FunSuite {
  var compiled: SimCompiled[myAPBStateMachine] = null

  test("compile") {
    compiled = SpinalSimConfig().compile{
      val c = new myAPBStateMachine(
        apbConfig =  ApbConfig(
          addressWidth = 8,
          dataWidth = 32,
          selWidth = 1
        )
        ,dataWidth = 32
    )
    c.ram.simPublic()
    c
    }
  }

  test("testbench"){
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(10)
      val io = dut.io.flatten.map(e => e.getName().replace("io_","") -> e).toMap

      def apbWrite(address: BigInt, data: BigInt): Unit = {
        io("apb_PSEL").assignBigInt(0)
        io("apb_PENABLE").assignBigInt(0)
        io("apb_PADDR").randomize()
        io("apb_PWRITE").randomize()
        dut.clockDomain.waitSampling()

        io("apb_PSEL").assignBigInt(1)
        io("apb_PENABLE").assignBigInt(0)
        io("apb_PWRITE").assignBigInt(1)
        io("apb_PADDR").assignBigInt(address)
        io("apb_PWDATA").assignBigInt(data)
        dut.clockDomain.waitSampling()

        io("apb_PENABLE").assignBigInt(1)
        io("apb_PSEL").assignBigInt(1)
        io("apb_PADDR").assignBigInt(address)
        io("apb_PWRITE").assignBigInt(1)
        io("apb_PWDATA").assignBigInt(data)
        dut.clockDomain.waitSampling()

        io("apb_PSEL").assignBigInt(0)
        io("apb_PENABLE").assignBigInt(1)
        io("apb_PADDR").assignBigInt(address)
        io("apb_PWDATA").assignBigInt(data)
        io("apb_PWRITE").assignBigInt(1)
        dut.clockDomain.waitSampling()
      }

      def apbRead(address: BigInt): BigInt = {
        io("apb_PSEL").assignBigInt(0)
        io("apb_PENABLE").assignBigInt(0)
        io("apb_PADDR").randomize()
        io("apb_PWRITE").randomize()
        dut.clockDomain.waitSampling()

        io("apb_PSEL").assignBigInt(1)
        io("apb_PENABLE").assignBigInt(0)
        io("apb_PADDR").assignBigInt(address)
        io("apb_PWRITE").assignBigInt(0)
        dut.clockDomain.waitSampling()

        io("apb_PSEL").assignBigInt(1)
        io("apb_PENABLE").assignBigInt(1)
        io("apb_PADDR").assignBigInt(address)
        io("apb_PWRITE").assignBigInt(0)
        dut.clockDomain.waitSampling()

        io("apb_PSEL").assignBigInt(0)
        io("apb_PENABLE").assignBigInt(1)
        io("apb_PADDR").assignBigInt(address)
        io("apb_PWDATA").randomize()
        io("apb_PWRITE").assignBigInt(0)
        dut.clockDomain.waitSampling() //注意这里正好
        io("apb_PRDATA").toBigInt
      }
      //write and Read test
      val ramContent = Array.fill(256)(Random.nextLong() & 0xFFFFFFFFl)
      for (i <- 0 to 255){
        apbWrite(i,ramContent(i))
        //println("content:",ramContent(i).toHexString)
        assert(dut.ram.getBigInt(i) == ramContent(i), s"dut.mem($i) wasn't written properly")
      }
      for (i <- 0 to 255) {
        val value = apbRead(i)
        assert(value == ramContent(i), s"dut.mem($i) wasn't read properly")
      }

    }
  }

}
