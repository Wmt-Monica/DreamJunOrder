package com.dreamplume.sell.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dreamplume.sell.dto.AddressDTO;
import com.dreamplume.sell.entity.Address;
import com.dreamplume.sell.enums.AddressStatusEnum;
import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.exception.SellException;
import com.dreamplume.sell.form.AddressForm;
import com.dreamplume.sell.repository.AddressRepository;
import com.dreamplume.sell.service.AddressService;
import com.dreamplume.sell.util.generator.IDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname AddressServiceImpl
 * @Description TODO
 * @Date 2022/5/9 11:15
 * @Created by 翊
 */
@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    @Resource
    AddressRepository addressRepository;

    /**
     * 根据用户名称获取所有设置的地址
     * @param userId 用户 Id
     * @return 地址 List 集合
     */
    @Override
    public List<Address> getAllAddressByUserId(String userId) {
        List<Address> address = addressRepository.selectList(new QueryWrapper<Address>().eq("user_id", userId));
        if (address.size() == 0) {
            log.info("【收货地址】用户收货地址列表为空, userId={}", userId);
            throw new SellException(SellErrorCode.ADDRESS_IS_EMPTY);
        }
        return address;
    }

    /**
     * 返回用户的默认收货地址
     * @param userId 用户 Id
     * @return 默认地址
     */
    @Override
    public Address getDefaultByUserId(String userId) {
        Address address = addressRepository
                .selectOne(new QueryWrapper<Address>()
                        .eq("user_id", userId)
                        .eq("is_default", AddressStatusEnum.DEFAULT.getCode()));
        if (ObjectUtils.isEmpty(address)) {
            log.info("【收货地址】用户默认收货地址为空, userId={}", userId);
        }
        return address;
    }

    /**
     * 提交表单创建新地址
     * @param addressForm 地址表单
     */
    @Override
    @Transactional
    public void initDefaultAddressByUserId(AddressForm addressForm) {
        log.info("【创建地址】addressForm={}", addressForm);
        Address insertAddress = new Address();
        BeanUtils.copyProperties(addressForm, insertAddress);
        insertAddress.setAddressId(IDGenerator.getInstance().getId());

        // 如果表单中设置的地址为默认地址，则更改初始创建的地址 isDefault 属性值
        if (addressForm.getIsDefault().equals(AddressStatusEnum.DEFAULT.getState())) {
            insertAddress.setIsDefault(AddressStatusEnum.DEFAULT.getCode());
            // 如果该用户之前存在默认的地址，那么旧修改旧的默认的地址的地址状态：非默认地址
            Address oldDefaultAddress = getDefaultByUserId(addressForm.getUserId());
            if (!ObjectUtils.isEmpty(oldDefaultAddress)) {
                oldDefaultAddress.setIsDefault(AddressStatusEnum.NOT_DEFAULT.getCode());
                int update = addressRepository.update(oldDefaultAddress, new QueryWrapper<Address>().eq("address_id", oldDefaultAddress.getAddressId()));
                if (update <= 0) {
                    log.error("【创建地址】添加新地址失败");
                    throw new SellException(SellErrorCode.ADDRESS_DEFAULT_ADD_FAIL);
                }
            }
        }
        log.info("【创建地址】：initDefaultAddress={}", insertAddress);
        int insert = addressRepository.insert(insertAddress);
        if (insert <= 0) {
            log.error("【创建地址】添加新地址失败");
            throw new SellException(SellErrorCode.ADDRESS_DEFAULT_ADD_FAIL);
        }
    }

    /**
     * 选择已有的地址列表，更新默认地址
     * @param userId 用户 ID
     * @param newDefaultAddressId 新地址编号
     */
    @Override
    @Transactional
    public void updateDefaultAddress(String userId, String newDefaultAddressId) {
        QueryWrapper<Address> byUserGetDefaultWrapper = new QueryWrapper<Address>().eq("user_id", userId).eq("is_default", AddressStatusEnum.DEFAULT.getCode());
        QueryWrapper<Address> newAddressWrapper = new QueryWrapper<Address>().eq("address_id", newDefaultAddressId);
        // 更新旧默认地址
        Address oldDefaultAddress = addressRepository.selectOne(byUserGetDefaultWrapper);
        if (!ObjectUtils.isEmpty(oldDefaultAddress)) {
            oldDefaultAddress.setIsDefault(AddressStatusEnum.NOT_DEFAULT.getCode());
            int updateOldDefaultAddress = addressRepository.update(oldDefaultAddress, byUserGetDefaultWrapper);
            if (updateOldDefaultAddress <= 0) throw new SellException(SellErrorCode.ADDRESS_DEFAULT_UPDATE_FAIL);
        }
        // 更新新默认地址
        Address newDefaultAddress = addressRepository.selectOne(newAddressWrapper);
        newDefaultAddress.setIsDefault(AddressStatusEnum.DEFAULT.getCode());
        int updateNew = addressRepository.update(newDefaultAddress, newAddressWrapper);

        if (updateNew <= 0) {
            log.error("【更新默认地址】更新失败");
            throw new SellException(SellErrorCode.ADDRESS_DEFAULT_UPDATE_FAIL);
        }
    }

    /**
     * 删除地址
     * @param addressId 地址 ID
     */
    @Override
    public void deleteAddress(String addressId) {
        int delete = addressRepository.delete(new QueryWrapper<Address>().eq("address_id", addressId));
        if (delete <= 0) {
            log.error("【删除地址】地址删除失败, addressId={}", addressId);
            throw new SellException(SellErrorCode.ADDRESS_DELETE_FAIL);
        }
    }

    /**
     * 修改地址
     * @param newAddressDTO 新地址传输对象
     */
    @Override
    @Transactional
    public void updateAddress(AddressDTO newAddressDTO) {
        Address newAddress = new Address();
        BeanUtils.copyProperties(newAddressDTO, newAddress);
        QueryWrapper<Address> wrapper = new QueryWrapper<Address>().eq("address_id", newAddress.getAddressId());
        if (ObjectUtils.isEmpty(addressRepository.selectOne(wrapper))) {
            log.error("【修改地址】旧地址不存在，addressId={}", newAddress.getAddressId());
            throw new SellException(SellErrorCode.ADDRESS_NOT_FOUND);
        }
        // 更新地址
        int update = addressRepository.update(newAddress, wrapper);
        if (update <= 0) {
            log.error("【修改地址】地址修改失败");
            throw new SellException(SellErrorCode.ADDRESS_UPDATE_FAIL);
        }

        // 如果更新的新地址设置为默认地址那么进行用户默认地址的替换
        if (newAddressDTO.getIsDefault().equals(AddressStatusEnum.DEFAULT.getState())) {
            updateDefaultAddress(newAddress.getUserId(), newAddress.getAddressId());
        }
    }
}
