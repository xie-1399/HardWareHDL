package SpinalHDL.riscv

import spinal.core._

import scala.collection.mutable
import scala.reflect.ClassTag
/*
(1)可能需要先了解一下什么是trait，这个特质不妨理解为JAVA里面的接口
(2)final def表示无法被重写
trait Philosophical{
  def function2() = {
    println("trait" + 1.toString)
  }
}

class Frog extends Philosophical{
  def function(): Unit = super.function2()
  override def toString: String = "Frog"
}

object Pipeline{
  def main(args: Array[String]): Unit = {
      println("Pipeline")
      val frog = new Frog
      println(frog)
      frog.function()
      frog.function2()
  }
}
(3)整个构建过程就是设定插件对应的Pipeline,setup and build所有的插件，最后将所有的Stage连接起来？
*/

trait Pipeline {
  private val plugins = mutable.ArrayBuffer[Plugin[this.type ]]()  //可变数量的数组
  def config:Config  //指定ISA的类型
  def data:StandardPipelineData //规定好的流水线中的数据
  def pipelineComponent:Component
  def stages:mutable.Seq[Stage]
  def fetchStage:Stage
  def retirementStage:Stage

  //添加一个插件
  def addPlugin(plugin: Plugin[this.type ]):Unit = {
    plugins += plugin
  }

  //添加多个插件
  def addPlugins(plugin: Seq[Plugin[this.type ]]) :Unit = {
    plugins ++= plugin
  }

  //整个流水线的Component需要进行初始化
  final def initBuild():Unit = {
    pipelineComponent.rework{ //Todo how to use the rework?
      init()
    }
    //设定所有插件的Pipeline
    plugins.foreach(_.setPipeline(this))
  }

  final def setupPlugins():Unit = {
    plugins.foreach(_.setup())
  }

  final def buildPlugins():Unit = {
    plugins.foreach(_.build())
  }

  final def finishBuild():Unit ={
    pipelineComponent.rework{
      //将流水线的不同阶段连接起来
      connectStages()
    }
    plugins.foreach(_.finish())
  }

  def build():Unit ={
    initBuild()
    setupPlugins()
    buildPlugins()
    finishBuild()
  }

  protected def init():Unit
  protected def connectStages():Unit

  //进行模板类型的过滤,其实就当作是查看所有的服务
  def serviceOption[T](implicit tag:ClassTag[T]):Option[T] = {
    val services = plugins.filter(_ match {
      case _:T => true
      case _ => false
    })

    assert(services.length <= 1)
    services.headOption.map(_.asInstanceOf[T])
  }

  def service[T](implicit tag:ClassTag[T]):T = {
    serviceOption[T].get
  }

  def hasService[T](implicit tag:ClassTag[T]):Boolean = {
    serviceOption[T].isDefined
  }

  def getImplementedExtensions:Seq[Extension] = {
    Extension(config.baseIsa) +: plugins.flatMap(_.getImplementedExtensions)
  }
}
