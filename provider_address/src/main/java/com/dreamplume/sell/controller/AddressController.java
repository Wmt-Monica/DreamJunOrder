package com.dreamplume.sell.controller;

import com.alibaba.fastjson.JSONObject;
import com.dreamplume.sell.dto.AddressDTO;
import com.dreamplume.sell.entity.Address;
import com.dreamplume.sell.form.AddressForm;
import com.dreamplume.sell.service.AddressService;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname AddressController
 * @Description TODO
 * @Date 2022/5/9 11:26
 * @Created by 翊
 */
@RestController
@RequestMapping("/address")
public class AddressController {

    @Resource
    AddressService addressService;

    /**
     * 根据用户 id 获取所有设置的地址
     * @param userId 用户 Id
     * @return 地址 List 集合
     */
    @GetMapping("/get/all/by/userId")
    public ResultVO<Object> getAllAddressByUserId(@RequestParam("userId") String userId) {
        List<Address> addressList = addressService.getAllAddressByUserId(userId);
        return ResultVOUtil.success(JSONObject.toJSON(addressList));
    }

    /**
     * 返回用户的默认收货地址
     * @param userId 用户 Id
     * @return 默认地址
     */
    @GetMapping("/get/default/by/userId")
    public ResultVO<Object> getDefaultByUserId(@RequestParam("userId") String userId) {
        Address address = addressService.getDefaultByUserId(userId);
        return ResultVOUtil.success(JSONObject.toJSON(address));
    }

    /**
     * 提交表单设置新地址
     * @param addressForm 地址表单
     */
    @PostMapping("/init/default/by/userId")
    public ResultVO<Object> initDefaultAddressByUserId(@RequestBody AddressForm addressForm) {
        addressService.initDefaultAddressByUserId(addressForm);
        return ResultVOUtil.success();
    }

    /**
     * 选择已有的地址列表，更新默认地址
     * @param userId 旧地址编号
     * @param newDefaultAddressId 新地址编号
     */
    @PutMapping("/update/default")
    public ResultVO<Object> updateDefaultAddress(@RequestParam("userId") String userId,
                                                 @RequestParam("newDefaultAddressId") String newDefaultAddressId) {
        addressService.updateDefaultAddress(userId, newDefaultAddressId);
        return ResultVOUtil.success();
    }

    /**
     * 删除地址
     * @param addressId 地址 ID
     */
    @DeleteMapping("/delete/by/id")
    public ResultVO<Object> deleteAddress(@RequestParam("addressId") String addressId) {
        addressService.deleteAddress(addressId);
        return ResultVOUtil.success();
    }

    /**
     * 修改地址
     * @param newAddressDTO 新地址
     */
    @PutMapping("/update")
    public ResultVO<Object> updateAddress(@RequestBody AddressDTO newAddressDTO) {
        addressService.updateAddress(newAddressDTO);
        return ResultVOUtil.success();
    }
}
