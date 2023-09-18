package TutorialSim

import Tutorial._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.Random

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
        for(idx <- 0 until 100){
          val data = Random.nextInt(128)
          val addr = Random.nextInt(128)
          wr(addr, data)
          rd(addr, data)
        }

    }
  }
}
