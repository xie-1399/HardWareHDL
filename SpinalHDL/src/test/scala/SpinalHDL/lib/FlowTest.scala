package SpinalHDL.lib

import spinal.core.sim._
import org.scalatest.FunSuite
import Common.SpinalSimConfig
import SpinalHDL.lib.FlowSource.UseFlow

import scala.util.Random

class FlowTest extends FunSuite{
  var compiled:SimCompiled[UseFlow] = null
  test("compile"){
    compiled = SpinalSimConfig().compile(new UseFlow)
  }

  test("testbench"){
    compiled.doSim(seed = 42){
      dut =>
        dut.clockDomain.forkStimulus(10)
        def operation() = {
          dut.io.flowA.valid #= Random.nextInt(6) > 3
          dut.io.flowA.payload #= Random.nextInt(100)
        }
        SpinalSimConfig.onlySample(dut.clockDomain,operation = operation,iter = 300)
    }
  }
}
