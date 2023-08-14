package SpinalHDL.ip
import spinal.core._
import spinal.lib._
import spinal.core.sim._
import org.scalatest.FunSuite
import Common.SpinalSimConfig
import spinal.lib.sim._

import scala.util.Random

object StramFlowSim{

  def StreamSimulation[T <: Data](s:Stream[T],useDriver:Boolean = false, setValue:(T) => Boolean = null,
                                  useMonitor:Boolean = false,monitor:(T) => Boolean = null,clockDomain: ClockDomain) = {
    //init Stream data
    if(useDriver){
      StreamDriver(s,clockDomain = clockDomain){
        payload =>
          setValue(payload)
      }
    }

    //Monitor Stream data
    if(useMonitor){
      StreamMonitor(s,clockDomain){
        payload =>
          monitor(payload)
      }
    }
    "Stream Simulation"
  }
}

class HW_Stream_Test extends FunSuite{
  var compiled:SimCompiled[controlStream] = null
  var halt:SimCompiled[haltStream] = null
  test("compile"){
    compiled = SpinalSimConfig().compile(new controlStream)
    halt = SpinalSimConfig().compile(new haltStream)
  }

  test("control Stream"){
    compiled.doSim(seed = 42){
      dut =>
        var number = 0
        dut.clockDomain.forkStimulus(10)

        def func(payload: carryData) = {
          payload.data #= Random.nextInt(100)
          payload.signal #= Random.nextInt(10) > 5
          if (dut.io.request.valid.toBoolean && dut.io.request.ready.toBoolean) {
            number += 1
          }
          true
        }
        StramFlowSim.StreamSimulation(dut.io.request,useDriver = true,setValue = func,clockDomain = dut.clockDomain)
        dut.clockDomain.waitActiveEdgeWhere(number == 10)
    }
  }

  test("halt Stream") {
    halt.doSim(seed = 42) {
      dut =>
        dut.clockDomain.forkStimulus(10)

        def func(payload: carryData) = {
          payload.data #= Random.nextInt(100)
          payload.signal #= Random.nextInt(10) > 5
          true
        }
        StramFlowSim.StreamSimulation(dut.io.request,useDriver = true,setValue = func,clockDomain = dut.clockDomain)
        for(idx <- 0 until 100){
          dut.io.stop #= Random.nextInt(10) > 5
          dut.clockDomain.waitSampling()
        }

    }
  }



}
