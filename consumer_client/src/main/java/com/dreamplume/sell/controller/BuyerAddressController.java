package com.dreamplume.sell.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dreamplume.sell.dto.AddressDTO;
import com.dreamplume.sell.entity.Address;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.form.AddressForm;
import com.dreamplume.sell.service.AddressService;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname BuyerAddressController
 * @Description TODO
 * @Date 2022/5/9 11:26
 * @Created by 翊
 */
@RestController
@RequestMapping("/buyer/address")
@Slf4j
public class BuyerAddressController {

    @Resource
    AddressService addressService;

    // 获取指定用户的所有的收货地址
    @GetMapping("/list")
    public ResultVO<Object> getUserAllAddress(@RequestParam("userId") String userId) {
        ResultVO<Object> getAllAddressByUserIdResultVo = addressService.getAllAddressByUserId(userId);
        if (!getAllAddressByUserIdResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(getAllAddressByUserIdResultVo.getCode()));
        }
        List<Address> allAddressByUserId = JSON.parseArray(JSONArray.toJSONString(getAllAddressByUserIdResultVo.getData()), Address.class);
        Map<String, Object> map = new HashMap<>();
        map.put("addressList", allAddressByUserId);
        return ResultVOUtil.success(JSONObject.toJSON(map));
    }

    // 根据用户名获取默认地址
    @GetMapping("/default")
    public ResultVO<Object> getDefaultAddress(@RequestParam("userId") String useId) {
        ResultVO<Object> getDefaultByUserIdResultVo = addressService.getDefaultByUserId(useId);
        if (!getDefaultByUserIdResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(getDefaultByUserIdResultVo.getCode()));
        }
        Address defaultAddress = JSON.parseObject(JSON.toJSONString(getDefaultByUserIdResultVo.getData()), Address.class);
        log.info("defaultAddress={}", defaultAddress);
        if (ObjectUtils.isEmpty(defaultAddress)) {
            return ResultVOUtil.error(SellErrorCode.ADDRESS_DEFAULT_IS_EMPTY);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("defaultAddress", defaultAddress);
        return ResultVOUtil.success(JSONObject.toJSON(map));
    }

    // 提交地址表单创建新的地址
    @PostMapping("/create")
    public ResultVO<Object> initDefaultAddressByUserId(@RequestBody AddressForm addressForm) {
        ResultVO<Object> initDefaultAddressByUserIdResultVo = addressService.initDefaultAddressByUserId(addressForm);
        if (!initDefaultAddressByUserIdResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(initDefaultAddressByUserIdResultVo.getCode()));
        }
        return ResultVOUtil.success();
    }

    // 更新用户的默认地址
    @PutMapping("/update/default")
    public ResultVO<Object> updateDefaultAddress(@RequestParam("userId") String userId,
                                                 @RequestParam("newDefaultAddressId") String newDefaultAddressId) {
        ResultVO<Object> updateDefaultAddressResultVo = addressService.updateDefaultAddress(userId, newDefaultAddressId);
        if (!updateDefaultAddressResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(updateDefaultAddressResultVo.getCode()));
        }
        return ResultVOUtil.success();
    }

    // 删除地址
    @DeleteMapping("/delete")
    public ResultVO<Object> deleteAddress(@RequestParam("addressId") String addressId) {
        ResultVO<Object> deleteAddressResultVo = addressService.deleteAddress(addressId);
        if (!deleteAddressResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(deleteAddressResultVo.getCode()));
        }
        return ResultVOUtil.success();
    }

    // 修改地址
    @PutMapping("/update")
    public ResultVO<Object> updateAddress(@RequestBody AddressDTO newAddressDTO) {
        ResultVO<Object> updateAddressResultVo = addressService.updateAddress(newAddressDTO);
        if (updateAddressResultVo.getCode().equals(SellErrorCode.SUCCESS.getCode())) {
            return ResultVOUtil.error(SellErrorCode.get(updateAddressResultVo.getCode()));
        }
        return ResultVOUtil.success();
    }
}
