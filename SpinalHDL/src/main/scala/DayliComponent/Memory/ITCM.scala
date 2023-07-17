package DayliComponent.Memory

//how to write the ITCM component

case class ITCMConfig(TCMSize:BigInt,
                      bytePerLine:Int,
                      addressWidth:Int,
                      cpuDataWidth:Int,
                      memDataWidth:Int,
                      bypassGen:Boolean = false,
                      twoCycleCache:Boolean = true,
                      bankCount:Int)

