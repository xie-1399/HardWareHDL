package TutorialSim

import Tutorial.DynamicMemorySearch
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.Random

class DynamicMemorySearchSim extends AnyFlatSpec with ChiselScalatestTester {
  "dynamic search sim" should "pass" in {
    test(new DynamicMemorySearch(n = 16,w = 8)).withAnnotations(Seq(WriteVcdAnnotation)) {
      dut =>
        val list = Array.fill(dut.n) {0}
        /* Initialize the memory */
        for (k <- 0 until dut.n) {
          dut.io.en.poke(0)
          dut.io.isWr.poke(1)
          dut.io.wrAddr.poke(k)
          dut.io.data.poke(0)
          dut.clock.step(1)
        }

        for (k <- 0 until 16) {
          // WRITE A WORD
          dut.io.en.poke(0)
          dut.io.isWr.poke(1)
          val wrAddr = Random.nextInt(dut.n - 1)
          val data = Random.nextInt((1 << dut.w) - 1) + 1 // can't be 0
          dut.io.wrAddr.poke(wrAddr)
          dut.io.data.poke(data)
          dut.clock.step(1)
          list(wrAddr) = data
          // SETUP SEARCH
          val target = if (k > 12) Random.nextInt(1 << dut.w) else data
          dut.io.isWr.poke(0)
          dut.io.data.poke(target)
          dut.io.en.poke(1)
          dut.clock.step(1)
          do {
            dut.io.en.poke(0)
            dut.clock.step(1)
          } while (dut.io.done.peekInt() == BigInt(0))
          val addr = dut.io.target.peekInt().toInt
          if (list contains target)
            assert(list(addr) == target, "LOOKING FOR " + target + " FOUND " + addr)
          else
            assert(addr == (list.length - 1), "LOOKING FOR " + target + " FOUND " + addr)
        }

    }
  }
}
