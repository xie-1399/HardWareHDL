package SpinalHDL.riscv.plugins

import spinal.core._
import spinal.lib._
import SpinalHDL.riscv._

//FetchService用于设定取指的地址映射，即SetaddressTranslator
class Fetcher(fetchstage:Stage,ibusLatency:Int = 2) extends Plugin[Pipeline] with FetchService {
  private var addressTranslator = new FetchAddressTranslator {
    //Todo no MMU Translator
    override def translator(stage: Stage, address: UInt): UInt = address
  }

  private var addressTranslatorChanged = false

  override def build(): Unit = {
    fetchstage plug new Area {
      import fetchstage._

      //Todo Define the ibus

      val ibus = pipeline.service[MemoryService].createInternalIBus(fetchstage)
      val ibusCtrl = new IBusControl(ibus,ibusLatency)

      arbitration.isReady := False
      val pc = input(pipeline.data.PC)
      val nextPC = pc + 4

      when(arbitration.isRunning){
        val fetch_address = addressTranslator.translator(fetchstage,pc)
        //Bus send the address
        val (valid,rdata) = ibusCtrl.read(fetch_address)
        when(valid){
          arbitration.isReady := True
          output(pipeline.data.NeXT_PC) := nextPC
          output(pipeline.data.IR) := rdata
        }
      }
    }
  }

  override def setAddressTranslator(translator: FetchAddressTranslator): Unit = {
    assert(!addressTranslatorChanged,"Fetch Address can only change once")
    addressTranslator = translator
    addressTranslatorChanged = true

  }
}
