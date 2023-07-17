package DayliComponent

import org.scalatest.FunSuite
import spinal.core.sim._
import Common.SpinalSimConfig

import scala.util.Random

class logicorRegTest extends FunSuite{
  var compiled:SimCompiled[logicorReg] = null
  test("compile"){
    compiled = SpinalSimConfig().compile(new logicorReg())
  }
  test("testbench"){
    compiled.doSim(seed = 42){
      dut =>
        dut.clockDomain.forkStimulus(10)
        for(_ <- 0 until 100){
          dut.io.a #= Random.nextDouble() < 0.5
          dut.io.b #= Random.nextInt(8)
          dut.io.valid #= true
          dut.clockDomain.waitSampling()

          dut.io.a #= Random.nextDouble() < 0.5
          dut.io.b #= Random.nextInt(8)
          dut.io.valid #= false
          dut.clockDomain.waitSampling()
        }

    }
  }

}
