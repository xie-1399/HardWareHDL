package DayliComponent.Function
import spinal.core._
import spinal.lib._


class function extends Component {
  val io = new Bundle{
    val cmd = slave Flow(Bits(8 bits))
    val valueA = out(Bits(8 bits))
    val valueB = out(Bits(32 bits))
    val valueC = out(Bits(48 bits))
  }

  def patternDetector(str:String) = new Area {
    //detect the cmd value type
    val hit = False
    val counter = Counter(str.length)

    when(io.cmd.valid){
      //use map to function the all set
      when((0 until str.length).map(x => counter.value === x && io.cmd.payload === str(x)).orR){
        counter.increment()
      }.otherwise{
        counter.clear()
      }
    }
    when(counter.willOverflow){
      hit := True
      counter.clear()
    }

  }

  //should look the clock cycle
  def valueLoader(start:Bool,that:Data) = new Area {
    //need to know the num of data
    require(widthOf(that) % widthOf(io.cmd.payload) == 0)   //learn use require
    val cycles =  that.getBitsWidth / io.cmd.payload.getBitsWidth
    val counter = Counter(cycles)
    val data = Reg(Bits(that.getBitsWidth bits))
    val hit = RegInit(False).setWhen(start)
    when(hit && io.cmd.valid){
      data.subdivideIn(io.cmd.payload.getBitsWidth bits)(counter) := io.cmd.payload           // bits slice
      hit.clearWhen(counter.willOverflowIfInc)
      counter.increment()
    }
    that := data
  }

  val setA = patternDetector("setValueA")
  val loadA = valueLoader(setA.hit, io.valueA)

  val setB = patternDetector("setValueB")
  val loadB = valueLoader(setB.hit, io.valueB)

  val setC = patternDetector("setValueC")
  val loadC = valueLoader(setC.hit, io.valueC)
}

