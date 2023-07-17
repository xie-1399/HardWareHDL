package DayliComponent.bus.myAPB

//需要理解APB的读写过程
//写过程：从IDLE阶段开始，首先经历地址阶段，这时会将PSEL拉高（是根据译码电路找到了APB的Slave），并且将要写的数据准备好，之后进入传输阶段，将PENABLE拉高，同时等PREADY也拉高时进行数据传输
//读过程：读和写非常的类似

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

//Todo if has some errors need to deal with

class myAPBStateMachine(apbConfig: ApbConfig,dataWidth:Int) extends Component {
    val io = new Bundle{
      val apb = slave(APB(apbConfig)) //in (PSEL,PENABLE,PWRITE,PADDR,PWDATA)
      val writeEnable = out(Bool())
      val readEnable = out(Bool())
    }

    val ram = Mem(Bits(dataWidth bits) , 256)
    io.apb.PREADY := True  //No Wait signal
    io.apb.PRDATA := 0
    io.writeEnable := False
    io.readEnable := False
    ram.write(io.apb.PADDR, io.apb.PWDATA,enable = io.apb.PREADY && io.apb.PWRITE && io.apb.PSEL(0))

    //使用APB总线读写的状态机
    val read = new Area {
      val fsm = new StateMachine{
        val IDLE: State = new State with EntryPoint {
          whenIsActive{
            when(io.apb.PSEL(0) && !io.apb.PWRITE){ //read
              goto(SETUP)
            }
          }
        }
        val SETUP:State = new State{
          whenIsActive{
            when(io.apb.PENABLE){
              goto(ENABLE)
            }
          }
        }

        val ENABLE:State = new State{
          //传输数据
          whenIsActive{
            io.readEnable := True
            io.apb.PRDATA := ram.readSync(io.apb.PADDR,enable = io.apb.PREADY)
            //io.apb.PRDATA := ram.readAsync(io.apb.PADDR)
            when(io.apb.PSEL(0) && io.apb.PREADY){
              goto(SETUP)
            }
            when(!io.apb.PSEL(0)){
              goto(IDLE)
            }
          }
        }
      }
    }

    val write = new Area {
      val fsm = new StateMachine {
        val IDLE: State = new State with EntryPoint {
          whenIsActive {
            when(io.apb.PSEL(0) && io.apb.PWRITE) {
              goto(SETUP)
            }
          }
        }
        val SETUP: State = new State {
          whenIsActive {
            when(io.apb.PENABLE) {
              goto(ENABLE)
            }
          }
        }

        val ENABLE: State = new State {
          //传输数据
          whenIsActive {
            io.writeEnable := True
            when(io.apb.PSEL(0) && io.apb.PREADY) {
              goto(SETUP)
            }
            when(!io.apb.PSEL(0)) {
              goto(IDLE)
            }
          }
        }
      }
    }
}

