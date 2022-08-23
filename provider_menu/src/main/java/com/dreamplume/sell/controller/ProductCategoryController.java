package com.dreamplume.sell.controller;

import com.alibaba.fastjson.JSONObject;
import com.dreamplume.sell.entity.ProductCategory;
import com.dreamplume.sell.service.ProductCategoryService;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname ProductCategoryController
 * @Description TODO
 * @Date 2022/7/30 4:16
 * @Created by 翊
 */
@RestController
@RequestMapping("/product/category")
@Slf4j
public class ProductCategoryController {

    @Resource
    ProductCategoryService productCategoryService;

    // 查询所有的商品类目集合
    @GetMapping("/get/all")
    public ResultVO<Object> findAll() {
        List<ProductCategory> all = productCategoryService.findAll();
        return ResultVOUtil.success(JSONObject.toJSON(all));
    }

    // 获取所有类目个数
    @GetMapping("/get/count")
    public ResultVO<Object> findCount() {
        Integer count = productCategoryService.findCount();
        return ResultVOUtil.success(JSONObject.toJSON(count));
    }

    // 分页查询所有的商品类目集合
    @GetMapping("/get/all/page")
    public ResultVO<Object> findAll(@RequestParam("pageNum") Integer pageNum,
                                    @RequestParam("pageSize") Integer pageSize) {
        List<ProductCategory> all = productCategoryService.findAll(pageNum, pageSize);
        log.info("all={}", all.size());
        return ResultVOUtil.success(JSONObject.toJSON(all));
    }

    // 根据商品类目 name 寻找商品类目
    @GetMapping("/get/by/name")
    public ResultVO<Object> findOne(@RequestParam("categoryName") String categoryName) {
        ProductCategory one = productCategoryService.findOne(categoryName);
        return ResultVOUtil.success(JSONObject.toJSON(one));
    }

    // 根据商品类目 name 寻找商品类目
    @GetMapping("/get/name/by/id")
    public ResultVO<Object> findNameById(@RequestParam("categoryId") String categoryId) {
        String productCategoryName = productCategoryService.findNameById(categoryId);
        return ResultVOUtil.success(JSONObject.toJSON(productCategoryName));
    }

    // 添加商品类目
    @PostMapping("/add")
    public ResultVO<Object> add(@RequestParam("categoryName") String categoryName) {
        productCategoryService.add(categoryName);
        return ResultVOUtil.success();
    }

    // 删除商品类目
    @PutMapping("/delete/by/id")
    public ResultVO<Object> delete(@RequestParam("categoryId") String categoryId) {
        productCategoryService.delete(categoryId);
        return ResultVOUtil.success();
    }

    // 根绝类目 ID 修改类目名称
    @PutMapping("/update/by/id")
    public ResultVO<Object> update(@RequestParam("categoryId") String categoryId,
                                   @RequestParam("categoryName") String categoryName) {
        productCategoryService.update(categoryId, categoryName);
        return ResultVOUtil.success();
    }

}
