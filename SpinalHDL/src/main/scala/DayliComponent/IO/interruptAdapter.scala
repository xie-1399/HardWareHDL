package DayliComponent.IO
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba4.axi.Axi4
import DayliComponent.bus.myAPB._
import spinal.lib.bus.amba4.axilite.AxiLite4Utils.Axi4Rich
import spinal.lib.bus.amba4.axilite.AxiLite4

class interruptAdapter extends Component {
  val io = new Bundle{
    val axicmd = slave(Axi4(busconfig.axiConfig))
    val axilite4cmd = master(AxiLite4(busconfig.axilite4Config))
  }
  io.axicmd.toLite() >> io.axilite4cmd
}
