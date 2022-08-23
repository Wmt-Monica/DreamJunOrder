package com.dreamplume.sell.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Classname User
 * @Description TODO
 * @Date 2022/4/19 21:43
 * @Created by 翊
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    /** 用户 id */
    @TableId
    private String userId;

    /** 用户名称 */
    private String userName;

    /** 用户密码 */
    private String password;

    /** 用户角色 id */
    private Integer roleId;

    /** 用户头像 */
    private String headPortrait;

    /** 用户邮箱 **/
    private String email;

    /** 用户创建时间 */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 用户注销标识 */
    private Integer logout;

    /** 用户头像图片文件名称，用于删除文件使用 */
    private String pictureFile;

}
