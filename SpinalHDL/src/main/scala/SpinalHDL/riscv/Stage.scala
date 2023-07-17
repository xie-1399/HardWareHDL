package SpinalHDL.riscv

/*
每个流水线阶段都有相应的控制信号，同时也有对应的输入和输出的数据

//use map

object Stage extends App{
  val map = mutable.Map.empty[String,Int]
  map("hello") = 1
  println(map)
  SpinalVerilog(new Stage("Fetch"))
}
*/
import spinal.core._
import scala.collection.mutable

//每个阶段的一些控制信号
class Arbitration() extends Bundle{
  val isValid = in Bool()
  val isStalled = in Bool()
  val isReady = in Bool()
  val isDone = in Bool()
  val rs1Needed = out Bool()
  val rs2Needed = out Bool()
  val jumpRequest = out Bool()
  val isAvalilable = out Bool()

  def isIdle:Bool = !isValid || isStalled  //True when there either is no instruction in the current stage or it is stalled.

  def isRunning:Bool = !isIdle // Is this stage currently processing an instruction
  isAvalilable := isDone || !isValid //流水线在完成指令或者没有指令时都是可用的
  isReady := True
  isReady.allowOverride()
  rs1Needed := False
  rs2Needed := False
  jumpRequest := False
  isDone := isValid && isReady && !isStalled
}

class Stage(val stageName:String) extends Component {
  //define name about the component
  definitionName = s"$stageName"+ s"_Stage"

  val arbitration = new Arbitration
  //Todo about the pipeline data
  //Map from PipelineData to the Data
  def PipelineRegMap() = mutable.Map[PipelineData[Data],Data]()
  val inputs = PipelineRegMap()
  val outputs = PipelineRegMap()
  val outputsDefaults = PipelineRegMap()
  val lastValues = PipelineRegMap() //Todo about lastValues

  //看这个reg是不是在inputs缓存里面，不在的话则直接加入到缓存里面去
  def input[T <: Data](reg: PipelineData[T]):T ={
    inputs.getOrElseUpdate(
      reg.asInstanceOf[PipelineData[Data]],rework{
        val input = in(reg.dataType())
        input.setName(s"in_${reg.name}")
        input
      }
    ).asInstanceOf[T]
  }

  def hasInput[T <: Data](reg: PipelineData[T]): Boolean = inputs.contains(reg.asInstanceOf[PipelineData[Data]])

  //output输出的时候需要先连接到default信号上，这样可能会避免一些错误信息
  def output[T <: Data](reg: PipelineData[T]): T = {
    val regAsData = reg.asInstanceOf[PipelineData[Data]]
    outputs
      .getOrElseUpdate(
        regAsData,
        rework{ //reworkOutsideConditionScope
          val output = out(reg.dataType())
          output.setName(s"out_${reg.name}")

          val default = reg.dataType()
          default.setName(s"_out_default_${reg.name}")
          default.assignDontCare()
          default.allowOverride
          outputsDefaults(regAsData) = default
          output := default
          output.allowOverride
          output
        }
      )
      .asInstanceOf[T]
  }

  def hasOutput[T<:Data](reg:PipelineData[T]) : Boolean = outputs.contains(reg.asInstanceOf[PipelineData[Data]])

  //Todo 寄存器的值?
  def value[T <: Data](reg: PipelineData[T]): T = {
    lastValues
      .getOrElseUpdate(
        reg.asInstanceOf[PipelineData[Data]],
        rework {
          val lastValue = reg.dataType()
          lastValue.setName(s"value_${reg.name}")
          lastValue
        }
      )
      .asInstanceOf[T]
  }

  //将输入值接到outputDefaults上
  def connectOutputDefaults():Unit = rework( //直接从输入接出去的一些信号
    for ((data, default) <- outputsDefaults){
      if(inputs.contains(data)){ //default来自输入？
        val input = inputs(data)
        default := input
      }
    }
  )

  def connectLastValues():Unit = rework{
    for((data,lastValue) <- lastValues){
      if(outputs.contains(data)){
        lastValue := output(data)
      }else if(inputs.contains(data)){
        lastValue := input(data)
      }else{
        assert(false,s"No value for ${data.name} in stage ${stageName}")
      }
    }
  }

//  private def reworkOutsideConditionScope[T](rtl: => T) = {
//    Utils.outsideConditionScope(rework(rtl))
//  }

}
