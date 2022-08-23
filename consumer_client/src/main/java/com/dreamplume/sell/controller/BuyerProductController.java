package com.dreamplume.sell.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dreamplume.sell.entity.ProductInfo;
import com.dreamplume.sell.enums.ProductStatusEnum;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.service.ProductService;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.ProductInfoVO;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 买家商品控制层
 * @Classname ProductController
 * @Description TODO
 * @Date 2022/4/20 10:47
 * @Created by 翊
 */
@RestController
@RequestMapping("/buyer/product")
@Slf4j
public class BuyerProductController {

    @Resource
    ProductService productService;

    // 根据菜品类目 ID 获取所有的菜品集合
    @GetMapping("/list/all/by/categoryId")
    public ResultVO<Object> findAllProductByCategoryId(@RequestParam("categoryId") String categoryId) {
        ResultVO<Object> findUpByCategoryIdResultVo = productService.findByCategoryId(categoryId, ProductStatusEnum.UP.getCode());
        if (!findUpByCategoryIdResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(findUpByCategoryIdResultVo.getCode()));
        }
        List<ProductInfo> productInfoList = JSON.parseArray(JSONArray.toJSONString(findUpByCategoryIdResultVo.getData()), ProductInfo.class);
        ResultVO<Object> findDownByCategoryIdResultVo = productService.findByCategoryId(categoryId, ProductStatusEnum.DOWN.getCode());
        if (!findDownByCategoryIdResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(findDownByCategoryIdResultVo.getCode()));
        }
        productInfoList.addAll(JSON.parseArray(JSONArray.toJSONString(findDownByCategoryIdResultVo.getData()), ProductInfo.class));
        return ResultVOUtil.success(JSONObject.toJSON(productInfoList));
    }

    // 按照商品售量展示商品
    @GetMapping("/list/page/host")
    public ResultVO<Object> findHostPage() {
        ResultVO<Object> findHostResultVo = productService.findHost();
        if (!findHostResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(findHostResultVo.getCode()));
        }
        List<ProductInfo> productInfoList = JSON.parseArray(JSONArray.toJSONString(findHostResultVo.getData()), ProductInfo.class);
        List<ProductInfoVO> productInfoVOList = new ArrayList<>();
        for (ProductInfo productInfo : productInfoList) {
            ProductInfoVO productInfoVO = new ProductInfoVO();
            BeanUtils.copyProperties(productInfo, productInfoVO);
            ResultVO<Object> findNameByIdResultVo = productService.findNameById(productInfo.getCategoryId());
            if (!findNameByIdResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
                return ResultVOUtil.error(SellErrorCode.get(findNameByIdResultVo.getCode()));
            }
            productInfoVO.setCategoryName(JSON.parseObject(JSON.toJSONString(findNameByIdResultVo.getData()), String.class));
            productInfoVOList.add(productInfoVO);
        }
        return ResultVOUtil.success(JSONObject.toJSON(productInfoVOList));
    }
}
