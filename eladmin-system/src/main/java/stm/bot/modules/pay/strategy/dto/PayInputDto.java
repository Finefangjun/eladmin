package stm.bot.modules.pay.strategy.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayInputDto implements Serializable {
    private static final long serialVersionUID = -8603137754741083602L;

    //版本
    String version;
    //商户号
    String merchantNo;
    //订单号
    String orderNo;
    //订单号
    String systemNo;
    //金额 分
    String amount;
    //创建时间 yyyyMMddHHmmss
    String createTime;
    //订单ip
    String createIp;
    //支付方式
    String payCode;
    //回调地址
    String notifyUrl;
    //同步地址
    String returnUrl;
    //sign加密类型
    String signType;
    String sign;
    //用户名
    String userId;
    //手机号
    String mobile;
}
