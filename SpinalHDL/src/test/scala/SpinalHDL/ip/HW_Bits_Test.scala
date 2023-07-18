package SpinalHDL.ip

import spinal.core.sim._
import org.scalatest.FunSuite
import Common.SpinalSimConfig

import scala.util.Random

//this is a test template of spinalHDL

class HW_Bits_Test extends FunSuite{
  var compiled:SimCompiled[HW_Bits] = null
  test("compile"){
    compiled = SpinalSimConfig().compile(new HW_Bits)
  }

  test("Bits High"){
    compiled.doSim(seed = 42){
      dut =>
        dut.clockDomain.forkStimulus(10)
        def opeartion() = {
          dut.io.value #= Random.nextInt(10)
        }
        SpinalSimConfig.onlySample(dut.clockDomain,operation = opeartion,iter = 10)
    }
  }
}
