package DayliComponent.Counter

import org.scalatest.FunSuite
import spinal.core.sim._
import Common.SpinalSimConfig

import scala.util.Random

class ClintTest extends FunSuite {
  var compiled: SimCompiled[Clint] = null

  test("compile") {
    compiled = SpinalSimConfig().compile(Clint(10))
  }
  test("Clint testbench") {
    compiled.doSim(seed = 42) {
      dut =>
        dut.clockDomain.forkStimulus(10)

        for (_ <- 0 until 500) {
          dut.io.tick2 #= Random.nextDouble() < 0.5
          dut.io.tick1 #= Random.nextDouble() < 0.5
          dut.clockDomain.waitSampling()
        }

    }
  }

}
