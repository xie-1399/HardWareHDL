package SpinalHDL.ip
import spinal.core._

//use Vec in the Component

class HW_Vec extends Component {
  val io = new Bundle{
    val start = in UInt(3 bits)
    val sel = in UInt(3 bits)
    val write = out Bool()
  }
  //if no use Reg, can not be initial with value and will be change quickly
  val buffer = Vec(Reg(UInt(3 bits)) init(0),8)

  when(io.write){
    buffer(io.sel) := io.sel
  }
  io.write := io.sel === io.start

}
