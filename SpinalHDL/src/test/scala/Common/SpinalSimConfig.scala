package Common
import spinal.core._
import spinal.core.sim.SimConfig
import spinal.core.sim._
//set SpinalHDL Sim Config here

object SpinalSimConfig {
  def apply() = SimConfig.withVcdWave.withConfig(
    SpinalConfig(targetDirectory = "rtl")).workspacePath("simulation")

  //only sample to produce the wave with iter cycles
  def onlySample(clockDomain: ClockDomain,operation:() => Unit,iter:Int = 100): Unit = {
    for(idx <- 0 until iter){
      operation()
      clockDomain.waitSampling()
    }
  }
}

