// Generator : SpinalHDL v1.8.1    git head : 2a7592004363e5b40ec43e1f122ed8641cd8965b
// Component : logicorReg
// Git hash  : 7b7bc2e83298c4a8781e05e7da5b2bc64558d35c

`timescale 1ns/1ps

module logicorReg (
  input               a,
  input      [2:0]    b,
  output reg [2:0]    output_1,
  input               clk,
  input               reset
);

  reg        [2:0]    reg1;
  reg                 reg3;
  wire                when_logicorReg_l21;

  assign when_logicorReg_l21 = (! a);
  always @(*) begin
    output_1 = 3'b000;
    if(a) begin
      output_1 = (b + b);
    end
  end

  always @(posedge clk or posedge reset) begin
    if(reset) begin
      reg1 <= 3'b000;
    end else begin
      reg1 <= b;
    end
  end

  always @(posedge clk) begin
    if(a) begin
      reg3 <= 1'b1;
    end
    if(when_logicorReg_l21) begin
      reg3 <= 1'b0;
    end
  end

  `ifdef COCOTB_SIM
initial begin
  $dumpfile ("logicorReg.vcd");
  $dumpvars (0, logicorReg);
  #1;
end
`endif
endmodule
