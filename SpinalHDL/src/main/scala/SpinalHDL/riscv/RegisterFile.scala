package SpinalHDL.riscv

//是否需要多个端口的读写？（如果一次性执行多条指令就需要多个端口的读写）
import spinal.core._
import spinal.lib._

case class RegisterFileReadIo()(implicit config: Config) extends Bundle with IMasterSlave{
  val rs1 = UInt(5 bits)
  val rs2 = UInt(5 bits)
  val rs1Data = UInt(config.xlen bits)
  val rs2Data = UInt(config.xlen bits)

  override def asMaster(): Unit = {
    in(rs1,rs2)
    out(rs1Data,rs2Data)
  }
}

case class RegisterFileWriteIo()(implicit config: Config) extends Bundle with IMasterSlave{
  val rd = UInt(5 bits)
  val rdData = UInt(config.xlen bits)
  val write = Bool()
  override def asMaster(): Unit = {
    in(rd,rdData,write)
  }
}


class RegisterFile()(implicit config: Config) extends Component {
  //regfile group
  private val registerNames = Seq("zero", "ra", "sp", "gp", "tp", "t0", "t1", "t2", "s0_fp", "s1",
    "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "s2", "s3", "s4", "s5", "s6", "s7", "s8",
    "s9", "s10", "s11", "t3", "t4", "t5", "t6")

  val readIo = master(RegisterFileReadIo())
  val writeIo = master(RegisterFileWriteIo())
  val regs = Mem(UInt(config.xlen bits),config.numRegs)

  //在仿真时能够显示对应寄存器的值

  for( i <- 0 until config.numRegs){
    val regWire = UInt(config.xlen bits)
    val regName = registerNames(i)
    regWire.setName(s"x${i}_${regName}")
    regWire := regs.readAsync(U(i).resized,writeFirst) //Todo let us use U(i),异步直接读出
  }

  def ReadReg(addr:UInt) = {
    addr.mux(
      0 -> U(0,config.xlen bits),  //use a mux to keep x0 equals 0
      default -> regs.readAsync(addr,writeFirst)
    )
  }

  readIo.rs1Data := ReadReg(readIo.rs1)
  readIo.rs2Data := ReadReg(readIo.rs2)

  //what is dirrerence between the two ways（is same）
  regs.write(writeIo.rd,writeIo.rdData,enable = writeIo.write && writeIo.rd =/= 0)

//  when(writeIo.write && writeIo.rd =/= 0){
//    regs(writeIo.rd) := writeIo.rdData
//  }

}
