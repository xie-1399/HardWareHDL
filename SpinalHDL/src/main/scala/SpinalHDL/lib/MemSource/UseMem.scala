package SpinalHDL.lib.MemSource
import spinal.core._
import spinal.lib._

//use the flow / stream to control the Memory Port

class UseMem extends Component {
  val io = new Bundle{
    val streamA = slave Stream(UInt(5 bits))
    val flowA = slave Flow(UInt(5 bits))
    val linked = in UInt (5 bits)
  }

  val memory = Mem(Bits(10 bits),32)

  val StreamSync = memory.streamReadSync(io.streamA)
  val StreamSynclinked = memory.streamReadSync(io.streamA,linkedData = io.linked)

  val FlowSync = memory.flowReadSync(io.flowA)

}
