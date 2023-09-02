package Gram
import chisel3._
import chisel3.iotesters.{PeekPokeTester, Driver}

class UsingModule extends Module{
  val io = IO(new Bundle{
    val out = Output(UInt(8.W))
  })
  io.out := 42.U
}

class UsingModuleTester(c:UsingModule) extends PeekPokeTester(c){
  step(1)
  expect(c.io.out,42.U)
}

object UsingModule{
  def main(args: Array[String]): Unit = {
    //chisel3.Driver.execute(args,() => new UsingModule)
    if (!Driver(() => new UsingModule())(c => new UsingModuleTester(c))) System.exit(1)
  }
}