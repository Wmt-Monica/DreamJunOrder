package com.dreamplume.sell.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dreamplume.sell.dto.CartDTO;
import com.dreamplume.sell.entity.ProductInfo;
import com.dreamplume.sell.enums.ProductStockEnum;
import com.dreamplume.sell.form.ProductInfoForm;
import com.dreamplume.sell.form.UpdateProductInfoForm;
import com.dreamplume.sell.service.ProductService;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.ProductInfoVO;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Classname ProductInfoController
 * @Description TODO
 * @Date 2022/7/30 4:16
 * @Created by 翊
 */
@RestController
@RequestMapping("/product")
@Slf4j
public class ProductInfoController {

    @Resource
    ProductService productService;

    /**
     * 根据商品类目获取一以下所有的商品（上架 + 下架）
     * @param categoryId 商品类目 ID
     * @param status     商品状态
     * @return 指定商品类目 categoryId 及其状态的以下所有的商品集合
     */
    @GetMapping("/get/by/categoryId")
    public ResultVO<Object> findByCategoryId(@RequestParam("categoryId") String categoryId,
                                             @RequestParam("status") Integer status) {
        List<ProductInfo> productInfoList = productService.findByCategoryId(categoryId, status);
        return ResultVOUtil.success(JSONObject.toJSON(productInfoList));
    }

    // 查看所有的上架菜品
    @GetMapping("/get/all/up")
    public ResultVO<Object> findAllUp() {
        List<ProductInfo> allUp = productService.findAllUp();
        return ResultVOUtil.success(JSONObject.toJSON(allUp));
    }

    // 查看火热的菜品
    @GetMapping("/get/all/host")
    public ResultVO<Object> findHost(@RequestParam(value = "productCount", required = false, defaultValue = "5") Integer productCount) {
        List<ProductInfo> hostList = productService.findHost(productCount);
        return ResultVOUtil.success(JSONObject.toJSON(hostList));
    }

    // 根据 ProductId 查找商品详情
    @GetMapping("/get/by/id")
    public ResultVO<Object> findById(@RequestParam("productId") String productId) {
        ProductInfo productInfo = productService.findById(productId);
        return ResultVOUtil.success(JSONObject.toJSON(productInfo));
    }

    // 订单生成：减库存
    @PutMapping("/decrease/stock")
    public ResultVO<Object> decreaseStock(@RequestParam("cartDTOListString") String cartDTOListString) {
        log.info("cartDTOListString={}", cartDTOListString);
        List<CartDTO> cartDTOList = JSON.parseArray(JSON.toJSON(cartDTOListString).toString(), CartDTO.class);
        log.info("cartDTOList={}", cartDTOList);
        productService.decreaseStock(cartDTOList);
        return ResultVOUtil.success();
    }

    // 订单取消：加库存
    @PutMapping("/increase/stock")
    public ResultVO<Object> increaseStock(@RequestParam("cartDTOListString") String cartDTOListString) {
        List<CartDTO> cartDTOList = JSON.parseArray(JSON.toJSON(cartDTOListString).toString(), CartDTO.class);
        log.info("cartDTOList={}", cartDTOList);
        productService.increaseStock(cartDTOList);
        return ResultVOUtil.success();
    }

    // 添加菜品
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultVO<Object> add(@RequestParam("productName") String productName,
                                @RequestParam("productPrice") BigDecimal productPrice,
                                @RequestParam("productDescription") String productDescription,
                                @RequestParam("categoryName") String categoryName,
                                @RequestPart("productIcon") MultipartFile productIcon) {
        ProductInfoForm productInfoForm = new ProductInfoForm(productName, productPrice, productDescription, productIcon, categoryName);
        ProductInfo productInfo = productService.add(productInfoForm);
        return ResultVOUtil.success(JSONObject.toJSON(productInfo));
    }

    @PostMapping(value = "/test", consumes = "multipart/form-data")
    public ResultVO<Object> test(@RequestPart("file") MultipartFile file) {
        log.info("fileName={}",file.getName());
        return ResultVOUtil.success();
    }

    // 上架菜品
    @PutMapping("/up")
    public ResultVO<Object> up(@RequestParam("productId") String productId) {
        productService.up(productId);
        return ResultVOUtil.success();
    }

    // 下架菜品
    @PutMapping("/down")
    public ResultVO<Object> down(@RequestParam("productId") String productId) {
        productService.down(productId);
        return ResultVOUtil.success();
    }

    /**
     * 商家端进行商品的修改库存
     * @param productId   商品 Id
     * @param updateStock 修改库存数量
     * @param stockEnum   商品修改库存的枚举对象,符号(-1/1) 用于判断是增库存还是减库存
     * @return 修改后的商品数量
     */
    @PutMapping("/update/stock/by/id")
    public ResultVO<Object> updateProductStock(@RequestParam("productId") String productId,
                                               @RequestParam("updateStock") Integer updateStock,
                                               @RequestParam("stockEnum") ProductStockEnum stockEnum) {
        ProductInfo productInfo = productService.updateProductStock(productId, updateStock, stockEnum);
        return ResultVOUtil.success(JSONObject.toJSON(productInfo));
    }

    // 查找商品的图片
    @GetMapping("/get/icon/by/id")
    public ResultVO<Object> findProductIcon(@RequestParam("productId") String productId) {
        String productIcon = productService.findProductIcon(productId);
        log.info("productIcon={}", productIcon);
        return ResultVOUtil.success(JSONObject.toJSON(productIcon));
    }

    // 根据商品类目 ID 删除下面的所有的菜品
    @DeleteMapping("/delete/by/categoryId")
    public ResultVO<Object> deleteByCategoryId(@RequestParam("categoryId") String categoryId) {
        List<ProductInfo> productInfoList = productService.deleteByCategoryId(categoryId);
        return ResultVOUtil.success(JSONObject.toJSON(productInfoList));
    }

    // 更新菜品详情
    @PutMapping("/update")
    public ResultVO<Object> updateProductInfo(@RequestParam("productId") String productId,
                                              @RequestParam("productName") String productName,
                                              @RequestParam("productPrice") BigDecimal productPrice,
                                              @RequestParam("productDescription") String productDescription,
                                              @RequestPart("productIcon") MultipartFile productIcon,
                                              @RequestParam("categoryName") String categoryName) {
        UpdateProductInfoForm form = new UpdateProductInfoForm(productId, productName, productPrice, productDescription, productIcon, categoryName);
        ProductInfo productInfo = productService.updateProductInfo(form);
        return ResultVOUtil.success(JSONObject.toJSON(productInfo));
    }
}
