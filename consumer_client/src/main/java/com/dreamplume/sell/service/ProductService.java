package com.dreamplume.sell.service;

import com.dreamplume.sell.configure.MultipartSupportConfig;
import com.dreamplume.sell.dto.CartDTO;
import com.dreamplume.sell.enums.ProductStockEnum;
import com.dreamplume.sell.form.ProductInfoForm;
import com.dreamplume.sell.form.UpdateProductInfoForm;
import com.dreamplume.sell.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Classname ProductService
 * @Description TODO
 * @Date 2022/4/19 23:57
 * @Created by 翊
 */
@FeignClient(value = "nacos-provider-menu", configuration = MultipartSupportConfig.class)
public interface ProductService {

    /**
     * 根据商品类目获取一以下所有的商品（上架 + 下架）
     * @param categoryId 商品类目 ID
     * @param status 商品状态
     * @return 指定商品类目 categoryId 及其状态的以下所有的商品集合
     */
    @GetMapping("/product/get/by/categoryId")
    ResultVO<Object> findByCategoryId(@RequestParam("categoryId") String categoryId,
                                      @RequestParam("status") Integer status);

    // 查看所有的上架菜品
    @GetMapping("/product/get/all/up")
    ResultVO<Object> findAllUp();

    // 查看火热的菜品
    @GetMapping("/product/get/all/host")
    ResultVO<Object> findHost();

    // 根据 ProductId 查找商品详情
    @GetMapping("/product/get/by/id")
    ResultVO<Object> findById(@RequestParam("productId") String productId);

    // 添加菜品
    @PostMapping(value = "/product/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResultVO<Object> add(@RequestParam("productName") String productName,
                         @RequestParam("productPrice") BigDecimal productPrice,
                         @RequestParam("productDescription") String productDescription,
                         @RequestParam("categoryName") String categoryName,
                         @RequestPart("productIcon") MultipartFile productIcon);

    @PostMapping(value = "/product/test", consumes = "multipart/form-data")
    ResultVO<Object> test(@RequestPart("file") MultipartFile file);

    // 上架菜品
    @PutMapping("/product/up")
    ResultVO<Object> up(@RequestParam("productId") String productId);

    // 下架菜品
    @PutMapping("/product/down")
    ResultVO<Object> down(@RequestParam("productId") String productId);

    /**
     * 商家端进行商品的修改
     * @param productId 商品 Id
     * @param updateStock 修改库存数量
     * @param stockEnum 商品修改库存的枚举对象,符号(-1/1) 用于判断是增库存还是减库存
     * @return 修改后的商品数量
     */
    @PutMapping("/product/update/stock/by/id")
    ResultVO<Object> updateProductStock(@RequestParam("productId") String productId,
                                        @RequestParam("updateStock") Integer updateStock,
                                        @RequestParam("stockEnum") ProductStockEnum stockEnum);

    // 查找商品的图片
    @GetMapping("/product/get/icon/by/id")
    ResultVO<Object> findProductIcon(@RequestParam("productId") String productId);

    // 根据商品类目 ID 删除下面的所有的菜品
    @DeleteMapping("/product/delete/by/categoryId")
    ResultVO<Object> deleteByCategoryId(@RequestParam("categoryId") String categoryId);

    // 更新菜品详情
    @PutMapping(value = "/product/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResultVO<Object> updateProductInfo(@RequestParam("productId") String productId,
                                       @RequestParam("productName") String productName,
                                       @RequestParam("productPrice") BigDecimal productPrice,
                                       @RequestParam("productDescription") String productDescription,
                                       @RequestPart("productIcon") MultipartFile productIcon,
                                       @RequestParam("categoryName") String categoryName);

    // 查询所有的商品类目集合
    @GetMapping("/product/category/get/all")
    ResultVO<Object> findAll();

    // 获取所有类目个数
    @GetMapping("/product/category/get/count")
    ResultVO<Object> findCount();

    // 分页查询所有的商品类目集合
    @GetMapping("/product/category/get/all/page")
    ResultVO<Object> findAll(@RequestParam("pageNum") Integer pageNum,
                             @RequestParam("pageSize") Integer pageSize);

    // 根据商品类目 name 寻找商品类目
    @GetMapping("/product/category/get/by/name")
    ResultVO<Object> findOne(@RequestParam("categoryName") String categoryName);

    // 根据商品类目 name 寻找商品类目
    @GetMapping("/product/category/get/name/by/id")
    ResultVO<Object> findNameById(@RequestParam("categoryId") String categoryId);

    // 添加商品类目
    @PostMapping("/product/category/add")
    ResultVO<Object> add(@RequestParam("categoryName") String categoryName);

    // 删除商品类目
    @PutMapping("/product/category/delete/by/id")
    ResultVO<Object> delete(@RequestParam("categoryId") String categoryId);

    // 根绝类目 ID 修改类目名称
    @PutMapping("/product/category/update/by/id")
    ResultVO<Object> update(@RequestParam("categoryId") String categoryId,
                            @RequestParam("categoryName") String categoryName);
}
