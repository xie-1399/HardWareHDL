package SpinalHDL.lib.MemSource

import spinal.core._
import spinal.lib.Stream
//about the Mem Source Code（there will be some api to operator the Mem）

case class ReadRetLinked[T <: Data, T2 <: Data](readType: HardType[T], linkedType: HardType[T2]) extends Bundle {
  val value = readType()
  val linked = linkedType()
}


class MemPimped[T <: Data](mem: Mem[T]) {

  //use Stream to readsync mem

  def streamReadSync[T2 <: Data](cmd:Stream[UInt],linkedData:T2,crossClock:Boolean = false) : Stream[ReadRetLinked[T,T2]] = {
    val ret = Stream(ReadRetLinked(mem.wordType,linkedData))

    val retValid = RegInit(False)
    val retData = mem.readSync(address = cmd.payload,enable = cmd.fire,clockCrossing = crossClock)
    val retLinked = RegNextWhen(linkedData,cmd.ready)

    when(ret.ready){
      retValid := Bool(false)  //or False
    }
    when(cmd.ready){
      retValid := cmd.valid
    }

    cmd.ready := ret.isFree   //!ret.valid || ret.ready
    ret.valid := retValid
    ret.value := retData
    ret.linked := retLinked
    ret
  }

  //Stream/Flow read sync example like this(if more continue stream comes)

  def streamReadSync(cmd: Stream[UInt]): Stream[T] = streamReadSync(cmd, crossClock = false)
  def streamReadSync(cmd: Stream[UInt], crossClock: Boolean): Stream[T] = {
    val ret = Stream(mem.wordType)
    val retValid = RegInit(False)
    val retData = mem.readSync(cmd.payload, cmd.fire, clockCrossing = crossClock)
    when(ret.ready) {
      retValid := Bool(false)
    }
    when(cmd.ready) {
      retValid := cmd.valid
    }
    cmd.ready := ret.isFree //!ret.valid || ret.ready
    ret.valid := retValid
    ret.payload := retData
    ret
  }



}

