package SpinalHDL.riscv

import spinal.core._
import scala.collection.mutable
import plugins._
/*
core part:在处理核中可以指定是采用静态的流水线或者动态的流水线结构
*/

object createStaticPipeline{
  def apply(
           disablePipelining:Boolean = false,
           extraPlugins:Seq[Plugin[Pipeline]] = Seq(),
           build:Boolean = true
           )(implicit conf:Config):StaticPipeline ={
            //Todo about Static Pipeline

            val pipeline = new Component with StaticPipeline {
              setDefinitionName("Pipeline")
              val fetch = new Stage("IF")
//              val decode = new Stage("ID")
//              val excute = new Stage("EX")
//              val memory = new Stage("MEM")
//              val writeback = new Stage("WB")

              override def config: Config = conf
              override def data: StandardPipelineData = new StandardPipelineData(conf)
              override def pipelineComponent: Component = this
//              override def stages: mutable.Seq[Stage] = mutable.Seq(fetch,decode,excute,memory,writeback)
              override def stages: mutable.Seq[Stage] = mutable.Seq(fetch)
            }
        //Todo about the scheduler module and for no used pipeline
          pipeline.addPlugins(
            mutable.Seq(
              new Fetcher(pipeline.fetch)
            ))
           if(build){
             pipeline.build()
           }
          pipeline
  }
}

object createDynamicPipeline{
}



class CoreFormat extends Component{
  setDefinitionName("Core")
  implicit val config = new Config(BaseIsa.RV32I)
  //Pipeline
  val pipeline = createStaticPipeline()(conf = config)
}



//Todo Maybe you need Soc
object Core extends App {

  SpinalVerilog(new CoreFormat())
}
