package SpinalHDL.riscv.Module

//just a Component to decode the RV IR
import spinal.core._
import spinal.lib._
import SpinalHDL.riscv._

//根据指令的类型取得对应的立即数,返回的为UInt类型
case class ImmediateDecoder(ir : Bits){
  private def signExtend(data:Bits):UInt = {
    Utils.signExtend(data, 32).asUInt
  }
  def i = signExtend(ir(31 downto 19))
  def s = signExtend(ir(31 downto 25) ## ir(11 downto 7))
  val b = signExtend(ir(31) ## ir(7) ## ir(30 downto 25) ## ir(11 downto 8) ## False)
  def u = (ir(31 downto 12) << 12).asUInt
  def j = signExtend(
    ir(31) ## ir(19 downto 12) ## ir(20) ## ir(30 downto 25) ## ir(24 downto 21) ## False
  )
}


class Decode extends Component {
  val config = MyCPUConfig.myconfig

  val io  = new Bundle{
    val decode_pc_in = in(ModuleStardData(config).PC.dataType)
    val decode_nextpc_in = in(ModuleStardData(config).NeXT_PC.dataType)
    val decode_ir = in(ModuleStardData(config).IR.dataType)

    val decode_rs1 = out(ModuleStardData(config).RS1.dataType)
    val decode_rs2 = out(ModuleStardData(config).RS2.dataType)
    val decode_rd = out(ModuleStardData(config).RD.dataType)

    val decode_pc_out = out(ModuleStardData(config).PC.dataType)
    val decode_nextpc_out = out(ModuleStardData(config).NeXT_PC.dataType)
  }

  val ir = io.decode_ir

  io.decode_rs1 := ir(19 downto 15)
  io.decode_rs2 := ir(24 downto 20)
  io.decode_rd := ir(11 downto 7)
  //分析ir -> 得到对应的输出
//  switch(ir){
//    ???
//  }

  io.decode_nextpc_out := RegNext(io.decode_nextpc_in)
  io.decode_pc_out := RegNext(io.decode_pc_in)


}



object Decode extends App{
  SpinalVerilog(new Decode())
}