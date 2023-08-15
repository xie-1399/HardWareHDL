package SpinalHDL.lib.FlowSource

import spinal.core._
import spinal.lib._

//the lib flow usage

class UseFlow extends Component {
  val io = new Bundle{
    val flowA = slave Flow(Bits(10 bits))
  }

  //the common methods about the flow
  val flowB = io.flowA.clone()

  flowB.setIdle()
  when(io.flowA.fire){
    flowB.push(io.flowA.payload)
  }

  val cond = RegInit(False).setWhen(io.flowA.fire)
  val regFlow = RegFlow(io.flowA)
  val toReg = io.flowA.toReg()

  val fork = io.flowA.combStage()
  val cloneable = io.flowA.clone()

  val validFlow = ValidFlow(Bits(10 bits))

  val m2sFlow = io.flowA.m2sPipe  //no hold payload
  val translatewith = io.flowA.translateWith(Bits(10 bits))

  val takewhen = io.flowA.takeWhen(cond)


}
