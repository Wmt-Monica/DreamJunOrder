package com.dreamplume.sell.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dreamplume.sell.configure.PictureConfig;
import com.dreamplume.sell.dto.CartDTO;
import com.dreamplume.sell.entity.ProductCategory;
import com.dreamplume.sell.entity.ProductInfo;
import com.dreamplume.sell.enums.ProductStatusEnum;
import com.dreamplume.sell.enums.ProductStockEnum;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.exception.SellException;
import com.dreamplume.sell.form.ProductInfoForm;
import com.dreamplume.sell.form.UpdateProductInfoForm;
import com.dreamplume.sell.repository.ProductInfoRepository;
import com.dreamplume.sell.service.ProductCategoryService;
import com.dreamplume.sell.service.ProductService;
import com.dreamplume.sell.util.PictureUnit;
import com.dreamplume.sell.util.generator.IDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Classname ProductServiceImpl
 * @Description TODO
 * @Date 2022/4/20 9:59
 * @Created by 翊
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Resource
    ProductInfoRepository productInfoRepository;

    @Resource
    ProductCategoryService productCategoryService;

    // 查看所有上架的菜品
    @Override
    public List<ProductInfo> findAllUp() {
        return findAllByState(ProductStatusEnum.UP.getCode());
    }

    // 根据菜品状态返回所有的菜品集合
    public List<ProductInfo> findAllByState(Integer productState) {
        return productInfoRepository.selectList(new QueryWrapper<ProductInfo>()
                .eq("product_status", productState));
    }

    // 查看火热的菜品
    @Override
    public List<ProductInfo> findHost(Integer productCount) {
        List<ProductInfo> productInfoList = findAllUp();
        productInfoList = productInfoList.stream().sorted((o1, o2) -> (int) (o2.getProductSales() - o1.getProductSales())).collect(Collectors.toList());
        productInfoList = productInfoList.subList(0, 5);
        return productInfoList;
    }

    // 根据商品 id 返回商品详情
    @Override
    public ProductInfo findById(String productId) {
        ProductInfo productInfo = productInfoRepository
                .selectOne(new QueryWrapper<ProductInfo>()
                .eq("product_id", productId));
        if (productInfo == null) {
            log.error("【查找商品信息】未查询到指定 id 的商品, productId={}",productId);
            throw new SellException(SellErrorCode.PRODUCT_NOT_EXIST);
        }
        return productInfo;
    }

    // 订单生成减库存
    @Override
    public void decreaseStock(List<CartDTO> cartDTOList) {
        log.info("cartDTOList={}", cartDTOList);
        for (CartDTO cartDTO : cartDTOList) {
            ProductInfo productInfo = findById(cartDTO.getProductId());
            log.info("productInfo={}", productInfo);
            // 购物车列表中的商品 Id 不存在
            if (ObjectUtils.isEmpty(productInfo)) {
                throw new SellException(SellErrorCode.PRODUCT_NOT_EXIST);
            }
            // 新库存
            int newStock = productInfo.getProductStock() - cartDTO.getProductQuantity();
            log.info("newStock={}", newStock);
            // 如果库存不足
            if (newStock < 0) {
                throw new SellException(SellErrorCode.PRODUCT_STOCK_ERROR);
            }
            // 新售量
            Long newSales = productInfo.getProductSales() + cartDTO.getProductQuantity();
            log.info("newSales={}", newSales);

            productInfo.setProductStock(newStock);
            productInfo.setProductSales(newSales);

            // 修改商品信息（库存，售量）
            QueryWrapper<ProductInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("product_id", cartDTO.getProductId());
            int update = productInfoRepository.update(productInfo, wrapper);
            log.info("update={}", update);
            if (update <= 0) {
                log.error("【商品减库存】商品减库存失败");
                throw new SellException(SellErrorCode.PRODUCT_STOCK_DECREASE_FAIL);
            }
        }
    }

    // 订单取消加库存
    @Override
    public void increaseStock(List<CartDTO> cartDTOList) {
        log.info("【cartDTOList:取消订单】cartDTOList:{}", cartDTOList);
        for (CartDTO cartDTO: cartDTOList) {
            ProductInfo productInfo = productInfoRepository.selectOne(new QueryWrapper<ProductInfo>().eq("product_id", cartDTO.getProductId()));
            if (productInfo == null) {
                throw new SellException(SellErrorCode.PRODUCT_NOT_EXIST);
            }
            Integer result = productInfo.getProductStock() + cartDTO.getProductQuantity();
            productInfo.setProductStock(result);
            productInfo.setProductSales(productInfo.getProductSales() - cartDTO.getProductQuantity());
            productInfoRepository.update(productInfo, new QueryWrapper<ProductInfo>().eq("product_id", cartDTO.getProductId()));
        }
    }

    // 根据商品 ID 获取商品图片外链
    @Override
    public String findProductIcon(String productId) {
        ProductInfo productInfo = productInfoRepository.selectOne(new QueryWrapper<ProductInfo>().eq("product_id", productId));
        if (productInfo == null) {
            log.error("【商品图片查询】未查询到指定 ID 的商品, productId={}", productId);
            throw new SellException(SellErrorCode.PRODUCT_NOT_EXIST);
        }
        return productInfo.getProductIcon();
    }

    // 根据商品类目 ID 删除下面的所有的菜品
    @Override
    @Transactional
    public List<ProductInfo> deleteByCategoryId(String categoryId) {
        // 获取指定类目 ID 下的所有商品详情
        List<ProductInfo> productInfoList = productInfoRepository.selectList(new QueryWrapper<ProductInfo>().eq("category_id", categoryId));
        log.info("【根据类目删除菜品】productInfoList={}", productInfoList);
        // 获取商品详情 List 集合中的商品 ID 集合
        List<String> productIdList = productInfoList.stream().map(ProductInfo::getProductId).collect(Collectors.toList());
        log.info("【根据类目删除菜品】productIdList={}", productIdList);
        if (productIdList.size() == 0) return productInfoList;
        // 删除商品 ID 集合中的所有的商品
        int delete = productInfoRepository.delete(new QueryWrapper<ProductInfo>().in("product_id", productIdList));
        if (delete < productIdList.size()) {
            log.error("【根据类目删除菜品】删除失败");
            throw new SellException(SellErrorCode.PRODUCT_CATEGORY_DELETE_FAIL);
        }
        // 从图床中删除这些菜品图片
        List<String> pictureFileList = productInfoList.stream().map(ProductInfo::getPictureFile).collect(Collectors.toList());
        for (String pictureFile : pictureFileList) {
            PictureUnit.deleteImage(pictureFile);
        }
        // 删除商品类目
        productCategoryService.delete(categoryId);
        return productInfoList;
    }

    // --------------------------------------------------------------

    // 根据商品类目及其 status 状态获取以下所有的商品, unless 满足条件不缓存
    @Cacheable(cacheNames = "semiPermanent", key = " 'productInfoList_' + #categoryId + '_' + #status", unless = "#result.size() == 0")
    @Override
    public List<ProductInfo> findByCategoryId(String categoryId, Integer status) {
        log.info("redis cache test get all product category = " + categoryId + " , status = " + status);
        return productInfoRepository.selectList(
                new QueryWrapper<ProductInfo>()
                        .eq("category_id", categoryId)
                        .eq("product_status", status));
    }

    // 上架菜品:使用 cacheEvict 将 redis 缓存中对应类目的菜品缓存删除
    @Caching(evict = {
            @CacheEvict(cacheNames = "semiPermanent", key = " 'productInfoList_' + #result.categoryId + '_' + '0'"),
            @CacheEvict(cacheNames = "semiPermanent", key = " 'productInfoList_' + #result.categoryId + '_' + '1'")
    })
    @Override
    public ProductInfo up(String productId) {
        ProductInfo productInfo = findById(productId);
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        int update = productInfoRepository.update(productInfo, new QueryWrapper<ProductInfo>().eq("product_id", productId));
        if (update <= 0) {
            log.error("【菜品上架】上架失败, productId={}", productId);
            throw new SellException(SellErrorCode.PRODUCT_UP_FAIL);
        }
        return productInfo;
    }

    // 下架菜品:使用 cacheEvict 将 redis 缓存中对应类目的菜品缓存删除
    @Caching(evict = {
            @CacheEvict(cacheNames = "semiPermanent", key = " 'productInfoList_' + #result.categoryId + '_' + '0'"),
            @CacheEvict(cacheNames = "semiPermanent", key = " 'productInfoList_' + #result.categoryId + '_' + '1'")
    })
    @Override
    public ProductInfo down(String productId) {
        ProductInfo productInfo = findById(productId);
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        int update = productInfoRepository.update(productInfo, new QueryWrapper<ProductInfo>().eq("product_id", productId));
        if (update <= 0) {
            log.error("【菜品上架】上架失败, productId={}", productId);
        }
        return productInfo;
    }

    // 添加新菜品
    @Transactional
    @CacheEvict(cacheNames = "semiPermanent", key = " 'productInfoList_' + #result.categoryId + '_' + '0'")
    @Override
    public ProductInfo add(ProductInfoForm productInfoForm) {
        // 根据表单中类目的名称获取类目对象,对其进行判空
        log.info("categoryName={}", productInfoForm.getCategoryName());
        ProductCategory category = productCategoryService.findOne(productInfoForm.getCategoryName());
        if (ObjectUtils.isEmpty(category)) {
            log.error("【添加菜品】未查找到新菜品的类目，添加失败，categoryName={}", productInfoForm.getCategoryName());
            throw new SellException(SellErrorCode.PRODUCT_CATEGORY_FIND_ONE_FAIL);
        }
        log.info("【添加新菜品】,categoryName={}, categoryId={}", category.getCategoryName(), category.getCategoryId());
        // 根据新增菜品表单中的信息填充 productInfo 菜品对象中的成员属性
        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(productInfoForm, productInfo);
        productInfo.setCategoryId(category.getCategoryId());
        productInfo.setProductId(IDGenerator.getInstance().getId());

        // 将添加商品的图片文件上传到图传中,并且填充 productInfo菜品对象图片的文件在图床中的名称及其外链 URL 信息
        Map<String, String> imageInfo = PictureUnit.storage(productInfoForm.getProductIcon());
        productInfo.setPictureFile(imageInfo.get(PictureConfig.PICTURE_FILE_NAME));
        productInfo.setProductIcon(imageInfo.get(PictureConfig.PICTURE_URL));

        // 将 productInfo 菜品对象插入 mysql 数据库中,如果数据插入失败抛出异常
        if (productInfoRepository.insert(productInfo) <= 0) {
            log.error("【添加菜品】添加菜品失败, productName={}", productInfoForm.getProductName());
            throw new SellException(SellErrorCode.PRODUCT_ADD_FAIL);
        }
        return productInfo;
    }

    /**
     * 商家端进行商品的修改
     * @param productId 商品 Id
     * @param updateStock 修改库存数量
     * @param stockEnum 商品修改库存的枚举对象,符号(-1/1) 用于判断是增库存还是减库存
     * @return 修改后的商品数量
     */
    @CacheEvict(cacheNames = "semiPermanent", key = " 'productInfoList_' + #result.categoryId + '_' + '0'")
    @Override
    public ProductInfo updateProductStock(String productId, Integer updateStock, ProductStockEnum stockEnum) {
        ProductInfo productInfo = findById(productId);
        // 如果是减库存并且当前商品的库存小于减去的库存的数量,那么就抛出数据操作异常
        if (stockEnum.getCode().equals(ProductStockEnum.DECREASE.getCode()) && productInfo.getProductStock() < updateStock) {
            log.error("【商家减库存】修改库存失败，productId={}, updateStock={}", productInfo.getProductId(), updateStock);
            throw new SellException(SellErrorCode.PRODUCT_STOCK_DECREASE_FAIL);
        }
        productInfo.setProductStock(productInfo.getProductStock() + stockEnum.getCode() * updateStock);
        int update = productInfoRepository.update(productInfo,
                new QueryWrapper<ProductInfo>()
                        .eq("product_id", productInfo.getProductId()));
        if (update <= 0) {
            log.error("【商家加库存】修改存失败，productId={}, updateStock={}, stockEnum.code={}", productInfo.getProductId(), updateStock, stockEnum.getCode());
            throw new SellException(SellErrorCode.PRODUCT_STOCK_UPDATE_FAIL);
        }
        return productInfo;
    }

    // 更新菜品
    @Override
    @CacheEvict(cacheNames = "semiPermanent", key = " 'productInfoList_' + #result.categoryId + '_' + '0'")
    public ProductInfo updateProductInfo(UpdateProductInfoForm form) {
        // 根据表单获取修改指定的类目对象,然后对其进行判空
        ProductCategory category = productCategoryService.findOne(form.getCategoryName());
        if (ObjectUtils.isEmpty(category)) {
            log.error("【修改菜品】未查找到类目，修改失败，categoryName={}", form.getCategoryName());
            throw new SellException(SellErrorCode.PRODUCT_CATEGORY_FIND_ONE_FAIL);
        }
        // 根据表单中的 productInfo 菜品对象的 ID 获取菜品对象,并且对其进行判空
        QueryWrapper<ProductInfo> wrapper = new QueryWrapper<ProductInfo>().eq("product_id", form.getProductId());
        ProductInfo productInfo = productInfoRepository.selectOne(wrapper);
        if (ObjectUtils.isEmpty(productInfo)) {
            log.error("【修改菜品】未查找到该菜品，修改失败，productId={}", form.getProductId());
            throw new SellException(SellErrorCode.PRODUCT_NOT_EXIST);
        }
        BeanUtils.copyProperties(form, productInfo);
        PictureUnit.deleteImage(productInfo.getPictureFile());
        // 将表单中最新的图片上传到图床上
        Map<String, String> pictureInfo = PictureUnit.storage(form.getProductIcon());
        // 将 productInfo 的图片信息附属到成员属性中
        productInfo.setProductIcon(pictureInfo.get(PictureConfig.PICTURE_URL));
        productInfo.setPictureFile(pictureInfo.get(PictureConfig.PICTURE_FILE_NAME));
        log.info("【修改菜品】productInfo={}",productInfo);
        // 将最新的 ProductInfo 对象修改到 mysql 中
        if (productInfoRepository.update(productInfo, wrapper) <= 0) {
            log.error("【修改菜品】修改失败 productInfo={}",productInfo);
            throw new SellException(SellErrorCode.PRODUCT_UPDATE_FAIL);
        }
        return productInfo;
    }
}
