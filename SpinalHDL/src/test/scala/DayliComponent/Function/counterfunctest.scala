package DayliComponent.Function

import org.scalatest.FunSuite
import spinal.core._
import spinal.core.sim._
import Common.SpinalSimConfig
import DayliComponent.wavesRtl._
import scala.collection.mutable._
import scala.util.Random

class CounterTester extends FunSuite {
  var compiled: SimCompiled[circuit2] = null

  test("compile") {
    compiled = SpinalSimConfig().compile {
      val c = new circuit2()
      c.value.simPublic()
      c
    }
  }

  test("testbench") {
    compiled.doSim(seed = 42){dut =>
      dut.clockDomain.forkStimulus(10)
      var counter = 0
      for(_ <- 0 until 100){
        val array = new ArrayBuffer[Boolean]()
        val a = Random.nextDouble() < 0.5
        dut.io.a #= a
        val b = Random.nextDouble() < 0.5
        dut.io.b #= b
        val c = Random.nextDouble() < 0.5
        dut.io.c #= c
        val d = Random.nextDouble() < 0.5
        dut.io.d #= d
        array += a
        array += b
        array += c
        array += d
        val value = array.count(_ == true)
        dut.clockDomain.waitSampling()
        assert(value == dut.value.toInt,s" value error")
        if(value % 2 == 0){
          assert(dut.io.q.toBoolean == true,s"dut.io.q missmatch")
        }else{
          assert(dut.io.q.toBoolean == false,s"dut.io.q missmatch")
        }
      }
    }
  }
}