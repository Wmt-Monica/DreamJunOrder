package com.dreamplume.sell.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Classname Role
 * @Description TODO
 * @Date 2022/4/19 21:43
 * @Created by 翊
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {

    /** 角色 id */
    @TableId
    private Integer roleId;

    /** 角色名称 */
    private String roleName;
}
