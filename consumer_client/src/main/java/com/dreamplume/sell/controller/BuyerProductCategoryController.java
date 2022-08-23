package com.dreamplume.sell.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dreamplume.sell.entity.ProductCategory;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.service.ProductService;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.ResultVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname BuyerProductCategoryController
 * @Description TODO
 * @Date 2022/7/26 10:21
 * @Created by 翊
 */
@RestController
@RequestMapping("/buyer/product/category")
public class BuyerProductCategoryController {

    @Resource
    ProductService productService;

    // 获取所有的商品类目
    @GetMapping("/list/all")
    public ResultVO<Object> findAllCapacity() {
        ResultVO<Object> findAllResultVo = productService.findAll();
        if (!findAllResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(findAllResultVo.getCode()));
        }
        List<ProductCategory> allCategory = JSON.parseArray(JSONArray.toJSONString(findAllResultVo.getData()), ProductCategory.class);
        return ResultVOUtil.success(JSONObject.toJSON(allCategory));
    }

}
