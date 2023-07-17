package SpinalHDL.riscv

import spinal.core._
import spinal.lib._


trait MemoryService{
  /*
  Returns the Pipeline's instruction / data Bus
  */
  def getExternalIBus : MemBus
  def getExternalDBus : MemBus

  /*
  Create a new instruction bus / data bus to be used in stage(Internal)
  */
  def createInternalIBus(stage:Stage):MemBus
  def createInternalDBus(stage:Stage):MemBus
}


trait FetchAddressTranslator{
  //提供取指的地址翻译
  def translator(stage: Stage,address:UInt):UInt
}

trait FetchService{
  def setAddressTranslator(translator: FetchAddressTranslator):Unit
}