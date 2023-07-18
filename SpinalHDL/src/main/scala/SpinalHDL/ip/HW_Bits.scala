package SpinalHDL.ip
import spinal.core._
import spinal.lib._
//some details about Bits (like high)

class HW_Bits extends Component {
  val io = new Bundle{
    val value = in Bits(10 bits)
  }
  val RVC = true
  val FETCH_DATA_WIDTH = 64
  val slice_one = 2 downto 2
  val slice_two = 2 downto 1

  val one_high = slice_one.high //return 2
  val two_high = slice_two.high

  //what's high(MSB index)
  val value_one_high = io.value(one_high)
  val value_two_high = io.value(two_high)
  def SLICE_WIDTH = if (RVC) 16 else 32
  def SLICE_BYTES = if (RVC) 2 else 4
  def SLICE_COUNT = FETCH_DATA_WIDTH / SLICE_WIDTH
  def SLICE_RANGE_LOW = if (RVC) 1 else 2
  val sliceRange = (SLICE_RANGE_LOW + log2Up(SLICE_COUNT) - 1 downto SLICE_RANGE_LOW)

  val slice_high = sliceRange.high
  val value_slice_high = io.value(slice_high)
}
