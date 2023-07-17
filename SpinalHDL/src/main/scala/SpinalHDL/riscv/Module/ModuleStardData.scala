package SpinalHDL.riscv.Module

import spinal.core._

object RegisterType extends SpinalEnum{
  //The Enumeration type corresponds to a list of named values.
  val NONE,GPR = newElement()
}

class PipelineData[T <: Data](val dataType:HardType[T]){ //Todo about HardType , its no need to use clone of
  def name = getClass.getSimpleName.takeWhile(_ != '$')
  override def toString: String = name  // just set Name
}

case class ModuleStardData(config: ModuleConfig){
  private val xlen = config.xlen

  //直接使用object创建单例对象，实例化之后就可以直接访问
  object PC extends PipelineData(UInt(xlen bits))
  object NeXT_PC extends PipelineData(UInt(xlen bits))
  object IR extends PipelineData(UInt(32 bits)) //Todo what is IR = read Instruction

  object RS1 extends PipelineData(UInt(5 bits)) //15 ~ 19
  object RS1_DATA extends PipelineData(UInt(xlen bits))
  object RS1_TYPE extends PipelineData(RegisterType()) //寄存器 or 立即数

  object RS2 extends PipelineData(UInt(5 bits)) //15 ~ 19
  object RS2_DATA extends PipelineData(UInt(5 bits)) //20 ~ 24
  object RS2_TYPE extends PipelineData(RegisterType()) //寄存器 or 立即数

  object RD extends PipelineData(UInt(5 bits)) //7 ~ 11
  object RD_DATA extends PipelineData(UInt(xlen bits))
  object RD_TYPE extends PipelineData(RegisterType()) //寄存器 or 立即数
  object RD_DATA_VALID extends PipelineData(Bool())

  object IMM extends PipelineData(UInt(32 bits))
  object IMM_USED extends PipelineData(Bool())
}
