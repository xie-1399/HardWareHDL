package DayliComponent.Excute

import org.scalatest.FunSuite
import spinal.core.sim._
import Common.SpinalSimConfig
import spinal.core._
import scala.util.Random

class ShifterTest extends FunSuite {
  var logiccompiled: SimCompiled[Shifter] = null
  var arithcompiled: SimCompiled[Shifter] = null
  var barrelcompiled: SimCompiled[Shifter] = null
  test("compile") {
    //true equlas right
    logiccompiled = SpinalSimConfig().compile(Shifter("logic",32,shifternum = 10))
    arithcompiled = SpinalSimConfig().compile(Shifter("arithmetic",32))
    barrelcompiled = SpinalSimConfig().compile(Shifter("barrel",32))
  }

  //in fact should have a function to get the shifter value
  test("logictest"){
    logiccompiled.doSim(name = "logicshifter",seed = 42){
      dut =>
        dut.clockDomain.forkStimulus(10)
        var data = 3
        def logictest(value:BigInt,left:Boolean) = {
          dut.io.direction #= left
          dut.io.inputdata #= value
          dut.clockDomain.waitSampling()
        }
        dut.clockDomain.waitSampling(5)
        for(i <- 0 until 100) {
          //test the logic for the left
          logictest(data & 0x7FFFFFFF, true)
          assert(data < Int.MaxValue)
          //assert((data << 10 & 0x7FFFFFFF) == dut.io.outputdata.toInt, s"${i} mismatch , data = ${data} , out = ${dut.io.outputdata.toInt}")
          data = (data << 1 & 0x7FFFFFFF)
        }
    }
  }

  test("arithmetictest"){
    //right -> signal
    arithcompiled.doSim(name = "arithmetictest",seed = 42) {
      dut =>
        dut.clockDomain.forkStimulus(10)
        var data:BigInt = 0x80000001
        def arithtest(value: BigInt,left:Boolean) = {
          dut.io.direction #= left
          dut.io.inputdata #= value
          dut.clockDomain.waitSampling()
        }
        dut.clockDomain.waitSampling(5)
        for (i <- 0 until 100) {
          data = data & 0x00000000ffffffffL //in the case
          //test the signal for the right
          arithtest(data, true)
          dut.clockDomain.waitSampling()
          //assert((data << 10 & 0x7FFFFFFF) == dut.io.outputdata.toInt, s"${i} mismatch , data = ${data} , out = ${dut.io.outputdata.toInt}")
          data = dut.io.outputdata.toBigInt
        }
    }
  }

  test("barreltest"){
    barrelcompiled.doSim(name = "barreltest", seed = 42) {
      dut =>
        dut.clockDomain.forkStimulus(10)
        var data: BigInt = 0x80000001

        def barreltest(value: BigInt, left: Boolean) = {
          dut.io.direction #= left
          dut.io.inputdata #= value
          dut.clockDomain.waitSampling()
        }

        dut.clockDomain.waitSampling(5)
        for (i <- 0 until 100) {
          data = data & 0x00000000ffffffffL //in the case
          //test the signal for the right
          barreltest(data, true)
          dut.clockDomain.waitSampling()
          //assert((data << 10 & 0x7FFFFFFF) == dut.io.outputdata.toInt, s"${i} mismatch , data = ${data} , out = ${dut.io.outputdata.toInt}")
          data = dut.io.outputdata.toBigInt
        }
    }
  }

}
