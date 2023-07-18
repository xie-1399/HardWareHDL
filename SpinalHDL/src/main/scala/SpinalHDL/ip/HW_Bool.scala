package SpinalHDL.ip
import spinal.core._
import spinal.lib._
//some more pro about Bool like mux and ?

class HW_Bool extends Component {
  val io = new Bundle{
    val start = in Bool()
    val sel = in Bool()
    val end = out Bool()
  }
  //are they equal? yes
  io.end := Mux(io.sel,io.start,False)
  val res = io.sel ? io.start | False
}
