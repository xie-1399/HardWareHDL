package SpinalHDL.lib.FlowSource

import spinal.core._
import spinal.lib._

//the lib flow usage

class UseFlow extends Component {
  val io = new Bundle{
    val flowA = slave Flow(Bits(10 bits))
  }

  //if set with Bundle ->
  val flowC = Flow(
    new Bundle{
      val address = Bits(10 bits)
    }
  )

  //the common methods about the flow
  val flowB = io.flowA.clone()

  //only when flowA valid -> B get the data and then don't care about
  flowB.setIdle()
  when(io.flowA.fire){
    flowB.push(io.flowA.payload)
  }

  //maybe should also clear when
  val cond = RegInit(False).setWhen(io.flowA.fire)

  val regFlow = RegFlow(io.flowA) //set all as reg
  val toReg = io.flowA.toReg() //just payload

  val fork = io.flowA.combStage()
  val cloneable = flowC.clone() //just a clone type

  val validFlow = ValidFlow(Bits(10 bits)) //always valid

  val m2sFlow = io.flowA.m2sPipe  //no hold payload
  val translatewith = io.flowA.translateWith(Bits(10 bits))

  val takewhen = io.flowA.takeWhen(cond)

  val noData = new Flow(NoData)
  noData.valid := io.flowA.valid


}
