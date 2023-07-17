package DayliComponent.bus.myAPB

//leran about how to change axi bus port to the apb port
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba4.axi._
import spinal.lib.bus.amba3.apb._
import spinal.lib.bus.amba4.axilite.{AxiLite4, AxiLite4Config}

object busconfig{
  val axiConfig = Axi4Config(
    addressWidth = 32,
    dataWidth = 32,
    idWidth = 4,
    useProt = false,
    useQos = false,
    useLock = false,
    useCache = false,
    useRegion = false
  )
  val apbConfig = Apb3Config(
    addressWidth = 32,
    dataWidth = 32,
    selWidth = 1
  )

  val axilite4Config = AxiLite4Config(
    addressWidth = 32,
    dataWidth = 32
  )

}


//在这个模块里面接收请求的是axi maxter的请求，接出去的是APB的master请求，画一个模块图就比较好区分

class myAxiToApb extends Component {
  val myaxi4 = slave(Axi4(busconfig.axiConfig))
  val myapb = master(Apb3(busconfig.apbConfig))
  val bridge = Axi4SharedToApb3Bridge(
    addressWidth = 32,
    dataWidth = 32,
    idWidth = 4
  )

  myaxi4.toShared() <> bridge.io.axi
  myapb <> bridge.io.apb
}


object myAxiToApb extends App{
  SpinalVerilog(new myAxiToApb)
}