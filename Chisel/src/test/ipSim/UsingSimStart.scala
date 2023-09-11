package ipSim

import chiseltest._
import ip.BasicComponent
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Random

  /*
  simple way to show how to test and sim about component
  */

class UsingscalaTest extends AnyFlatSpec with Matchers{
  "Inter" should "add" in {
    val i = 2
    val j = 3
    i + j should be (5)
  }
}

  /*
  think about how to use the chisel test with the sim
  and know about the generate wave type
 */

class UsingSimStart extends AnyFlatSpec with ChiselScalatestTester {
  "dut" should "pass" in{
      test(new BasicComponent).withAnnotations(Seq(WriteVcdAnnotation)){
        dut =>
          for(idx <- 0 until 100){
            val rs1 = Random.nextInt(6)
            val rs2 = Random.nextInt(6)
            dut.io.a.poke(rs1)
            dut.io.b.poke(rs2)
            dut.io.ctrl.poke(2)
            dut.clock.step()
            println(rs1,rs2)
            println("result is :" + dut.io.c.peekInt())
            assert(dut.io.c.peekInt() == rs1 + rs2)
            if(rs2 != 0){
              assert(dut.io.out.peekInt() == rs1 / rs2)
            }
          }
      }
  }

}
