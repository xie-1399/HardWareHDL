package SpinalHDL.ip

import spinal.core.sim._
import org.scalatest.FunSuite
import Common.SpinalSimConfig

import scala.util.Random

class HW_Vec_Test extends FunSuite{
  var compiled:SimCompiled[HW_Vec] = null
  test("compile"){
    compiled = SpinalSimConfig().compile(new HW_Vec)
  }

  test("testbench"){
    compiled.doSim(seed = 42){
      dut =>
        dut.clockDomain.forkStimulus(10)
        def operation() = {
          dut.io.sel #= Random.nextInt(6)
          dut.io.start #= Random.nextInt(6)
        }
        SpinalSimConfig.onlySample(dut.clockDomain,operation = operation,iter = 300)
    }
  }
}
