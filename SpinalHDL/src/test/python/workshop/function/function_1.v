// Generator : SpinalHDL v1.8.2    git head : e61ce1baf9647b1ce757909fe4780481f3fad92d
// Component : function_1
// Git hash  : 014f8da8030f5c59ac4354cd63e637700f1b55b6

`timescale 1ns/1ps

module function_1 (
  input               io_cmd_valid,
  input      [7:0]    io_cmd_payload,
  output     [7:0]    io_valueA,
  output     [31:0]   io_valueB,
  output     [47:0]   io_valueC,
  input               clk,
  input               reset
);

  wire       [3:0]    _zz_setA_counter_valueNext;
  wire       [0:0]    _zz_setA_counter_valueNext_1;
  wire       [3:0]    _zz_when_function_l21;
  wire       [7:0]    _zz_when_function_l21_1;
  wire                _zz_when_function_l21_2;
  wire                _zz_when_function_l21_3;
  wire                _zz_when_function_l21_4;
  wire       [0:0]    _zz_when_function_l21_5;
  wire       [2:0]    _zz_when_function_l21_6;
  wire       [0:0]    _zz_when;
  wire       [3:0]    _zz_setB_counter_valueNext;
  wire       [0:0]    _zz_setB_counter_valueNext_1;
  wire       [3:0]    _zz_when_function_l21_1_1;
  wire       [7:0]    _zz_when_function_l21_1_2;
  wire                _zz_when_function_l21_1_3;
  wire                _zz_when_function_l21_1_4;
  wire                _zz_when_function_l21_1_5;
  wire       [0:0]    _zz_when_function_l21_1_6;
  wire       [2:0]    _zz_when_function_l21_1_7;
  wire       [1:0]    _zz_loadB_counter_valueNext;
  wire       [0:0]    _zz_loadB_counter_valueNext_1;
  wire       [3:0]    _zz_setC_counter_valueNext;
  wire       [0:0]    _zz_setC_counter_valueNext_1;
  wire       [3:0]    _zz_when_function_l21_2_1;
  wire       [7:0]    _zz_when_function_l21_2_2;
  wire                _zz_when_function_l21_2_3;
  wire                _zz_when_function_l21_2_4;
  wire                _zz_when_function_l21_2_5;
  wire       [0:0]    _zz_when_function_l21_2_6;
  wire       [2:0]    _zz_when_function_l21_2_7;
  wire       [2:0]    _zz_loadC_counter_valueNext;
  wire       [0:0]    _zz_loadC_counter_valueNext_1;
  reg                 setA_hit;
  reg                 setA_counter_willIncrement;
  reg                 setA_counter_willClear;
  reg        [3:0]    setA_counter_valueNext;
  reg        [3:0]    setA_counter_value;
  wire                setA_counter_willOverflowIfInc;
  wire                setA_counter_willOverflow;
  wire                when_function_l21;
  reg                 loadA_counter_willIncrement;
  wire                loadA_counter_willClear;
  wire                loadA_counter_willOverflowIfInc;
  wire                loadA_counter_willOverflow;
  reg        [7:0]    loadA_data;
  reg                 loadA_hit;
  wire                when_function_l42;
  reg                 setB_hit;
  reg                 setB_counter_willIncrement;
  reg                 setB_counter_willClear;
  reg        [3:0]    setB_counter_valueNext;
  reg        [3:0]    setB_counter_value;
  wire                setB_counter_willOverflowIfInc;
  wire                setB_counter_willOverflow;
  wire                when_function_l21_1;
  reg                 loadB_counter_willIncrement;
  wire                loadB_counter_willClear;
  reg        [1:0]    loadB_counter_valueNext;
  reg        [1:0]    loadB_counter_value;
  wire                loadB_counter_willOverflowIfInc;
  wire                loadB_counter_willOverflow;
  reg        [31:0]   loadB_data;
  reg                 loadB_hit;
  wire                when_function_l42_1;
  wire       [3:0]    _zz_1;
  reg                 setC_hit;
  reg                 setC_counter_willIncrement;
  reg                 setC_counter_willClear;
  reg        [3:0]    setC_counter_valueNext;
  reg        [3:0]    setC_counter_value;
  wire                setC_counter_willOverflowIfInc;
  wire                setC_counter_willOverflow;
  wire                when_function_l21_2;
  reg                 loadC_counter_willIncrement;
  wire                loadC_counter_willClear;
  reg        [2:0]    loadC_counter_valueNext;
  reg        [2:0]    loadC_counter_value;
  wire                loadC_counter_willOverflowIfInc;
  wire                loadC_counter_willOverflow;
  reg        [47:0]   loadC_data;
  reg                 loadC_hit;
  wire                when_function_l42_2;
  wire       [7:0]    _zz_2;

  assign _zz_setA_counter_valueNext_1 = setA_counter_willIncrement;
  assign _zz_setA_counter_valueNext = {3'd0, _zz_setA_counter_valueNext_1};
  assign _zz_when = 1'b1;
  assign _zz_setB_counter_valueNext_1 = setB_counter_willIncrement;
  assign _zz_setB_counter_valueNext = {3'd0, _zz_setB_counter_valueNext_1};
  assign _zz_loadB_counter_valueNext_1 = loadB_counter_willIncrement;
  assign _zz_loadB_counter_valueNext = {1'd0, _zz_loadB_counter_valueNext_1};
  assign _zz_setC_counter_valueNext_1 = setC_counter_willIncrement;
  assign _zz_setC_counter_valueNext = {3'd0, _zz_setC_counter_valueNext_1};
  assign _zz_loadC_counter_valueNext_1 = loadC_counter_willIncrement;
  assign _zz_loadC_counter_valueNext = {2'd0, _zz_loadC_counter_valueNext_1};
  assign _zz_when_function_l21 = 4'b0110;
  assign _zz_when_function_l21_1 = 8'h75;
  assign _zz_when_function_l21_2 = (setA_counter_value == 4'b0101);
  assign _zz_when_function_l21_3 = (io_cmd_payload == 8'h6c);
  assign _zz_when_function_l21_4 = ((setA_counter_value == 4'b0100) && (io_cmd_payload == 8'h61));
  assign _zz_when_function_l21_5 = ((setA_counter_value == 4'b0011) && (io_cmd_payload == 8'h56));
  assign _zz_when_function_l21_6 = {((setA_counter_value == 4'b0010) && (io_cmd_payload == 8'h74)),{((setA_counter_value == 4'b0001) && (io_cmd_payload == 8'h65)),((setA_counter_value == 4'b0000) && (io_cmd_payload == 8'h73))}};
  assign _zz_when_function_l21_1_1 = 4'b0110;
  assign _zz_when_function_l21_1_2 = 8'h75;
  assign _zz_when_function_l21_1_3 = (setB_counter_value == 4'b0101);
  assign _zz_when_function_l21_1_4 = (io_cmd_payload == 8'h6c);
  assign _zz_when_function_l21_1_5 = ((setB_counter_value == 4'b0100) && (io_cmd_payload == 8'h61));
  assign _zz_when_function_l21_1_6 = ((setB_counter_value == 4'b0011) && (io_cmd_payload == 8'h56));
  assign _zz_when_function_l21_1_7 = {((setB_counter_value == 4'b0010) && (io_cmd_payload == 8'h74)),{((setB_counter_value == 4'b0001) && (io_cmd_payload == 8'h65)),((setB_counter_value == 4'b0000) && (io_cmd_payload == 8'h73))}};
  assign _zz_when_function_l21_2_1 = 4'b0110;
  assign _zz_when_function_l21_2_2 = 8'h75;
  assign _zz_when_function_l21_2_3 = (setC_counter_value == 4'b0101);
  assign _zz_when_function_l21_2_4 = (io_cmd_payload == 8'h6c);
  assign _zz_when_function_l21_2_5 = ((setC_counter_value == 4'b0100) && (io_cmd_payload == 8'h61));
  assign _zz_when_function_l21_2_6 = ((setC_counter_value == 4'b0011) && (io_cmd_payload == 8'h56));
  assign _zz_when_function_l21_2_7 = {((setC_counter_value == 4'b0010) && (io_cmd_payload == 8'h74)),{((setC_counter_value == 4'b0001) && (io_cmd_payload == 8'h65)),((setC_counter_value == 4'b0000) && (io_cmd_payload == 8'h73))}};
  always @(*) begin
    setA_hit = 1'b0;
    if(setA_counter_willOverflow) begin
      setA_hit = 1'b1;
    end
  end

  always @(*) begin
    setA_counter_willIncrement = 1'b0;
    if(io_cmd_valid) begin
      if(when_function_l21) begin
        setA_counter_willIncrement = 1'b1;
      end
    end
  end

  always @(*) begin
    setA_counter_willClear = 1'b0;
    if(io_cmd_valid) begin
      if(!when_function_l21) begin
        setA_counter_willClear = 1'b1;
      end
    end
    if(setA_counter_willOverflow) begin
      setA_counter_willClear = 1'b1;
    end
  end

  assign setA_counter_willOverflowIfInc = (setA_counter_value == 4'b1000);
  assign setA_counter_willOverflow = (setA_counter_willOverflowIfInc && setA_counter_willIncrement);
  always @(*) begin
    if(setA_counter_willOverflow) begin
      setA_counter_valueNext = 4'b0000;
    end else begin
      setA_counter_valueNext = (setA_counter_value + _zz_setA_counter_valueNext);
    end
    if(setA_counter_willClear) begin
      setA_counter_valueNext = 4'b0000;
    end
  end

  assign when_function_l21 = ({((setA_counter_value == 4'b1000) && (io_cmd_payload == 8'h41)),{((setA_counter_value == 4'b0111) && (io_cmd_payload == 8'h65)),{((setA_counter_value == _zz_when_function_l21) && (io_cmd_payload == _zz_when_function_l21_1)),{(_zz_when_function_l21_2 && _zz_when_function_l21_3),{_zz_when_function_l21_4,{_zz_when_function_l21_5,_zz_when_function_l21_6}}}}}} != 9'h000);
  always @(*) begin
    loadA_counter_willIncrement = 1'b0;
    if(when_function_l42) begin
      loadA_counter_willIncrement = 1'b1;
    end
  end

  assign loadA_counter_willClear = 1'b0;
  assign loadA_counter_willOverflowIfInc = 1'b1;
  assign loadA_counter_willOverflow = (loadA_counter_willOverflowIfInc && loadA_counter_willIncrement);
  assign when_function_l42 = (loadA_hit && io_cmd_valid);
  assign io_valueA = loadA_data;
  always @(*) begin
    setB_hit = 1'b0;
    if(setB_counter_willOverflow) begin
      setB_hit = 1'b1;
    end
  end

  always @(*) begin
    setB_counter_willIncrement = 1'b0;
    if(io_cmd_valid) begin
      if(when_function_l21_1) begin
        setB_counter_willIncrement = 1'b1;
      end
    end
  end

  always @(*) begin
    setB_counter_willClear = 1'b0;
    if(io_cmd_valid) begin
      if(!when_function_l21_1) begin
        setB_counter_willClear = 1'b1;
      end
    end
    if(setB_counter_willOverflow) begin
      setB_counter_willClear = 1'b1;
    end
  end

  assign setB_counter_willOverflowIfInc = (setB_counter_value == 4'b1000);
  assign setB_counter_willOverflow = (setB_counter_willOverflowIfInc && setB_counter_willIncrement);
  always @(*) begin
    if(setB_counter_willOverflow) begin
      setB_counter_valueNext = 4'b0000;
    end else begin
      setB_counter_valueNext = (setB_counter_value + _zz_setB_counter_valueNext);
    end
    if(setB_counter_willClear) begin
      setB_counter_valueNext = 4'b0000;
    end
  end

  assign when_function_l21_1 = ({((setB_counter_value == 4'b1000) && (io_cmd_payload == 8'h42)),{((setB_counter_value == 4'b0111) && (io_cmd_payload == 8'h65)),{((setB_counter_value == _zz_when_function_l21_1_1) && (io_cmd_payload == _zz_when_function_l21_1_2)),{(_zz_when_function_l21_1_3 && _zz_when_function_l21_1_4),{_zz_when_function_l21_1_5,{_zz_when_function_l21_1_6,_zz_when_function_l21_1_7}}}}}} != 9'h000);
  always @(*) begin
    loadB_counter_willIncrement = 1'b0;
    if(when_function_l42_1) begin
      loadB_counter_willIncrement = 1'b1;
    end
  end

  assign loadB_counter_willClear = 1'b0;
  assign loadB_counter_willOverflowIfInc = (loadB_counter_value == 2'b11);
  assign loadB_counter_willOverflow = (loadB_counter_willOverflowIfInc && loadB_counter_willIncrement);
  always @(*) begin
    loadB_counter_valueNext = (loadB_counter_value + _zz_loadB_counter_valueNext);
    if(loadB_counter_willClear) begin
      loadB_counter_valueNext = 2'b00;
    end
  end

  assign when_function_l42_1 = (loadB_hit && io_cmd_valid);
  assign _zz_1 = ({3'd0,1'b1} <<< loadB_counter_value);
  assign io_valueB = loadB_data;
  always @(*) begin
    setC_hit = 1'b0;
    if(setC_counter_willOverflow) begin
      setC_hit = 1'b1;
    end
  end

  always @(*) begin
    setC_counter_willIncrement = 1'b0;
    if(io_cmd_valid) begin
      if(when_function_l21_2) begin
        setC_counter_willIncrement = 1'b1;
      end
    end
  end

  always @(*) begin
    setC_counter_willClear = 1'b0;
    if(io_cmd_valid) begin
      if(!when_function_l21_2) begin
        setC_counter_willClear = 1'b1;
      end
    end
    if(setC_counter_willOverflow) begin
      setC_counter_willClear = 1'b1;
    end
  end

  assign setC_counter_willOverflowIfInc = (setC_counter_value == 4'b1000);
  assign setC_counter_willOverflow = (setC_counter_willOverflowIfInc && setC_counter_willIncrement);
  always @(*) begin
    if(setC_counter_willOverflow) begin
      setC_counter_valueNext = 4'b0000;
    end else begin
      setC_counter_valueNext = (setC_counter_value + _zz_setC_counter_valueNext);
    end
    if(setC_counter_willClear) begin
      setC_counter_valueNext = 4'b0000;
    end
  end

  assign when_function_l21_2 = ({((setC_counter_value == 4'b1000) && (io_cmd_payload == 8'h43)),{((setC_counter_value == 4'b0111) && (io_cmd_payload == 8'h65)),{((setC_counter_value == _zz_when_function_l21_2_1) && (io_cmd_payload == _zz_when_function_l21_2_2)),{(_zz_when_function_l21_2_3 && _zz_when_function_l21_2_4),{_zz_when_function_l21_2_5,{_zz_when_function_l21_2_6,_zz_when_function_l21_2_7}}}}}} != 9'h000);
  always @(*) begin
    loadC_counter_willIncrement = 1'b0;
    if(when_function_l42_2) begin
      loadC_counter_willIncrement = 1'b1;
    end
  end

  assign loadC_counter_willClear = 1'b0;
  assign loadC_counter_willOverflowIfInc = (loadC_counter_value == 3'b101);
  assign loadC_counter_willOverflow = (loadC_counter_willOverflowIfInc && loadC_counter_willIncrement);
  always @(*) begin
    if(loadC_counter_willOverflow) begin
      loadC_counter_valueNext = 3'b000;
    end else begin
      loadC_counter_valueNext = (loadC_counter_value + _zz_loadC_counter_valueNext);
    end
    if(loadC_counter_willClear) begin
      loadC_counter_valueNext = 3'b000;
    end
  end

  assign when_function_l42_2 = (loadC_hit && io_cmd_valid);
  assign _zz_2 = ({7'd0,1'b1} <<< loadC_counter_value);
  assign io_valueC = loadC_data;
  always @(posedge clk or posedge reset) begin
    if(reset) begin
      setA_counter_value <= 4'b0000;
      loadA_hit <= 1'b0;
      setB_counter_value <= 4'b0000;
      loadB_counter_value <= 2'b00;
      loadB_hit <= 1'b0;
      setC_counter_value <= 4'b0000;
      loadC_counter_value <= 3'b000;
      loadC_hit <= 1'b0;
    end else begin
      setA_counter_value <= setA_counter_valueNext;
      if(setA_hit) begin
        loadA_hit <= 1'b1;
      end
      if(when_function_l42) begin
        if(loadA_counter_willOverflowIfInc) begin
          loadA_hit <= 1'b0;
        end
      end
      setB_counter_value <= setB_counter_valueNext;
      loadB_counter_value <= loadB_counter_valueNext;
      if(setB_hit) begin
        loadB_hit <= 1'b1;
      end
      if(when_function_l42_1) begin
        if(loadB_counter_willOverflowIfInc) begin
          loadB_hit <= 1'b0;
        end
      end
      setC_counter_value <= setC_counter_valueNext;
      loadC_counter_value <= loadC_counter_valueNext;
      if(setC_hit) begin
        loadC_hit <= 1'b1;
      end
      if(when_function_l42_2) begin
        if(loadC_counter_willOverflowIfInc) begin
          loadC_hit <= 1'b0;
        end
      end
    end
  end

  always @(posedge clk) begin
    if(when_function_l42) begin
      if(_zz_when[0]) begin
        loadA_data[7 : 0] <= io_cmd_payload;
      end
    end
    if(when_function_l42_1) begin
      if(_zz_1[0]) begin
        loadB_data[7 : 0] <= io_cmd_payload;
      end
      if(_zz_1[1]) begin
        loadB_data[15 : 8] <= io_cmd_payload;
      end
      if(_zz_1[2]) begin
        loadB_data[23 : 16] <= io_cmd_payload;
      end
      if(_zz_1[3]) begin
        loadB_data[31 : 24] <= io_cmd_payload;
      end
    end
    if(when_function_l42_2) begin
      if(_zz_2[0]) begin
        loadC_data[7 : 0] <= io_cmd_payload;
      end
      if(_zz_2[1]) begin
        loadC_data[15 : 8] <= io_cmd_payload;
      end
      if(_zz_2[2]) begin
        loadC_data[23 : 16] <= io_cmd_payload;
      end
      if(_zz_2[3]) begin
        loadC_data[31 : 24] <= io_cmd_payload;
      end
      if(_zz_2[4]) begin
        loadC_data[39 : 32] <= io_cmd_payload;
      end
      if(_zz_2[5]) begin
        loadC_data[47 : 40] <= io_cmd_payload;
      end
    end
  end


endmodule
