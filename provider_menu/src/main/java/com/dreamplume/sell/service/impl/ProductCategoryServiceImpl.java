package com.dreamplume.sell.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dreamplume.sell.entity.ProductCategory;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.exception.SellException;
import com.dreamplume.sell.repository.ProductCategoryRepository;
import com.dreamplume.sell.service.ProductCategoryService;
import com.dreamplume.sell.util.generator.IDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname ProductCategoryServiceImpl
 * @Description TODO
 * @Date 2022/4/20 13:48
 * @Created by 翊
 */
@Service
@Slf4j
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Resource
    ProductCategoryRepository productCategoryRepository;

    @Override
    public String findNameById(String categoryId) {
        return productCategoryRepository.selectOne(new QueryWrapper<ProductCategory>().eq("category_id", categoryId)).getCategoryName();
    }

    // 根据商品类目 name 寻找商品类目
    @Override
    public ProductCategory findOne(String categoryName) {
        ProductCategory productCategory = findByName(categoryName);
        if (productCategory == null) {
            log.error("【查找商品类目】未查找到指定的商品类目, categoryName={}", categoryName);
            throw new SellException(SellErrorCode.PRODUCT_CATEGORY_FIND_ONE_FAIL);
        }
        return productCategory;
    }

    // 添加商品类目
    @CacheEvict(cacheNames = "semiPermanent", key = "'allCategory'")
    @Override
    public void add(String categoryName) {
        if (findByName(categoryName) != null) {
            log.error("【添加商品类目】，商品类目名称重复，添加失败，categoryName={}", categoryName);
            throw new SellException(SellErrorCode.PRODUCT_CATEGORY_NAME_REPEAT);
        }
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName(categoryName);
        productCategory.setCategoryId(IDGenerator.getInstance().getId());
        log.info("【添加商品类目】, productCategory={}",productCategory);
        int insert = productCategoryRepository.insert(productCategory);
        log.info("【添加商品类目】insert={}",insert);
        if (insert <= 0) {
            log.error("【添加商品类目】添加商品类目失败，productCategory={}",productCategory);
            throw new SellException(SellErrorCode.PRODUCT_CATEGORY_ADD_FAIL);
        }
    }

    // 删除商品类目
    @CacheEvict(cacheNames = "semiPermanent", key = "'allCategory'")
    @Override
    public void delete(String categoryId) {
        QueryWrapper<ProductCategory> wrapper = new QueryWrapper<ProductCategory>().eq("category_id", categoryId);
        Integer count = productCategoryRepository.selectCount(wrapper);
        log.info("count = {}", count);
        int delete = productCategoryRepository.delete(new QueryWrapper<ProductCategory>().eq("category_id", categoryId));
        if (delete <= 0) {
            log.error("【删除商品类目】productCategoryId={}",categoryId);
            throw new SellException(SellErrorCode.PRODUCT_CATEGORY_DELETE_FAIL);
        }
    }

    // 修改商品类目
    @CacheEvict(cacheNames = "semiPermanent", key = "'allCategory'")
    @Override
    public void update(String categoryId, String categoryName) {
        if (findByName(categoryName) != null) {
            log.error("【修改商品类目】，商品类目名称重复，修改失败，categoryName={}", categoryName);
            throw new SellException(SellErrorCode.PRODUCT_CATEGORY_NAME_REPEAT);
        }
        QueryWrapper<ProductCategory> wrapper = new QueryWrapper<ProductCategory>().eq("category_id", categoryId);
        ProductCategory productCategory = productCategoryRepository.selectOne(wrapper);
        if (productCategory == null) {
            log.error("【修改商品类目】，商品类目查找失败，categoryId={}", categoryId);
            throw new SellException(SellErrorCode.PRODUCT_CATEGORY_FIND_ONE_FAIL);
        }
        productCategory.setCategoryName(categoryName);
        int update = productCategoryRepository.update(productCategory, wrapper);
        if (update <= 0) {
            log.error("【修改商品类目】，商品类目修改失败，categoryId={}", categoryId);
            throw new SellException(SellErrorCode.PRODUCT_CATEGORY_UPDATE_FAIL);
        }
    }

    /**
     * 获取所有的商品类目对象集合
     * Cacheable 开启缓存，如果存在对应 key 的缓存直接返回缓存数据，否则将其返回值放入缓存中
     * key: 键
     *      单纯字符串使用''单引号括起来
     *      使用 #result.name, #arg_name, #p0,p1,p2...,#root.arg[0/1/2/3....]
     * cacheNames：缓存名称，方便指定缓存的失效时间
     * @return 获取所有的商品类目对象集合
     */
    @Cacheable(cacheNames = "semiPermanent", key = "'allCategory'")
    @Override
    public List<ProductCategory> findAll() {
        log.info("redis cache test get all product category");
        return productCategoryRepository.selectList(null);
    }

    @Override
    public Integer findCount() {
        return productCategoryRepository.selectCount(null);
    }

    // 分页查找所有的商品类目集合
    @Override
    public List<ProductCategory> findAll(Integer pageNum, Integer pageSize) {
        return productCategoryRepository.selectPage(new Page<>(pageNum, pageSize), null).getRecords();
    }

    // 根据类目名称获取类目对象
    public ProductCategory findByName(String categoryName) {
        return productCategoryRepository.selectOne(new QueryWrapper<ProductCategory>().eq("category_name", categoryName));
    }
}
