package DayliComponent.IO

import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba4.axi._
import spinal.lib.bus.amba4.axilite._
import spinal.lib.bus.amba4.axilite.AxiLite4Utils.Axi4Rich
import spinal.lib.bus.amba4.axilite.AxiLite4Utils.AxiLite4Rich
import spinal.lib.misc._
import DayliComponent.bus.myAPB._
import spinal.lib.misc.plic.AxiLite4Plic

case class Axitolite() extends Component {
  val io = new Bundle{
    val axicmd = slave(Axi4(busconfig.axiConfig))
    val axiLite4cmd = master(AxiLite4(busconfig.axilite4Config))
  }
  io.axicmd.toLite() >> io.axiLite4cmd
}

case class Litetoaxi() extends Component{
  val io = new Bundle{
    val axicmd = master(Axi4(busconfig.axiConfig))
    val axiLite4cmd = slave(AxiLite4(busconfig.axilite4Config))
  }
  io.axiLite4cmd.toAxi() >> io.axicmd
}

class ClintLite4() extends Component{
  val io = new Bundle {
    val axicmd = slave(Axi4(busconfig.axiConfig))
    val timerInterrupt = out Bits (4 bits)
    val softwareInterrupt = out Bits (4 bits)
    val time = out UInt(64 bits)
  }
  val adapter = Axitolite()
  adapter.io.axicmd <> io.axicmd

  val clintCtrl = AxiLite4Clint(4, bufferTime = false) //hartCount
  io.timerInterrupt := clintCtrl.io.timerInterrupt
  io.softwareInterrupt := clintCtrl.io.softwareInterrupt
  io.time := clintCtrl.io.time
  //Todo P to P
  //adapter.io.axiLite4cmd<>clintCtrl.io.bus.resized
  adapter.io.axiLite4cmd.aw.ready := clintCtrl.io.bus.aw.ready
  adapter.io.axiLite4cmd.w.ready := clintCtrl.io.bus.w.ready
  adapter.io.axiLite4cmd.b.valid := clintCtrl.io.bus.b.valid
  adapter.io.axiLite4cmd.b.payload.resp := clintCtrl.io.bus.b.payload.resp
  adapter.io.axiLite4cmd.ar.ready := clintCtrl.io.bus.ar.ready
  adapter.io.axiLite4cmd.r.valid := clintCtrl.io.bus.r.valid
  adapter.io.axiLite4cmd.r.payload.data := clintCtrl.io.bus.r.payload.data
  adapter.io.axiLite4cmd.r.payload.resp := clintCtrl.io.bus.r.payload.resp

  clintCtrl.io.bus.aw.valid := adapter.io.axiLite4cmd.aw.valid
  clintCtrl.io.bus.aw.addr := adapter.io.axiLite4cmd.aw.addr.resized
  clintCtrl.io.bus.w.valid := adapter.io.axiLite4cmd.w.valid
  clintCtrl.io.bus.w.payload := adapter.io.axiLite4cmd.w.payload
  clintCtrl.io.bus.b.ready := adapter.io.axiLite4cmd.b.ready
  clintCtrl.io.bus.ar.valid := adapter.io.axiLite4cmd.ar.valid
  clintCtrl.io.bus.ar.payload.addr:= adapter.io.axiLite4cmd.ar.payload.addr.resized
  clintCtrl.io.bus.r.ready:= adapter.io.axiLite4cmd.r.ready
}

class PlicLite4 extends Component{
  val io = new Bundle {
    val axicmd = slave(Axi4(busconfig.axiConfig))
  }

  val adapter = Axitolite()
  adapter.io.axicmd <> io.axicmd
  val plicCtrl = new AxiLite4Plic(
    sourceCount = 31,
    targetCount = 2
  )
  //plicCtrl.io.bus.resized <> adapter.io.axiLite4cmd
  val plicInterrupts = in Bits (32 bits)
  plicCtrl.io.sources := plicInterrupts >> 1
  // for just the Axilite4 interface
  //  val clint = clintCtrl.io.bus.toIo()
  //  val plic = plicCtrl.io.bus.toIo()

  //Todo p to p
  adapter.io.axiLite4cmd.aw.ready := plicCtrl.io.bus.aw.ready
  adapter.io.axiLite4cmd.w.ready := plicCtrl.io.bus.w.ready
  adapter.io.axiLite4cmd.b.valid := plicCtrl.io.bus.b.valid
  adapter.io.axiLite4cmd.b.payload.resp := plicCtrl.io.bus.b.payload.resp
  adapter.io.axiLite4cmd.ar.ready := plicCtrl.io.bus.ar.ready
  adapter.io.axiLite4cmd.r.valid := plicCtrl.io.bus.r.valid
  adapter.io.axiLite4cmd.r.payload.data := plicCtrl.io.bus.r.payload.data
  adapter.io.axiLite4cmd.r.payload.resp := plicCtrl.io.bus.r.payload.resp

  plicCtrl.io.bus.aw.valid := adapter.io.axiLite4cmd.aw.valid
  plicCtrl.io.bus.aw.addr := adapter.io.axiLite4cmd.aw.addr.resized
  plicCtrl.io.bus.w.valid := adapter.io.axiLite4cmd.w.valid
  plicCtrl.io.bus.w.payload.data := adapter.io.axiLite4cmd.w.payload.data
  plicCtrl.io.bus.b.ready := adapter.io.axiLite4cmd.b.ready
  plicCtrl.io.bus.ar.valid:= adapter.io.axiLite4cmd.ar.valid
  plicCtrl.io.bus.ar.addr := adapter.io.axiLite4cmd.ar.addr.resized
  plicCtrl.io.bus.r.ready := adapter.io.axiLite4cmd.r.ready
}