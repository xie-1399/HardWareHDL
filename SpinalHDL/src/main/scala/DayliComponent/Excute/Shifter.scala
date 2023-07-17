package DayliComponent.Excute
//about how to implement a shift ALU
import spinal.core._
import spinal.lib._

//simple shifter(logic)  arithmetic shifter and barrel shifter

case class Shifter(kind:String,inputWidth:Int,shifternum:Int = 1) extends Component {
  val io  = new Bundle{
    val inputdata = in Bits(inputWidth bits)
    val outputdata = out Bits(inputWidth bits)
    val direction = in Bool()
  }
  val data = Reg(Bits(inputWidth bits)) init(0)
  //True -> right
  val logic = new Area {
    val logicshift = (kind == "logic") generate new Area {
      data := io.inputdata |<< shifternum
      when(io.direction){
        data := io.inputdata |>> shifternum
      }
    }
    val arithmeticshift = (kind == "arithmetic") generate new Area {
      data := signshifter(io.inputdata,shifternum,io.direction)
    }
    val barrelshift = (kind == "barrel") generate new Area {
      data := io.inputdata.rotateLeft(shifternum)
      when(io.direction){
        data := io.inputdata.rotateRight(shifternum)
      }
    }
  }
  io.outputdata := data

  def signshifter[T <: BitVector](data: Bits, shiftnum:Int,direction: Bool): Bits = { //T: UInt SInt Bits
    val width = data.getBitsWidth
    val sign = data(width -1)
    val lfill = B(shiftnum bits, default -> sign)
    val rfill = B(shiftnum bits, default -> False)
    val sign_data = Bits(width bits)
    sign_data := data(width -1 - shifternum downto 0) ## rfill
    when(direction){
      sign_data := lfill ## data(width - 1 downto shiftnum)
    }
    sign_data
  }
}








