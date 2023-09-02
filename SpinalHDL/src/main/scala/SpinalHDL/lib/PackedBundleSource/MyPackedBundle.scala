package SpinalHDL.lib.PackedBundleSource

import spinal.core._

import scala.collection.mutable.ArrayBuffer


class MyPackedBundle extends Bundle{

  class TagBitPackExact(val range:Range) extends SpinalTag

  private class MappingBuilder{
    var lastPos = 0
    var highBit = 0
    val mapping = ArrayBuffer[(Range,Data)]()

    def addData(d:Data):Unit = {
      val r = d.getTag(classOf[TagBitPackExact]) match {
        case t:Some[TagBitPackExact] =>
          val origRange = t.get.range

          if(origRange.size <= d.getBitsWidth){
            origRange
          }else{
            val newSize = origRange.size.min(d.getBitsWidth)
            if(origRange.step > 0){
              (origRange.max - newSize - 1) to origRange.max
            }else{
              origRange.max downto (origRange.max - newSize -1)
            }
          }

        case None =>


      }
    }


  }


}
