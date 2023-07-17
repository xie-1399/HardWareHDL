package SpinalHDL.riscv

/*
关于总线请求的相关实现,Maybe you should know about the Stream and the MasterSlave
*/

import spinal.core._
import spinal.core.internals.Operator
import spinal.lib._
import spinal.lib.bus.amba4.axi._

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

//class MemBusControl(bus:MemBus)(implicit config:Config) extends Area{
//}

class IBusControl(bus:MemBus,ibusLatency:Int)(implicit config: Config) extends Area {
  assert(!bus.config.readWrite)
  assert(ibusLatency > 0)

  case class Cmd() extends Bundle{
    val address = UInt(bus.config.addressWidth bits)
  }

  case class Rsp() extends Bundle {
    val address = UInt(bus.config.addressWidth bits)
    val ir = UInt(bus.config.dataWidth bits)
  }

  def read(address:UInt):(Bool,UInt) = {
    val valid = False
    val ir = Reg(UInt(config.xlen bits)) init(0)
    ir := 0
    val rsp = Stream(Rsp())
    when(rsp.valid){
      when(rsp.payload.address === address){
        valid := True
        ir := rsp.payload.ir
      }
    }
    (valid,ir)
  }
}

