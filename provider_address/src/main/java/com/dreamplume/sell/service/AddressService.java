package com.dreamplume.sell.service;

import com.dreamplume.sell.dto.AddressDTO;
import com.dreamplume.sell.entity.Address;
import com.dreamplume.sell.form.AddressForm;

import java.util.List;

/**
 * @Classname AddressService
 * @Description TODO
 * @Date 2022/5/9 11:14
 * @Created by 翊
 */
public interface AddressService {

    /**
     * 根据用户 id 获取所有设置的地址
     * @param userId 用户 Id
     * @return 地址 List 集合
     */
    List<Address> getAllAddressByUserId(String userId);

    /**
     * 返回用户的默认收货地址
     * @param userId 用户 Id
     * @return 默认地址
     */
    Address getDefaultByUserId(String userId);

    /**
     * 提交表单设置新地址
     * @param addressForm 地址表单
     */
    void initDefaultAddressByUserId(AddressForm addressForm);

    /**
     * 选择已有的地址列表，更新默认地址
     * @param oldDefaultAddressId 旧地址编号
     * @param newDefaultAddressId 新地址编号
     */
    void updateDefaultAddress(String oldDefaultAddressId, String newDefaultAddressId);

    /**
     * 删除地址
     * @param addressId 地址 ID
     */
    void deleteAddress(String addressId);

    /**
     * 修改地址
     * @param newAddressDTO 新地址
     */
    void updateAddress(AddressDTO newAddressDTO);
}
