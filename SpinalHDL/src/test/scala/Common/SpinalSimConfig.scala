package Common
import spinal.core._
import spinal.core.sim.SimConfig

//set SpinalHDL Sim Config here

object SpinalSimConfig {
  def apply() = SimConfig.withVcdWave.withConfig(
    SpinalConfig(targetDirectory = "rtl")).workspacePath("simulation")
}
