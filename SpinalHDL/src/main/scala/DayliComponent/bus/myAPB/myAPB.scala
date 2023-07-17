package DayliComponent.bus.myAPB
import spinal.core._
import spinal.lib._

case class ApbConfig(addressWidth:Int,
                     dataWidth:Int,
                     selWidth:Int,
                     useSlaveError:Boolean = false)


case class APB(apbConfig:ApbConfig) extends Bundle with IMasterSlave {
  val PSEL = Bits(apbConfig.selWidth bits)
  val PENABLE = Bool()
  val PWRITE = Bool()
  val PREADY = Bool()
  val PADDR = UInt(apbConfig.addressWidth bits)
  val PWDATA = Bits(apbConfig.dataWidth bits)
  val PRDATA = Bits(apbConfig.dataWidth bits)
  val PSLVERROR = if(apbConfig.useSlaveError) Bool() else null
  override def asMaster(): Unit = {
    in(PRDATA,PREADY)
    out(PSEL,PENABLE,PWRITE,PADDR,PWDATA)
    if(apbConfig.useSlaveError) in(PSLVERROR)
  }

  //Maybe you should connect one bus to another
  def >> (sink:APB):Unit = {
    assert(this.apbConfig.selWidth == sink.apbConfig.selWidth)
    sink.PADDR := this.PADDR.resized
    sink.PSEL := this.PSEL
    sink.PENABLE := this.PENABLE
    sink.PWRITE := this.PWRITE
    sink.PWDATA := this.PWDATA
    this.PRDATA := sink.PRDATA
    this.PREADY := sink.PREADY
    if(PSLVERROR != null){
      this.PSLVERROR := ( if (sink.PSLVERROR != null) sink.PSLVERROR else False)
    }
  }

  def << (sink : APB) : Unit = sink >> this


}
