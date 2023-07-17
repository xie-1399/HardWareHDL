package DayliComponent.bus.myAXI
import spinal.core._
import spinal.lib.bus.amba4.axi._
import spinal.lib._

//maybe can config
case class Buscmd() extends Bundle{
  val address = UInt(32 bits)
  val id = UInt(4 bits)
  val write = Bool()
  val wdata = UInt(32 bits)
  val wmask = Bits(4 bits)
}

case class Busrsp() extends Bundle{
  val rdata = UInt(32 bits)
  val id = UInt(4 bits)
}

case class MemBus() extends Bundle with IMasterSlave {
  val cmd = Stream(Buscmd())
  val rsp = Flow(Busrsp())
  override def asMaster(): Unit = {
    master(cmd)
    slave(rsp)
  }

  def toAxi4(): Axi4 = {
    assert(!isMasterInterface)
    val axi4config = Axi4Config(
      addressWidth = 32,
      dataWidth = 32,
      idWidth = 4,
      useCache = false,
      useProt = false,
      useLock = false,
      useQos = false,
      useRegion = false,
      readIssuingCapability = 4,
      writeIssuingCapability = 4,
      combinedIssuingCapability = 8,
      readDataReorderingDepth = 4
    )
    val axi4Bus = Axi4(axi4config)
    //burst读
    axi4Bus.ar.valid := Mux(cmd.write,False,cmd.valid)
    axi4Bus.ar.payload.addr := cmd.address
    axi4Bus.ar.id := cmd.id
    axi4Bus.ar.size := U(2,3 bits)
    axi4Bus.ar.len := U(7,8 bits) //7 + 1
    axi4Bus.ar.burst := B"01"
    //burst写
    axi4Bus.aw.valid := Mux(cmd.write, cmd.valid, False)
    axi4Bus.aw.payload.addr := cmd.address
    axi4Bus.aw.id := cmd.id
    axi4Bus.aw.size := U(2,3 bits)
    axi4Bus.aw.len := 0
    axi4Bus.aw.burst := B"00"

    axi4Bus.w.valid := cmd.write && cmd.valid
    axi4Bus.w.data := cmd.wdata.asBits
    axi4Bus.w.strb := cmd.wmask

    axi4Bus.w.last := True
    axi4Bus.b.ready := True
    axi4Bus.r.ready := True

    rsp.valid := axi4Bus.readRsp.valid
    rsp.id := axi4Bus.readRsp.id
    rsp.payload.rdata := axi4Bus.readRsp.payload.data.asUInt
    cmd.ready := axi4Bus.ar.ready || axi4Bus.aw.ready
    axi4Bus
  }
}


class Outstanding(axi4Config: Axi4Config) extends Component {
  val io = new Bundle{
    val request = slave(MemBus())
    val axi = master(Axi4(axi4Config))
  }
  //get the trans id
  val dbus = io.request.toAxi4()
  dbus >> io.axi
  val readid = RegNext(io.axi.ar.id)
  val writeid = RegNext(io.axi.aw.id)
}


object CmdToAxi extends App{
  val axiConfig = Axi4Config(
    addressWidth = 32,
    dataWidth = 32,
    idWidth = 4,
    useCache = false,
    useProt = false,
    useLock = false,
    useQos = false,
    useRegion = false,
    readIssuingCapability = 4,
    writeIssuingCapability = 4,
    combinedIssuingCapability = 8,
    readDataReorderingDepth = 4
  )
  SpinalVerilog(new Outstanding(axiConfig))
}