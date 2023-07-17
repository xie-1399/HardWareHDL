package DayliComponent

import org.scalatest.FunSuite
import spinal.core.Component.push
import spinal.core.sim._
import Common.SpinalSimConfig

import scala.util.Random

class MuxlibTest extends FunSuite{
  var compiled:SimCompiled[Muxlib] = null
  test("compile"){
    compiled = SpinalSimConfig().compile(Muxlib())
  }
  test("testbench"){
    compiled.doSim(seed = 42){
      dut =>
        dut.clockDomain.forkStimulus(10)

        for(_ <- 0 until 100){
          val randomsrc1 = Random.nextInt(3)
          val randomsrc2 = Random.nextInt(3)
          dut.io.src1 #= randomsrc1
          dut.io.src2 #= randomsrc2
          dut.io.bitwise #= Random.nextInt(3)
          val randombool = Random.nextDouble() > 0.5
          dut.io.cond #= randombool

          dut.clockDomain.waitSampling()
          val value = if(randombool) randomsrc1 else randomsrc2
          assert(dut.io.output.toBigInt == value, s"not match ")
        }

    }
  }

}