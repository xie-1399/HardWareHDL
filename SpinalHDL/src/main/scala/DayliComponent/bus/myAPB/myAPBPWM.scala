package DayliComponent.bus.myAPB
import spinal.core._
import spinal.lib._

/*
Test for 要注意Area声明的顺序，不能前面使用后面的Area，这会引起错误,这里并没有对状态机进行实现
*/
class myAPBPWM(apbConfig:ApbConfig,timerWidth:Int) extends Component {
  val io = new Bundle {
    val apb = slave(APB(apbConfig))
    val pwm = out Bool()
  }
  //就是比较timer寄存器和dutycycle寄存器的值，前者小于后者输出高电平，否则输出低电平。
  val logic = new Area {
    val enable = Reg(Bool()) init (False)
    val dutycycle = Reg(UInt(timerWidth bits)) init (0)
    val timer = Reg(UInt(timerWidth bits)) init (0)
    when(enable) {
      timer := timer + 1
    }
    when(timer < dutycycle) {
      io.pwm := True
    }.otherwise {
      io.pwm := False
    }
  }

  val control = new Area {
    val write = io.apb.PENABLE && io.apb.PSEL(0) && io.apb.PWRITE
    io.apb.PRDATA := 0
    io.apb.PREADY := True
    switch(io.apb.PADDR) {
      is(0) {
        io.apb.PRDATA(0) := logic.enable
        when(write) {
          logic.enable := io.apb.PWDATA(0)
        }
      }
      is(4) {
        io.apb.PRDATA := logic.dutycycle.asBits.resized
        when(write) {
          logic.dutycycle := io.apb.PWDATA.asUInt.resized
        }
      }
    }
  }
}
