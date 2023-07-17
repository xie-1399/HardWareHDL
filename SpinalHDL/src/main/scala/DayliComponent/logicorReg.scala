package DayliComponent
import spinal.core._
import spinal.sim._
import org.scalatest.FunSuite

/*
这个Component非常的简单，主要是为了成为是寄存器以及直接用val的区别在哪里
*/

class logicorReg extends Component {
  noIoPrefix()
  val io = new Bundle{
    val a = in(Bool())
    val b = in(UInt(3 bits))
    val output = out(UInt(3 bits))
    val valid = in Bool()
  }

  val reg1 = Reg(UInt(3 bits))init(0)
  val reg2 = io.b //直接替换成io.b了
  val reg3 = Reg(Bool()).setWhen(io.a).clearWhen(!io.a) //注意这里的时序
  //use initial random value
  val reg4 = Reg(UInt(4 bits)) randBoot()

  val validReg = RegInit(False)
  when(io.valid){
    validReg := True
  }

  reg1 := io.b //使用一个寄存器可以延迟一拍赋值
  io.output := 0
  when(io.a){
    io.output := reg2 + reg2
  }
}

object logicorReg extends App{
  SpinalVerilog(new logicorReg)
}