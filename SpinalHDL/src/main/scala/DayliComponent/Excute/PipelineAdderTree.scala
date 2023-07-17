package DayliComponent.Excute

//it's about adder tree (pipeline)

import spinal.core._
import spinal.lib._

//one adder example
case class Sum(inputWidth:Int,size:Int,stage:Int) extends Component {
  this.setDefinitionName(s"sum_Stage_${stage}")
  //output width
  val outputWidth = inputWidth + log2Up(size)
  val io = new Bundle{
    val nets = slave Flow(Vec(SInt(inputWidth bits),size))
    val sum = out(SInt(outputWidth bits)).setAsReg()  //set as Reg
  }
  when(io.nets.valid){
    io.sum := io.nets.payload.map(_.resize(outputWidth bits)).reduce(_ + _)  //should set as the same bits
  }
}

//需要注意的一个点是如何实现分组的
class PipelineAdderTree(inputWidth:Int,size:Int,groupsize:Int) extends Component {

  //create one sum method
  private def sumAdd(nets:Flow[Vec[SInt]],stage:Int):Sum = {
    val uSum = Sum(nets.payload.head.getWidth,nets.payload.size,stage)
    uSum.io.nets.valid := nets.valid
    uSum.io.nets.payload := nets.payload
    uSum
  }

  //几个为一组 （group size） resursive
  def pipeTree(nets:Flow[Vec[SInt]],groupSize:Int,stage:Int = 0):(List[Sum],Int) = {
    val nextstage = stage + 1

    if(nets.payload.size <= groupSize){
      //end case
      (List(sumAdd(nets,nextstage)),nextstage)
    }else{
      val groupnum = scala.math.ceil(nets.payload.size.toDouble / groupSize).toInt
      val nextStage = (0 until groupnum).toList
        .map(i => nets.payload.drop(i * groupSize).take(groupSize)) //提取前四个数据
        .map{
          grouped => val groupednets = Flow(Vec(SInt(grouped.head.getBitsWidth bits),grouped.size))
          groupednets.valid := nets.valid
          groupednets.payload := Vec(grouped)
          sumAdd(groupednets,nextstage)
        }
      val ret = Flow(Vec(SInt(nextStage.head.io.sum.getWidth bits), nextStage.size))
      ret.valid := RegNext(nets.valid, init = False)
      ret.payload := Vec(nextStage.map(_.io.sum)).resized
      pipeTree(ret,groupSize,nextstage)
    }
  }
  //sum size
  val io_nets = slave Flow(Vec(SInt(inputWidth bits),size))
  val(sum,stage) = pipeTree(io_nets,groupsize,0)
  this.setDefinitionName(s"adderTree")

  def Latency:Int = stage //every stage has latency

  def outputWidth:Int = inputWidth + log2Up(groupsize) * stage
  val io_sum = master Flow(SInt(sum.head.io.sum.getWidth bits))

  io_sum.payload := sum.head.io.sum
  io_sum.valid := RegNext(sum.head.io.nets.valid,init = False)
}


object PipelineAdderTree {
  def apply(nets: Flow[Vec[SInt]], addCellSize: Int): PipelineAdderTree = {
    val uAdderTree = new PipelineAdderTree(nets.payload.head.getWidth, nets.payload.size, addCellSize)
    uAdderTree.io_nets := nets
    uAdderTree
  }

  def apply(nets: Vec[SInt], addCellSize: Int): PipelineAdderTree = {
    val uAdderTree = new PipelineAdderTree(nets.head.getWidth, nets.size, addCellSize)
    uAdderTree.io_nets.payload := nets
    uAdderTree.io_nets.valid   := True
    uAdderTree
  }
}

object AdderTree extends App{
  SpinalVerilog(new PipelineAdderTree(8,16,4))
}