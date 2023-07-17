package DayliComponent.Privileged
import spinal.core._
import spinal.core.fiber.Handle
import spinal.lib._
import scala.collection.mutable._

class CsrRamAllocation(val entries : Int){
  var at = -1
  var addressWidth = -1
  def getAddress(offset:UInt):UInt = {
    U(at,addressWidth bits) | offset //why use |
  }
  def getAddress() = U(at,addressWidth bits)

  val entriesLog2 = 1 << log2Up(entries)
}

case class CsrRamRead(addressWidth : Int, dataWidth : Int, priority : Int) extends Bundle{
  val valid,ready = Bool()
  val address = UInt(addressWidth bits)
  val data = Bits(dataWidth bits)
  def fire = valid && ready
}

case class CsrRamWrite(addressWidth : Int, dataWidth : Int, priority : Int) extends Bundle{
  val valid, ready = Bool()
  val address = UInt(addressWidth bits)
  val data = Bits(dataWidth bits)

  def fire = valid && ready
}

class CsrRam extends Component {
  val allocation = ArrayBuffer[CsrRamAllocation]()
  val reads = ArrayBuffer[Handle[CsrRamRead]]()
  val writes = ArrayBuffer[Handle[CsrRamWrite]]()


}
