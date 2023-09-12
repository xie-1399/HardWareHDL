package ipSim

import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import ip._
import chiseltest._
import scala.util.Random

class UsingSequentialSim extends AnyFlatSpec with ChiselScalatestTester {
  "sequential test" should "pass" in{
    test(new UsingSequential).withAnnotations(Seq(WriteVcdAnnotation)){
      dut =>
        for(idx <- 0 until 100){
          val rs1 = Random.nextInt(6)
          val rs2 = Random.nextInt(6) > 3
          println(rs1,rs2)
          dut.io.enable.poke(rs2)
          dut.io.value.poke(rs1)
          dut.clock.step(1)
          println(dut.io.value.peekInt(),dut.io.enable.peekBoolean())
        }
    }
  }

}
