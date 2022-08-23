package com.dreamplume.sell.service;

import com.dreamplume.sell.configure.MultipartSupportConfig;
import com.dreamplume.sell.dto.AddressDTO;
import com.dreamplume.sell.form.AddressForm;
import com.dreamplume.sell.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname AddressService
 * @Description TODO
 * @Date 2022/5/9 11:14
 * @Created by 翊
 */
@FeignClient(value = "nacos-provider-address", configuration = MultipartSupportConfig.class)
public interface AddressService {

    // 根据用户 id 获取所有设置的地址
    @GetMapping("/address/get/all/by/userId")
    ResultVO<Object> getAllAddressByUserId(@RequestParam("userId") String userId);

    // 返回用户的默认收货地址
    @GetMapping("/address/get/default/by/userId")
    ResultVO<Object> getDefaultByUserId(@RequestParam("userId") String userId);

    // 提交表单设置新地址
    @PostMapping("/address/init/default/by/userId")
    ResultVO<Object> initDefaultAddressByUserId(@RequestBody AddressForm addressForm);

    // 选择已有的地址列表，更新默认地址
    @PutMapping("/address/update/default")
    ResultVO<Object> updateDefaultAddress(@RequestParam("userId") String userId,
                                          @RequestParam("newDefaultAddressId") String newDefaultAddressId);

    // 删除地址
    @DeleteMapping("/address/delete/by/id")
    ResultVO<Object> deleteAddress(@RequestParam("addressId") String addressId);

    // 修改地址
    @PutMapping("/address/update")
    ResultVO<Object> updateAddress(@RequestBody AddressDTO newAddressDTO);
}
