package TutorialSim

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Tutorial.Adder

import scala.util.Random

class AdderSim extends AnyFlatSpec with ChiselScalatestTester {
  "Adder test" should "pass" in {
    test(new Adder(10)){
      dut =>
      for(idx <- 0 until 100){
        val in0 = Random.nextInt(1 << dut.w)
        val in1 = Random.nextInt(1 << dut.w)
        dut.io.in0.poke(in0)
        dut.io.in1.poke(in1)
        dut.clock.step()
        dut.io.out.expect((in0 + in1) & ((1 << dut.w)-1))
     }
    }
  }

}
