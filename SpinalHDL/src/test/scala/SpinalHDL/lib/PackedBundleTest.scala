package SpinalHDL.lib

import Common.SpinalSimConfig
import SpinalHDL.lib.PackedBundleSource._
import org.scalatest.FunSuite
import spinal.core.sim._

import scala.util.Random

class PackedBundleTest extends FunSuite{
  var compiled:SimCompiled[UsePackedBundle] = null
  test("compile"){
    compiled = SpinalSimConfig().compile(new UsePackedBundle)
  }

  test("testbench"){
    compiled.doSim(seed = 42){
      dut =>
        dut.clockDomain.forkStimulus(10)
        def operation() = {
        }
        SpinalSimConfig.onlySample(dut.clockDomain,operation = operation,iter = 300)
    }
  }
}
