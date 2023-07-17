#Learn about the cocotb demo

import cocotb
import random
from cocotb.clock import Clock
from cocotb.triggers import *

@cocotb.test()
async def regtest(dut):
    clock = Clock(dut.clk,1,units="ns")
    cocotb.start_soon(clock.start())

    for i in range(100):
        val = random.randint(0,1)
        dut.a.value = val
        dut.b.value = val
        await FallingEdge(dut.clk)
        if(dut.a.value):
            assert dut.output_1.value == 2 * dut.b.value