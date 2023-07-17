package DayliComponent.Counter

import org.scalatest.FunSuite
import spinal.core.sim._
import Common.SpinalSimConfig

import scala.util.Random

//Run this scala test to generate and check that your RTL work correctly
class CounterTester extends FunSuite {
  var compiled: SimCompiled[counter] = null

  test("compile") {
    compiled = SpinalSimConfig().compile(counter(width = 4))
  }

  test("testbench") {
    compiled.doSim(seed = 42){dut =>
      dut.clockDomain.forkStimulus(10)
      var counter = 0
      for(_ <- 0 until 100){
        dut.io.clear #= Random.nextDouble() < 0.1
        dut.io.test  #= Random.nextInt(3)
        println("before:"+ dut.io.test.toInt)
        dut.clockDomain.waitSampling() //将输入打进来
        println("after:" + dut.io.test.toInt)
        assert(dut.io.value.toInt == counter, "dut.io.value missmatch")
        assert(dut.io.full.toBoolean == (counter == 15), "dut.io.full missmatch")
        counter = if(dut.io.clear.toBoolean) 0 else (counter + 1) & 0xF
      }
    }
  }
}