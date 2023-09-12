package SpinalHDL.ip
import spinal.core._

class Accumulator extends Component {
  val io = new Bundle{
    val cond = in UInt (1 bits)
    val output = out UInt(8 bits)
  }

  val value = Reg(UInt(8 bits)) init(0)
  value := io.cond + value
  io.output := value
}
