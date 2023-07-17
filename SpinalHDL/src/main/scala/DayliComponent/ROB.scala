package DayliComponent
//ROB看上去是一个比较复杂的表，里面有很多需要注意的关键信息
import spinal.core._
import spinal.lib._
import spinal.lib.pipeline.Stageable

import scala.collection.mutable
import scala.collection.mutable._

object ROBParameter{
  val robSize = 64
  def COLS = 2
  def LINES = robSize / COLS

}

//object ROB extends AreaObject{
//  def COLS = Frontend.DISPATCH_COUNT
//  def LINES = SIZE/COLS
//  val SIZE = NaxParameter[Int]
//  def ID_WIDTH = log2Up(SIZE.get)
//  val ID = Stageable(UInt(ID_WIDTH bits))
//  val MSB = Stageable(UInt(1 bits)) //Extra ID bit which allow to figure out ordering by a substractor
//  def lineRange = ID_WIDTH-1 downto log2Up(COLS)
//}


class ReorderBuffer(var robSize:Int,
                  var completionWithReg:Boolean = false) extends Component {
  case class RobCompletion(idWidth: Int) extends Bundle {
    //How to judge one instruction finish
    val id = UInt(idWidth bits)
  }

  case class Write(key: Stageable[Data], size: Int, value: mutable.Seq[Data], robId: UInt, enable: Bool)

  case class ReadAsync(key: Stageable[Data], size: Int, robId: UInt, skipFactor: Int, skipOffset: Int, rsp: Vec[Data])

  val writes = ArrayBuffer[Write]()
  val readAsyncs = ArrayBuffer[ReadAsync]()

  def write[T <: Data](key: Stageable[T], size: Int, value: mutable.Seq[T], robId: UInt, enable: Bool) = {
    writes += Write(key.asInstanceOf[Stageable[Data]], size, value.asInstanceOf[mutable.Seq[Data]], robId = robId, enable = enable)
  }

  //Todo about the skipfactor and skipoffset
  def readAsync[T <: Data](key: Stageable[T], size: Int, robId: UInt, skipFactor: Int = 1, skipOffset: Int = 0): Vec[T] = {
    val r = ReadAsync(key.asInstanceOf[Stageable[Data]], size, robId, skipFactor, skipOffset, Vec.fill(size)(key()))
    readAsyncs += r
    r.rsp.asInstanceOf[Vec[T]]
  }

  case class Completion(bus:Flow[RobCompletion])
  val completions = ArrayBuffer[Completion]()

  val idwidth = log2Up(robSize)

  val logic = new Area {
    val completionReg = completionWithReg generate new Area {
      //override type RefOwnerType = this.type

    }
  }

  }

object ROB extends App{
  SpinalVerilog(new ReorderBuffer(ROBParameter.robSize))
}
