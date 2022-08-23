package com.dreamplume.sell.enums;

import lombok.Getter;
import org.apache.log4j.spi.ErrorCode;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum SellErrorCode implements CodeEnum, ErrorCode {

    UNKNOWN(9999,"系统内部发生未知异常"),

    SUCCESS(0, "成功"),
//
    PARAM_ERROR(1, "参数不正确"),
//
//    // USER
//
    USER_NO_LOGON(1001, "未登录用户拦截"),
//
    USER_TOKEN_TIMEOUT(1002, "用户 token 过期，请重新登录"),
//
    USER_UPDATE_OLD_PASSWORD_ERROR(1003, "用户更新，原密码错误，更新失败"),
//
    USER_UPDATE_NAME_REPLICATION(1004, "用户更新名称重复"),
//
    USER_NAME_UPDATE_FAIL(1005, "用户名更新失败"),
//
//    USER_NAME_UPDATE_SUCCESS(1006, "用户名更新成功"),
//
    USER_LOGOFF_FAIL(1007, "用户注销失败"),
//
//    USER_LOGOFF_SUCCESS(1008, "用户注销成功"),
//
//    USER_LOGON_FAIL(1010, "用户登录失败"),
//
//    USER_LOGON_SUCCESS(1012, "用户登录成功"),
//
    USER_LOGIN_FAIL(1013, "用户登录失败, 用户名或密码错误"),
//
    USER_NAME_REPEAT(1014, "用户名重复"),
//
//    USER_REGISTER_SUCCESS(1015, "用户注册成功"),
//
    USER_REGISTER_FAIL(1016, "用户注册失败"),
//
    USER_NOT_FOUND(1017, "用户不存在"),
//
    USER_UPDATE_PASSWORD_FAIL(1018, "用户更新登录密码失败"),
//
    USER_UPDATE_HEAD_PORTRAIT_FAIL(1019, "用户更新头像失败"),
//
//    USER_DISPLAY_HEAD_PORTRAIT_FAIL(1020, "用户显示图片失败"),
//
    USER_REGISTER_EMAIL_REPEAT(1021,"用户注册邮箱已被注册"),
//
    USER_TOKEN_INVALID(1022, "用户 token 过期，请重新登录"),
//
    USER_LOGOUT_FAIL(1023, "用户退出登录失败"),
//
//    USER_LOGOUT_SUCCESS(1024, "用户退出登录成功"),
//
//
//    // EMAIL
//
    EMAIL_SEND_FAIL(2001,"邮箱发送失败"),
//
    EMAIL_REPLICATION_SEND(2002, "邮箱时间范围内未使用，请勿重复发送"),
//
//    // VERIFICATION_CODE
//
    VERIFICATION_CODE_EXPIRED(3001, "验证码过期，请重新发送"),
//
    VERIFICATION_CODE_ERROR(3002, "验证码错误，请重新输入"),
//
//    // ROLE
//
    ROLE_NULL(4001, "用户角色未查找到"),
//
//    // CART
//
    CART_EMPTY(5001, "购物车为空"),
//
//    // PRODUCT
//
    PRODUCT_UP_FAIL(6001, "商品上架失败"),
//
//    PRODUCT_UP_SUCCESS(6002, "商品上架成功"),
//
    PRODUCT_NOT_EXIST(6003, "商品不存在"),
//
    PRODUCT_STOCK_ERROR(6004, "商品库存不正确"),
//
//    PRODUCT_STATUS_ERROR(6005, "商品状态不正确"),
//
//    PRODUCT_DOWN_FAIL(6006, "商品下架失败"),
//
//    PRODUCT_DOWN_SUCCESS(6007, "商品下架成功"),
//
//    PRODUCT_ADD_SUCCESS(6008, "菜品添加成功"),
//
    PRODUCT_ADD_FAIL(6009,"商品添加失败"),
//
//    PRODUCT_STOCK_INCREASE_SUCCESS(6010, "商品库存添加成功"),
//
//    PRODUCT_STOCK_INCREASE_FAIL(6011, "商品库存添加失败"),
//
//    PRODUCT_STOCK_DECREASE_SUCCESS(6012, "商品库存减少成功"),
//
    PRODUCT_STOCK_DECREASE_FAIL(6013, "商品库存减少失败"),
//
    PRODUCT_UPDATE_FAIL(6014, "商品详情修改失败"),
//
    PRODUCT_STOCK_UPDATE_FAIL(6015,"商品库存修改失败"),
//
//    // ORDER
//
//    ORDER_FINISH_SUCCESS(7001, "订单完结成功"),
//
//    ORDER_FINISH_FAIL(7002, "订单完结失败"),
//
//    ORDER_CANCEL_FAIL(7003, "订单取消失败"),
//
//    ORDER_CANCEL_SUCCESS(7004, "订单取消成功"),
//
//    ORDER_PAY_FAIL(7005, "订单支付失败"),
//
//    ORDER_PAY_SUCCESS(7006, "订单支付成功"),
//
    ORDER_UPDATE_FAIL(7008, "订单更新失败"),
//
//    ORDER_UPDATE_SUCCESS(7009, "订单更新成功"),
//
    ORDER_PAY_STATUS_ERROR(7010, "订单支付状态不正确"),
//
    ORDER_AMOUNT(7011, "订单金额无效"),
//
    ORDER_STATUS_ERROR(7012, "订单状态不正确"),
//
    ORDER_NOT_EXIST(7013, "订单不存在"),
//
    ORDER_NULL(7014, "订单为空"),
//
    ORDER_OWNER_ERROR(7015, "该订单不属于当前用户"),
//
//    ORDER_CANCEL_MONEY_BACK_FAIL(7016, "取消订单，金额返回失败"),
//
    ORDER_PAY_STATE_UPDATE_FAIL(7017, "订单支付状态更新失败"),
//
    ORDER_STATE_UPDATE_FAIL(7018, "订单状态更新失败"),
//
//    // WALLET
//
    WALLET_NOT_ENOUGH(8001, "买家余额不足"),
//
    WALLET_NOT_FIND(8002,"未查询到指定用户的钱包"),
//
    WALLET_RECHARGE_FAIL(8003, "用户充值失败"),
//
//    WALLET_RECHARGE_SUCCESS(8004, "用户充值成功"),
//
    WALLET_WITHDRAWAL_FAIL(8005, "用户提现失败"),
//
//    WALLET_WITHDRAWAL_SUCCESS(8006, "用户提现成功"),
//
    WALLET_SET_PASSWORD_FAIL(8007, "用户设置钱包密码失败"),
//
    WALLET_CREATE_FAIL(8009, "用户创建钱包失败"),
//
    WALLET_PAY_PASSWORD_ERROR(8010, "买家支付密码输入错误"),
//
    WALLET_OLD_PASSWORD_ERROR(8011, "钱包重置支付密码，原密码输入错误"),
//
    WALLET_UPDATE_PAY_PASSWORD_FAIL(8012, "用户更新钱包支付密码失败"),
//
    WALLET_PAY_FAIL(8013,"钱包支付失败"),
//
    WALLET_BACK_FAIL(8014,"钱包退款返还失败"),
//
//    // PRODUCT_CATEGORY
//
    PRODUCT_CATEGORY_ADD_FAIL(9001, "商品类目添加失败"),
//
//    PRODUCT_CATEGORY_ADD_SUCCESS(9002, "商品类目添加成功"),
//
    PRODUCT_CATEGORY_DELETE_FAIL(9003, "商品类目删除失败"),
//
//    PRODUCT_CATEGORY_DELETE_SUCCESS(9004, "商品类目删除成功"),
//
    PRODUCT_CATEGORY_UPDATE_FAIL(9005, "商品类目修改失败"),
//
//    PRODUCT_CATEGORY_UPDATE_SUCCESS(9006, "商品类目修改成功"),
//
    PRODUCT_CATEGORY_FIND_ONE_FAIL(9007, "商品类目查找失败"),
//
    PRODUCT_CATEGORY_NAME_REPEAT(9008,"商品类目名称重复，添加失败"),
//
//
//    // PICTURE
//    PICTURE_DISPLAY_FAIL(10001, "图片显示失败"),
//
    PICTURE_UP_LOAD_FAIL(10002, "图片上传失败"),
//
    PICTURE_DELETE_FAIL(10003,"图片删除失败"),
//
//    PICTURE_FILE_NOT_FOUNT(10004, "图片文件未查询到"),
//
    PICTURE_UPLOAD_TIMES_LIMIT(10005, "添加图片至图床次数上限"),
//
    PICTURE_ANALYSIS_FIAL(10006, "图片文件解析失败"),
//
//
//    // ADDRESS
    ADDRESS_IS_EMPTY(11001, "用户收货地址列表为空"),
//
    ADDRESS_DEFAULT_IS_EMPTY(11002, "用户默认收货地址为空"),
//
    ADDRESS_DEFAULT_ADD_FAIL(11003,"添加地址失败"),
//
    ADDRESS_DEFAULT_UPDATE_FAIL(11004,"更新默认地址失败"),
//
//    ADDRESS_DEFAULT_DELETE_FAIL(11005, "删除默认地址失败"),
//
    ADDRESS_DELETE_FAIL(11006, "删除默认地址失败"),
//
    ADDRESS_NOT_FOUND(11007, "地址未找到"),
//
    ADDRESS_UPDATE_FAIL(11008, "地址修改失败"),
//
//    // OrderDetail
    ORDER_DETAIL_ADD_FAIL(12001, "订单详情添加失败"),
//
//
//    // APP
    APP_DOWN_FAIL(13001, "软件下载失败"),

    METHOD_IS_NOT_SUPPORT(14001, "请求方法不被支持")

    ;

    private Integer code;

    private String message;

    SellErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    // 将所有的结果返回枚举存储在 Map 集合中，便于之后根据 code 返回指定的 SellResultCode
    private static Map<Integer, SellErrorCode> cache = new HashMap<>();
    static {
        for (SellErrorCode resultCode : SellErrorCode.values()) {
            cache.put(resultCode.getCode(), resultCode);
        }
    }

    public static SellErrorCode get(Integer code) {
        return cache.get(code);
    }
}
