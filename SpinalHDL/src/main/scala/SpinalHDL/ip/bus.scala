package SpinalHDL.ip

import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba3.apb._
import spinal.lib.bus.regif.BusInterface

import scala.math.Ordering.Byte

//use the bootcamp AMBA bus

class ApbReg extends Component{
  val apb = slave(Apb3(Apb3Config(8,32,2)))
  val apbslave = Apb3SlaveFactory(apb,1)
  //datawidth = 32
  val regs = Vec(Reg(UInt(32 bits)) init(0),8)
  (0 until 8).map(i => apbslave.readAndWrite(regs(i),address = i * 4))
}


class Apbslave extends Component{
  val din  =  slave(Apb3(Apb3Config(16,32)))
  val do1  = master(Apb3(Apb3Config( 8,32)))
  val do2  = master(Apb3(Apb3Config(12,32)))
  val do3  = master(Apb3(Apb3Config(12,32)))
  val do4  = master(Apb3(Apb3Config( 2,32)))

  val mux = Apb3Decoder(master = din,
    slaves = List(do1 ->  (0x0000,  64 ),
      do2 ->  (0x1000,1 KiB),
      do3 ->  (0x2000,2 KiB),
      do4 ->  (0x3000,  32 )))
}

//use bus interface
class ApbRegBank extends Component{
  val io = new Bundle{
    val apb = slave(Apb3(Apb3Config(16,32)))
  }
  val busif = BusInterface(io.apb,(0x0000,100 Byte),0)
  val Reg0 = busif.newReg("rge0")
  val Reg1 = busif.newReg("rge1")
  val Regadd = busif.newRegAt(address = 0x40,doc = "regadd")
}


object bus extends App{
  SpinalVerilog(new ApbRegBank)
}

