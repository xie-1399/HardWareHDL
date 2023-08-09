package SpinalHDL.ip

import spinal.core.sim._
import org.scalatest.FunSuite
import Common.SpinalSimConfig

import scala.util.Random

//this is a test template of spinalHDL

class HW_SInt_Test extends FunSuite{
  var compiled:SimCompiled[HW_SInt] = null
  test("compile"){
    compiled = SpinalSimConfig().compile(new HW_SInt)
  }

  test("SInt convert"){
    compiled.doSim(seed = 42){
      dut =>
        dut.clockDomain.forkStimulus(10)
        def opeartion() = {
          dut.io.input #= Random.nextInt(10) - 5
          println("------------------------------")
          val value = dut.io.output.toInt
          println(dut.io.input.toInt)
          println(value)
          println(value.toBinaryString)
        }
        SpinalSimConfig.onlySample(dut.clockDomain,operation = opeartion,iter = 20)
    }
  }
}
