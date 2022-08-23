package com.dreamplume.sell.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dreamplume.sell.entity.ProductInfo;
import com.dreamplume.sell.enums.ProductStockEnum;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.form.ProductInfoForm;
import com.dreamplume.sell.form.UpdateProductInfoForm;
import com.dreamplume.sell.service.ProductService;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname BusinessProductController
 * @Description TODO
 * @Date 2022/4/20 15:11
 * @Created by 翊
 */
@RequestMapping("/business/product")
@RestController
@Slf4j
public class BusinessProductController {

    @Resource
    ProductService productService;

    // 根据菜品类目 ID 及其菜品状态获取所有的菜品列表
    @GetMapping("/list/by/category/status")
    public ResultVO<Object> getAllProductByCategoryIdAndStatus(@RequestParam("categoryId") String categoryId,
                                                               @RequestParam("status") Integer status) {
        ResultVO<Object> findByCategoryIdResultVo = productService.findByCategoryId(categoryId, status);
        if (!findByCategoryIdResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(findByCategoryIdResultVo.getCode()));
        }
        List<ProductInfo> productInfoList = JSON.parseArray(JSONArray.toJSONString(findByCategoryIdResultVo.getData()), ProductInfo.class);
        return ResultVOUtil.success(JSONObject.toJSON(productInfoList));
    }

    // 上架菜品
    @PutMapping("/up")
    public ResultVO<Object> upProduct(@RequestParam("productId") String productId) {
        ResultVO<Object> upResultVo = productService.up(productId);
        if (!upResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(upResultVo.getCode()));
        }
        return ResultVOUtil.success();
    }

    // 下架菜品
    @PutMapping("/down")
    public ResultVO<Object> downProduct(@RequestParam("productId") String productId) {
        ResultVO<Object> downResultVo = productService.down(productId);
        if (!downResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(downResultVo.getCode()));
        }
        return ResultVOUtil.success();
    }

    @PostMapping(value = "/test", consumes = "multipart/form-data")
    public ResultVO<Object> test(@RequestPart("file") MultipartFile file) {
        return productService.test(file);
    }

    // 添加商品
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultVO<Object> add(@RequestParam("productName") String productName,
                                @RequestParam("productPrice") BigDecimal productPrice,
                                @RequestParam("productDescription") String productDescription,
                                @RequestParam("categoryName") String categoryName,
                                @RequestPart("productIcon") MultipartFile productIcon) {
        ResultVO<Object> addResultVo = productService.add(productName, productPrice, productDescription, categoryName, productIcon);
        log.info("test");
        if (!addResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(addResultVo.getCode()));
        }
        ProductInfo productInfo = JSON.parseObject(JSON.toJSONString(addResultVo.getData()), ProductInfo.class);
        return ResultVOUtil.success(JSONObject.toJSON(productInfo));
    }

    // 加库存
    @PutMapping("/stock/increase")
    public ResultVO<Object> increaseProductStock(@RequestParam("productId") String productId,
                                                 @RequestParam("increaseStock") Integer increaseStock) {
        ResultVO<Object> updateProductStockResultVo = productService.updateProductStock(productId, increaseStock, ProductStockEnum.INCREASE);
        if (!updateProductStockResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(updateProductStockResultVo.getCode()));
        }
        ProductInfo productInfo = JSON.parseObject(JSON.toJSONString(updateProductStockResultVo.getData()), ProductInfo.class);
        int newStock = productInfo.getProductStock();
        return ResultVOUtil.success(JSONObject.toJSON(newStock));
    }

    // 减库存
    @PutMapping("/stock/decrease")
    public ResultVO<Object> decreaseProductStock(@RequestParam("productId") String productId,
                                                 @RequestParam("decreaseStock") Integer decreaseStock) {
        ResultVO<Object> updateProductStockResultVo = productService.updateProductStock(productId, decreaseStock, ProductStockEnum.DECREASE);
        if (!updateProductStockResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(updateProductStockResultVo.getCode()));
        }
        ProductInfo productInfo = JSON.parseObject(JSON.toJSONString(updateProductStockResultVo.getData()), ProductInfo.class);
        int newStock = productInfo.getProductStock();
        return ResultVOUtil.success(JSONObject.toJSON(newStock));
    }

    // 更新菜品详情信息
    @PutMapping("/update")
    public ResultVO<Object> updateProductInfo(@RequestParam("productId") String productId,
                                              @RequestParam("productName") String productName,
                                              @RequestParam("productPrice") BigDecimal productPrice,
                                              @RequestParam("productDescription") String productDescription,
                                              @RequestPart("productIcon") MultipartFile productIcon,
                                              @RequestParam("categoryName") String categoryName) {
        ResultVO<Object> updateProductInfoResultVo = productService.updateProductInfo(productId, productName, productPrice, productDescription, productIcon, categoryName);
        if (!updateProductInfoResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(updateProductInfoResultVo.getCode()));
        }
        ProductInfo newProductInfo = JSON.parseObject(JSON.toJSONString(updateProductInfoResultVo.getData()), ProductInfo.class);
        Map<String, Object> map = new HashMap<>();
        map.put("product", newProductInfo);
        return ResultVOUtil.success(JSONObject.toJSON(map));
    }
}
