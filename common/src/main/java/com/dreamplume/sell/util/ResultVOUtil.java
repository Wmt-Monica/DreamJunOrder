package com.dreamplume.sell.util;


import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.vo.ResultVO;
import org.springframework.stereotype.Component;

@Component
public class ResultVOUtil {

    public static ResultVO<Object> success(Object object) {
        ResultVO<Object> resultVO = new ResultVO<>();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO<Object> success() {
        ResultVO<Object> resultVO = new ResultVO<>();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO<Object> error(SellErrorCode errorCode) {
        ResultVO<Object> resultVO = new ResultVO<>();
        resultVO.setCode(errorCode.getCode());
        resultVO.setMsg(errorCode.getMessage());
        return resultVO;
    }

}
