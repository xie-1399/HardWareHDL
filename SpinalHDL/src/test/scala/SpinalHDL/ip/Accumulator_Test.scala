package SpinalHDL.ip

import spinal.core.sim._
import org.scalatest.FunSuite
import Common.SpinalSimConfig
import SpinalHDL.ip._
import scala.util.Random

//this is a test template of spinalHDL

class Accumulator_Test extends FunSuite{
  var compiled:SimCompiled[Accumulator] = null
  test("compile"){
    compiled = SpinalSimConfig().compile(new Accumulator)
  }

  test("accu"){
    compiled.doSim(seed = 42){
      dut =>
        dut.clockDomain.forkStimulus(10)
        dut.io.cond #= 0
        dut.clockDomain.waitSampling()
        def opeartion() = {
          dut.io.cond #= Random.nextInt(2)
        }
        SpinalSimConfig.onlySample(dut.clockDomain,operation = opeartion,iter = 10)
    }
  }
}
