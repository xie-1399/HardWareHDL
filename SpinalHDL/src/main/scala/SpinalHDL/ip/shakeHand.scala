package SpinalHDL.ip

import spinal.core._
import spinal.lib._

case class HandShake(payloadWidth: Int) extends Bundle with IMasterSlave {
  val valid   = Bool()
  val ready   = Bool()
  val payload = Bits(payloadWidth bits)

  //You have to implement this asMaster function.
  //This function should set the direction of each signals from an master point of view
  override def asMaster(): Unit = {
    out(valid,payload)
    in(ready)
  }
  val encoding = SpinalEnumEncoding("dynamicEncoding",_*2 + 1)
  object MyEnumDynamic extends SpinalEnum(encoding){
    val e0,e1,e2,e3 = newElement()
  }
}



case class shakeTest() extends Component{
  val io = new Bundle {
    val shake1 = slave(HandShake(10)) // learn about slave or master
  }
  io.shake1.ready := True
  val itMatch = io.shake1.payload === M"0000----11"
}

object shake extends App {
  SpinalVerilog(new shakeTest())
}