package DayliComponent.tools

import spinal.core.sim._
import org.scalatest.{FunSuite, durations}
import Common.SpinalSimConfig

import scala.util.Random

class Streamfifotest extends FunSuite{
  var compiled:SimCompiled[Filter] = null
  test("compile"){
    compiled = SpinalSimConfig().compile(new Filter)
  }

  test("testbench"){
    compiled.doSim(seed = 42){
      dut =>
        dut.clockDomain.forkStimulus(10)
        def getin(dut:Filter,num:Int): Unit = {
          for(i <- 0 until num){
            dut.in.payload.ir #= Random.nextInt(256) & 0xFF
            dut.in.valid #= true
            dut.out.ready #= true
            dut.clockDomain.waitSampling()
          }
          simSuccess()
        }
        //init
        dut.in.payload.ir #= Random.nextInt(256) & 0xFF
        dut.in.valid #= false
        dut.out.ready #= false
        dut.clockDomain.waitSampling()

        getin(dut,32)
    }
  }

}
