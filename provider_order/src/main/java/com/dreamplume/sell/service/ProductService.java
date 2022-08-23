package com.dreamplume.sell.service;

import com.dreamplume.sell.configure.MultipartSupportConfig;
import com.dreamplume.sell.dto.CartDTO;
import com.dreamplume.sell.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Classname ProductService
 * @Description TODO
 * @Date 2022/7/30 3:52
 * @Created by 翊
 */
@FeignClient(value = "nacos-provider-menu", configuration = MultipartSupportConfig.class)
public interface ProductService {

    // 根据 ProductId 查找商品详情
    @GetMapping("/product/get/by/id")
    ResultVO<Object> findById(@RequestParam("productId") String productId);

    // 订单生成：减库存
    @PutMapping("/product/decrease/stock")
    ResultVO<Object> decreaseStock(@RequestParam("cartDTOListString") String cartDTOListString);

    // 订单取消：加库存
    @PutMapping("/product/increase/stock")
    ResultVO<Object> increaseStock(@RequestParam("cartDTOListString") String cartDTOListString);

    // 查找商品的图片
    @GetMapping("/product/get/icon/by/id")
    ResultVO<Object> findProductIcon(@RequestParam("productId") String productId);
}
