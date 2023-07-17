package SpinalHDL.ip

import spinal.core._
import spinal.lib._

//connect some duts with list(in the case)

case class Fir(width:Int) extends Component{
  val fi = slave Flow(UInt(width bits))
  val fo = master Flow(UInt(width bits))

  fi.stage() >> fo //what's the stage
  def -->(that:Fir): Fir = {
    this.fo >> that.fi
    that
  }
}


class multiDuts(width:Int,firnum:Int) extends Component {
  val fi = slave Flow (UInt(width bits))
  val fo = master Flow (UInt(width bits))

  val Firs = List.fill(firnum)(Fir(width))
  //val init = Fir(width)
  Firs.reduceLeft(_ --> _)

  //or use fold
  //Firs.foldLeft(init)(_ --> _)

  fi >> Firs(0).fi
  Firs(firnum-1).fo >> fo
}


object multiDuts extends App{
  SpinalVerilog(new multiDuts(32,8))
}