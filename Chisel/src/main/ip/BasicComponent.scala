package ip

import chisel3._

//like flow in the spinal
class Channel() extends Bundle{
  //define the bundle like this
  val valid = Bool()
  val data = UInt(32.W)
}
/*
lots of signal don't generate like constant value
*/
class BasicComponent extends Module {
  val io = IO(new Bundle{
    val a = Input(UInt(3.W))
    val b = Input(UInt(3.W))
    val c = Output(UInt(32.W))
  })

  // basic type and constant
  val bits = Wire(Bits(8.W))
  val uint = Wire(UInt(4.W))
  val sint = Wire(SInt(8.W))
  val bool = Wire(Bool())
  val number = WireDefault(10.U(4.W))
  val const = -3.S(4.W)
  val constBinary = "b1111_1111".U
  val constantChar = 'A'.U
  val constBool = true.B
  bits := const(0)
  sint := -3.S
  uint := 0.U
  //the basic combinational logic
  val and = io.a & io.b
  val or = io.a | io.b
  val xor = io.a ^ io.b
  val not = ~io.a
  val add = io.a + io.b
  val sub = io.a - io.b
  val mul = io.a * io.b
  val div = io.a / io.b
  val mod = io.a % io.b
  val neg = -io.a

  //define wire and reg and use the bundle
  val w = Wire(UInt(3.W))

  w := add
  val sign = io.a(2)
  bool := true.B
  val mux = Mux(bool,add,sub)

  val reg = RegInit(0.U(8.W))
  reg := number - 3.U
  val bothReg = RegNext(constBinary,0.U)

  //a simple counter like this
  val cntReg = RegInit(0.U(8.W))
  cntReg := Mux(cntReg === 9.U,0.U,cntReg + 1.U)

  val ch = Wire(new Channel())
  ch.valid := true.B
  ch.data := 0.U
  io.c := add
}

object BasicComponent extends App{
  emitVerilog(new BasicComponent,Array("--target-dir", "rtl"))
}