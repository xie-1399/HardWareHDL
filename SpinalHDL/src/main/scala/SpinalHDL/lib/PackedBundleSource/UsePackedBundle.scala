package SpinalHDL.lib.PackedBundleSource

//use the packing
import spinal.lib._
import spinal.core._

class UsePackedBundle extends Component {
  // s group of bundle with pack bits

  val regWord = new PackedBundle{
    val init = Bool().packFrom(2)
    val stop = Bool()
    val result = Bits(16 bits).packTo(31)
  }

  val wordPacked = new PackedWordBundle(8 bits) {
    val aNumber = UInt(8 bits)
    val bNumber = UInt(8 bits).pack(0 to 7)
    val large = Bits(18 bits)
    val flag = Bool()
  }

  val io = new Bundle {
    val words = in Bits (32 bits)
  }

}
