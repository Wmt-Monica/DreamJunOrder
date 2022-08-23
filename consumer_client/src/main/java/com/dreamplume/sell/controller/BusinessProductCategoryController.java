package com.dreamplume.sell.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dreamplume.sell.converter.ProductCategoryToProductCategoryVOConverter;
import com.dreamplume.sell.entity.ProductCategory;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.service.*;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.ProductCategoryVO;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname BusinessProductCategoryController
 * @Description TODO
 * @Date 2022/4/21 21:37
 * @Created by 翊
 */
@RestController
@RequestMapping("/business/product/category")
@Slf4j
public class BusinessProductCategoryController {

    @Resource
    ProductService productService;

    // 分页查找所有的商品类目集合
    @GetMapping("/list/page")
    public ResultVO<Object> list(@RequestParam("pageNum") Integer pageNum,
                                 @RequestParam("pageSize") Integer pageSize) {
        ResultVO<Object> findAllResultVo = productService.findAll(pageNum, pageSize);
        if (!findAllResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(findAllResultVo.getCode()));
        }
        log.info("findAllResultVo.code={}", findAllResultVo.getCode());
        List<ProductCategory> allProductCategory = JSON.parseArray(JSONArray.toJSONString(findAllResultVo.getData()), ProductCategory.class);
        log.info("allProductCategory.size={}", allProductCategory.size());
        List<ProductCategoryVO> productCategoryVOList = ProductCategoryToProductCategoryVOConverter.converter(allProductCategory);
        Map<String, Object> map = new HashMap<>();
        map.put("productCategoryList", productCategoryVOList);
        ResultVO<Object> findCountResultVo = productService.findCount();
        log.info("findCountResultVo={}", findCountResultVo);
        if (!findCountResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(findCountResultVo.getCode()));
        }
        map.put("productCategoryCount", JSON.parseObject(JSON.toJSONString(findCountResultVo.getData()), Integer.class));
        return ResultVOUtil.success(JSONObject.toJSON(map));
    }

    // 添加商品类目
    @PostMapping("/add")
    public ResultVO<Object> add(@RequestParam("productCategoryName") String productCategoryName) {
        log.info("productCategoryName={}",productCategoryName);
        ResultVO<Object> addResultVo = productService.add(productCategoryName);
        if (addResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(addResultVo.getCode()));
        }
        return ResultVOUtil.success();
    }

    // 删除商品类目
    @PutMapping("/delete")
    @Transactional
    public ResultVO<Object> delete(@RequestParam(value = "productCategoryId") String productCategoryId) {
        log.info("【商家删除类目】productCategoryId={}",productCategoryId);
        // 删除所有该类目下的所有菜品
        ResultVO<Object> deleteByCategoryIdResultVo = productService.deleteByCategoryId(productCategoryId);
        if (!deleteByCategoryIdResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(deleteByCategoryIdResultVo.getCode()));
        }
        return ResultVOUtil.success();
    }

    // 修改商品类目
    @PutMapping("/update")
    public ResultVO<Object> update(@RequestParam("categoryId") String categoryId,
                                   @RequestParam("categoryName") String categoryName) {
        ResultVO<Object> updateResultVo = productService.update(categoryId, categoryName);
        if (updateResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(updateResultVo.getCode()));
        }
        return ResultVOUtil.success();
    }

}
