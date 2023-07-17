package DayliComponent
import spinal.core._
/*
实现一个可以配置的多选器,多选器是一种比较简单组合逻辑电路
*/

case class Multiplexers(selnum:Int,inputnum:Int,inputwidth:Int,outputnum:Int,outputwidth:Int) extends Component {
  noIoPrefix()
  val io = new Bundle{
    val sels =  in Vec(Bool(),selnum)
    val inputs =  in Vec(Bits(inputwidth bits),inputnum)
    val outputs = out Vec(Bits(outputwidth bits),1)
  }
  io.outputs.map(_ := 1)
  for(i <- 0 to selnum){
    if(selnum != i) {
      when(io.sels(i)){
        io.outputs(0) := io.inputs(i)
      }.otherwise{
        io.outputs(0) := io.inputs(i+1)
      }
    }
  }
}

case class Muxlib() extends Component{
  noIoPrefix()
  val io = new Bundle{
    val src1 = in UInt(2 bits)
    val src2 = in UInt(2 bits)
    val cond = in Bool()
    val bitwise = in UInt(2 bits)
    val output = out UInt(2 bits)
  }
  //Example 1
  val output1 = io.bitwise.mux(
    0 -> (io.src1 + io.src2),
    1 -> (io.src1 - io.src2),
    default -> (io.src1)
  )
  //Rxample 2
  val output2 = Mux(io.cond,io.src1,io.src2)
  io.output := output2
}

case class Mux256to1v() extends Component{
  noIoPrefix()
  val io = new Bundle{
    val in1= in Bits(1024 bits)
    val sel = in Bits(8 bits)
    val output = out Bits(4 bits)
  }
  io.output := io.in1(4 * (io.sel.asUInt),4 bits) // use the offset to do
}

object Multiplexers extends App{
  //SpinalVerilog(Multiplexers(1,2,99,1,99))
  SpinalVerilog(Mux256to1v())
}