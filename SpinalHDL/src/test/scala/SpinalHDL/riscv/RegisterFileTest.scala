package SpinalHDL.riscv

import org.scalatest.FunSuite
import spinal.core.sim._
import Common.SpinalSimConfig
import scala.util.Random

//这里需要了解异步读对应的机制

class RegisterFileTest extends FunSuite {
  var compiled: SimCompiled[RegisterFile] = null
  val config:Config = new Config(BaseIsa.RV32I)
  test("compile") {
    compiled = SpinalSimConfig().compile{
      val rf = new RegisterFile()(config)
      rf.writeIo.simPublic()
      rf.readIo.simPublic()
      rf.regs.simPublic()
      rf
    }
  }

  test("testbench") {
    compiled.doSim(seed = 42) {
      dut =>
        dut.clockDomain.forkStimulus(10)
        dut.clockDomain.waitSampling()
        //randomly wirte to the register group

        def write(address: BigInt, data: BigInt): Unit = {
          dut.readIo.rs1.assignBigInt(0)
          dut.readIo.rs2.assignBigInt(0)
          dut.writeIo.rd.assignBigInt(address)
          dut.writeIo.write.assignBigInt(1)
          dut.writeIo.rdData.assignBigInt(data)
          dut.clockDomain.waitSampling()
        }

        def read(rs1:BigInt,rs2:BigInt): Array[BigInt] = {
          val array = new Array[BigInt](2)
          dut.readIo.rs1.assignBigInt(rs1)
          dut.readIo.rs2.assignBigInt(rs2)
          dut.writeIo.rd.assignBigInt(0)
          dut.writeIo.write.assignBigInt(0)
          dut.writeIo.rdData.assignBigInt(0)
          dut.clockDomain.waitSampling()
          array(0) = dut.readIo.rs1Data.toBigInt
          array(1) = dut.readIo.rs2Data.toBigInt
          array
        }

        val ramContent = Array.fill(32)(Random.nextLong() & 0xFFFFFFFFl)
        for(i <- 1 until 32){
          write(i,ramContent(i))
        }
        dut.clockDomain.waitSampling()
        for (i <- 1 until 32) {
          assert(dut.regs.getBigInt(i) == ramContent(i), s"dut.mem($i) wasn't written properly")
        }

        for( i <- 0 until 32){
            val simple = read(i,0)
            val data = simple(0)
            val zero = simple(1)
          if(i == 0){
            assert(data == 0,s"wrong 1")
          }
          else {
            assert(data == ramContent(i),s"wrong 2")
          }
          assert(zero == 0,s"wrong 3")
          dut.clockDomain.waitSampling()
        }

        //assert(array(0) == ramContent(16),s"dut.mem16 wasn't written properly")
        //assert(array(1) == 0,s"dut.mem0 wasn't written properly")
        }

    }
}
