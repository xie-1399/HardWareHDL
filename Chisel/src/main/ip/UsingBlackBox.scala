package ip

import chisel3._
import chisel3.util._
import chisel3.experimental._

  /*
    the ext module always like blackbox format to test and so on
    `BlackBox` modules are generated in the emitted Verilog, whereas
    `ExtModule` modules are not.
  */

class BUFGCE extends BlackBox(Map("SIM_DEVICE" -> "7SERIES")) {
  val io = IO(new Bundle {
    val I = Input(Clock())
    val CE = Input(Bool())
    val O = Output(Clock())
  })
}

class alt_inbuf extends ExtModule(
  Map("io_standard" -> "1.0 V",
    "location" -> "IOBANK_1",
    "enable_bus_hold" -> "on",
    "weak_pull_up_resistor" -> "off",
    "termination" -> "parallel 50 ohms")) {
  val io = IO(new Bundle {
    val i = Input(Bool())
    val o = Output(Bool())
  })
}

 /*
 in-line blackBox and can set the blackbox path
 */
 class BlackBoxAdderIO extends Bundle {
   val a = Input(UInt(32.W))
   val b = Input(UInt(32.W))
   val cin = Input(Bool())
   val c = Output(UInt(32.W))
   val cout = Output(Bool())
 }

class InlineBlackBoxAdder extends HasBlackBoxInline {
  val io = IO(new BlackBoxAdderIO)
  setInline("InlineBlackBoxAdder.v",
    s"""
       |module InlineBlackBoxAdder(a, b, cin, c, cout);
       |input  [31:0] a, b;
       |input  cin;
       |output [31:0] c;
       |output cout;
       |wire   [32:0] sum;
       |
       |assign sum  = a + b + cin;
       |assign c    = sum[31:0];
       |assign cout = sum[32];
       |
       |endmodule
    """.stripMargin)
}

  /*
  two different ways to get the .v file
  */
class PathBlackBoxAdder extends HasBlackBoxPath {
  val io = IO(new BlackBoxAdderIO)
  addPath("./src/main/resources/PathBlackBoxAdder.v")
}

class ResourceBlackBoxAdder extends HasBlackBoxResource {
  val io = IO(new BlackBoxAdderIO)
  addResource("/ResourceBlackBoxAdder.v")
}

 /*
 wrapper it with the module
 */
class InlineAdder extends Module{
   val io = IO(new BlackBoxAdderIO)
   val adder = Module(new InlineBlackBoxAdder)
   io <> adder.io
 }

object UsingBlackBox extends App{
  ChiselSim.Gen(new InlineAdder())
}
