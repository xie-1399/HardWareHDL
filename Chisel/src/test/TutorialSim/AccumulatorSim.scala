package TutorialSim

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Tutorial._
import scala.util.Random

class AccumulatorSim extends AnyFlatSpec with ChiselScalatestTester {
  "accumulator test" should "pass" in {
    test(new Accumulator).withAnnotations(Seq(WriteVcdAnnotation)){
      dut =>
        /*
        the sequence logic is very diff with spinal hdl
        */
        var mark = 0
        for(idx <- 0 until 100){
          val in = Random.nextInt(2)
          dut.io.in.poke(in)
          dut.clock.step(1)
          if(in == 1) mark += 1
          dut.io.output.expect(mark)
        }
    }
  }
}
