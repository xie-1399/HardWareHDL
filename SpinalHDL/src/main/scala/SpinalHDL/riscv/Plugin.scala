package SpinalHDL.riscv

import spinal.core._

/*
Plugin抽象类,一个非常重要的方法是隐式规则，比如下面的这个例子，其实就是在编译的时候插入一些新的规则
object Plugin{
  implicit def DoubletoInt(x:Double) = x.toInt
  case class demo(width:Int,height:Int){}

  implicit class demomaker(width:Int){
    def x(height:Int) = demo(width,height)
   }
  def main(args: Array[String]): Unit = {
    val num:Int = 3.5
    println(num)
    val counter:demomaker = demomaker(10)
  }
  val maker = 3 x 4
}

关于泛型的使用：泛型可以接收多种类型的参数
class Queue[T](private val leading:List[T],private val trailing:List[T]){
  private def mirror() =
    if (leading.isEmpty) new Queue(trailing.reverse,Nil)
    else this
  def head = mirror.leading.head
  def tail = {
    val q = mirror
    new Queue(q.leading.tail,q.trailing)
  }
  def enqueue(x:T) = new Queue[T](leading, x::trailing)
}

*/


abstract class Plugin[-PipelineT <: Pipeline]{
  //需要指定plugin所属于的pipeline
  protected [this] var pipeline:PipelineT = _
  implicit def config = pipeline.config

  def setPipeline(pipeline: PipelineT):Unit = {
    assert(this.pipeline == null)
    this.pipeline = pipeline
  }

  def getName = getClass.getSimpleName.replace("$","")

  def setup():Unit = {}

  def build():Unit = {}

  def finish():Unit = {} //Todo

  //set Component or Area Name
  implicit class PlugComponent(component: Component){
    def plug[T](logic: => T):T = component.rework{
      val result =logic
      result match {
        case component:Component => component.setName(getName)
        case area: Area => area.setName(getName).reflectNames()
        case _ =>
      }
      result
    }
  }

  implicit class PlugPipeline(pipeline: Pipeline){
    def plug[T](logic: => T):T = pipeline.pipelineComponent plug logic
  }

  def getImplementedExtensions:Seq[Extension] = Seq()
  implicit def charToExtension(char:Char) = Extension(char)

}


