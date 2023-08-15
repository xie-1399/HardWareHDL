package SpinalHDL.lib.FlowSource

import spinal.core._
import spinal.lib.{DataCarrier, IMasterSlave, MSFactory,Stream}

//the Flow source code and learn more about using flow

//use master and slave
class FlowFactory extends MSFactory{
  def apply[T <: Data](payloadType:HardType[T]) = {
    val ret = new Flow(payloadType)
    postApply(ret)
    ret
  }
  def apply[T <: Data](payloadType: => T) : Flow[T] = apply(HardType(payloadType))
}


object Flow extends FlowFactory


class Flow[T <: Data](val payloadType: HardType[T]) extends Bundle with IMasterSlave with DataCarrier[T]{
  override def asMaster(): Unit = out(this)

  override def asSlave(): Unit = in(this)

  override def fire: Bool = valid

  override def valid: Bool = Bool()

  override def payload: T = payloadType()

  override def clone(): Bundle = Flow(payloadType).asInstanceOf[this.type ]

  override def freeRun(): Flow.this.type = this

  //set idle status
  def setIdle():this.type = {
    valid := False
    payload.assignDontCare()
    this
  }

  //set to Reg
  def toReg() : T = toReg(null.asInstanceOf[T])
  def toReg(init:T):T = RegNextWhen(this.payload,this.fire,init)  //when valid rise will use init

  def connectFrom(that: Flow[T]): Flow[T] = {
    valid := that.valid
    payload := that.payload
    that
  }

  def push(that:T):Unit = {
    valid := True
    payload := that
  }

  def default(that:T):Unit = {
    valid := False
    payload := that
  }

  def <<(that:Flow[T]):Flow[T] = this connectFrom that
  def >>(into:Flow[T]):Flow[T] = {
    into << this
    into
  }

  def m2sPipe:Flow[T] = m2sPipe()
  def m2sPipe(holdPayload:Boolean = false,flush : Bool = null, crossClockData:Boolean = false):Flow[T] = {
    if(!holdPayload){
      val ret = RegNext(this) //wait one cycle
      ret.valid.init(False)
      if(flush != null) when(flush){ret.valid := False}
      if(crossClockData) ret.payload.addTag(crossClockDomain)  //add tag about the cross clock data
      ret
    }
    else{
      val ret = Reg(this)
      ret.valid.init(False)
      ret.valid := this.valid
      when(this.valid){
        ret.payload := this.payload
      }
      if (flush != null) when(flush) {ret.valid := False}
      if (crossClockData) ret.payload.addTag(crossClockDomain)
      ret
    }.setCompositeName(this,"m2sPipe",true)

  }
  //connect with one cycle late
  def <-<(that:Flow[T])  = {
    this << that.m2sPipe
  }
  def >->(that:Flow[T]) = {
    that <-< this
  }
  def stage():Flow[T] = this.m2sPipe


  //seem like clone the flow
  def combStage():Flow[T] = {
    val ret = Flow(payload).setCompositeName(this,"combStage",true)
    ret << this
    ret
  }

  def swapPayload[T2 <: Data](that:HardType[T2]) = {
    val next = new Flow(that).setCompositeName(this,"swap",true)
    next.valid := this.valid
    next
  }

  //convert to the Stream
  def toStream : Stream[T] = toStream(null)
  def toStream(overflow:Bool) : Stream[T] = {
    val ret = Stream(payloadType)
    ret.valid := this.valid
    ret.payload := this.payload
    if(overflow != null) overflow := ret.valid && !ret.ready
    ret
  }

  //add cond about the valid
  def takeWhen(cond:Bool) : Flow[T] = {
    val next = new Flow(payloadType).setCompositeName(this,"takeWhen",true)
    next.valid := this.valid && cond
    next.payload := this.payload
    next
  }

  def throwWhen(cond:Bool):Flow[T] = {
    this.takeWhen(!cond)
  }

  //use the cmd but trans with another data
  def translateWith[T2 <: Data](that:T2):Flow[T2] = {
    val next = new Flow(that)
    next.valid := this.valid
    next.payload := that
    next
  }

  //use another flow drive this
//  def translateFrom[T2 <: Data](that: Flow[T2])(dataAssignment: (T, that. ) => Unit): Flow[T] = {
//    this.valid := that.valid
//    dataAssignment(this.payload, that.payload)  //define the payload using func
//    this
//  }


  //more about the cross clockDomain -> FlowCCByToggle

}
