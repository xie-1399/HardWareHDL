package TutorialSim

import Tutorial.Counter
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import scala.util.Random

class CounterSim extends AnyFlatSpec with ChiselScalatestTester {
  "Counter test" should "pass" in {
    test(new Counter).withAnnotations(Seq(WriteVcdAnnotation)) {
      dut =>
      val maxInt = 16
      var curCnt = 0

      def intWrapAround(n: Int, max: Int) =
        if (n > max) 0 else n

      dut.io.inc.poke(0)
      dut.io.amt.poke(0)

      // let it spin for a bit
      for (i <- 0 until 5) {
        dut.clock.step(1)
      }

      for(idx <- 0 until 100){
        val inc = Random.nextBoolean()
        val amt = Random.nextInt(maxInt)
        dut.io.inc.poke(if (inc) 1 else 0)
        dut.io.amt.poke(amt)
        dut.clock.step(1)
        curCnt = if (inc) intWrapAround(curCnt + amt, 255) else curCnt
        dut.io.tot.expect(curCnt)
      }
    }
  }

}
