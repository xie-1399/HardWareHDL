package TutorialSim

import Tutorial._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.Random

// Todo the test is not very strict
class MemoSim extends AnyFlatSpec with ChiselScalatestTester {
  "Memo test" should "pass" in {
    test(new Memo).withAnnotations(Seq(WriteVcdAnnotation)) {
      dut =>
        def rd(addr: Int, data: Int) = {
          dut.io.ren.poke(1)
          dut.io.rdAddr.poke(addr)
          dut.clock.step(1)
          dut.io.rdData.expect(data)
        }

        def wr(addr: Int, data: Int) = {
          dut.io.wen.poke( 1)
          dut.io.wrAddr.poke(addr)
          dut.io.wrData.poke(data)
          dut.clock.step(1)
        }

        wr(0, 1)
        rd(0, 1)
        wr(9, 11)
        rd(9, 11)
    }
  }

}
