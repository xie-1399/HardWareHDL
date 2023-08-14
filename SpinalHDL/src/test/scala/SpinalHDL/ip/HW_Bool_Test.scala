package SpinalHDL.ip

import spinal.core.sim._
import org.scalatest.FunSuite
import Common.SpinalSimConfig

import scala.util.Random

class HW_Bool_Test extends FunSuite{
  var compiled: SimCompiled[HW_Bool] = null
  test("compile") {
    compiled = SpinalSimConfig().compile(new HW_Bool(false))
  }

  test("test bench") {
    compiled.doSim(seed = 42) {
      dut =>
        dut.clockDomain.forkStimulus(10)
        def operation() = {
          dut.io.start #= Random.nextBoolean()
          dut.io.sel #= Random.nextBoolean()
        }
        SpinalSimConfig.onlySample(dut.clockDomain,operation = operation,iter = 300)
    }
  }
}
