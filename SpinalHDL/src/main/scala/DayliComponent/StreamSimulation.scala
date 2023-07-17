package DayliComponent

import spinal.core._
import spinal.core.internals.Operator
import spinal.lib._
import spinal.lib.bus.amba4.axi._

object MemBus{
  def getAxi4Config(config: MemBusConfig,idWidth:BitCount) = Axi4Config(
    addressWidth = config.addressWidth,
    dataWidth = config.dataWidth,
    useId = true,
    idWidth = idWidth.value, // 3 bits .value
    useLast = true,
    useStrb = true
  )
}

case class MemBusConfig(addressWidth:Int,dataWidth:Int,readWrite:Boolean){
  //地址位宽，数据位宽以及读/写
  def BytetowordAddress(ba:UInt):UInt = ba >> log2Up(dataWidth / 8) // or ba(dataWidth - 1 downto log2Up(dataWidth / 8 ))
  //64 位 -> 1字 = 8 字节，这个主要和寄存器有关，64位寄存器代表字长为64，即一次处理的数据为64位
  def word2ByteAddress(wa:UInt): UInt = wa << log2Up(dataWidth / 8)
}

//Bit Count like 32 bits
case class MemBusCmd(config: MemBusConfig,idWidth:BitCount) extends Bundle{
  val address = UInt(config.addressWidth bits)
  val id = UInt(idWidth)
  val write = if(config.readWrite) Bool() else null
  val wdata = if(config.readWrite) UInt(config.dataWidth bits) else null
  val wmask = if(config.readWrite) Bits(config.dataWidth / 8 bits) else null
}

case class MemBusRsp(config: MemBusConfig,idWidth:BitCount) extends Bundle{
  val rdata = UInt(config.dataWidth bits)
  val id = UInt(idWidth)
}

case class MemBus(config:MemBusConfig,val idWidth:BitCount) extends Bundle with IMasterSlave {
  val cmd = Stream(MemBusCmd(config,idWidth))
  val rsp = Stream(MemBusRsp(config,idWidth))
  override def asMaster(): Unit = {
    master(cmd)
    slave(rsp)
  }

  //For instruction just read , cmd -> axi4 cmd
  def toAxi4ReadOnly(): Axi4ReadOnly = { //master ar slave r
    assert(isMasterInterface) //just as master interface

    val axi4Config = MemBus.getAxi4Config(config, idWidth)
    val axi4Bus = Axi4ReadOnly(axi4Config)

    axi4Bus.readCmd.valid := cmd.valid
    axi4Bus.readCmd.addr := cmd.address
    axi4Bus.readCmd.id := cmd.id
    cmd.ready := axi4Bus.readCmd.ready

    rsp.valid := axi4Bus.readRsp.valid
    rsp.id := axi4Bus.readRsp.id
    rsp.rdata := axi4Bus.readRsp.data.asUInt.resized
    axi4Bus.readRsp.ready := rsp.ready
    axi4Bus
  }
}

/*
通过Flow往一块RAM里面写数据，cmd并通过Stream类型去处理时序，但是这个地方的时序需要非常的注意
*/

case class MemoryWrite() extends Bundle{
  val address = UInt(8 bits)
  val data = Bits(32 bits)
}

class StreamSimulation extends Component {
  val io = new Bundle{
    val memWrite = slave(Flow(MemoryWrite()))
    val cmdA = slave(Stream(UInt(8 bits)))
    val cmdB = slave(Stream(Bits((32 bits))))
    val rsp = master(Stream(Bits(32 bits))) //in rsp.ready
  }
  val ram = Mem(Bits(32 bits),256) // 256 * 32 bits
  ram.write(io.memWrite.address,io.memWrite.data,enable = io.memWrite.valid)
  val memReadData = ram.readSync(io.cmdA.payload, enable = io.cmdA.fire)
  val readingCmdAMax:Int = 7
  val readingCmdBMax:Int = 7

  val readcmdas = CounterUpDown(
    stateCount = readingCmdAMax + 1,
    incWhen = io.cmdA.fire,
    decWhen = io.rsp.fire
  )
  val readcmdbs = CounterUpDown(
    stateCount = readingCmdBMax + 1,
    incWhen = io.cmdB.fire,
    decWhen = io.rsp.fire
  )
  val dataB = RegNextWhen(io.cmdB.payload,io.cmdB.fire)
  io.cmdA.ready := readcmdas === 0
  io.cmdB.ready := readcmdbs === 0
  io.rsp.valid := readcmdas =/= 0 && readcmdbs =/=0
  io.rsp.payload := 0
  when(io.rsp.fire){
    io.rsp.payload := memReadData ^ dataB
  }


  //Todo use the Stream API useful
//  val outA = ram.streamReadSync(io.cmdA)
//  val joinAB = StreamJoin(outA,io.cmdB)
//  io.rsp << joinAB.translateWith(outA.payload ^ io.cmdB.payload)
}
