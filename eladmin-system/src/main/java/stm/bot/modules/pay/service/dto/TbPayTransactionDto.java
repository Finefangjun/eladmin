/*
*  Copyright 2019-2025 zj
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package stm.bot.modules.pay.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;
import io.swagger.annotations.ApiModelProperty;

/**
* @website https://awss.vip.com
* @description /
* @author admin
* @date 2025-04-18
**/
@Data
public class TbPayTransactionDto implements Serializable {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户类型")
    private String userType;

    @ApiModelProperty(value = "pay_method_id")
    private Integer payMid;

    @ApiModelProperty(value = "payCode")
    private String payCode;

    @ApiModelProperty(value = "通道")
    private String channelId;

    @ApiModelProperty(value = "商户号")
    private String merchantId;

    @ApiModelProperty(value = "sys单号")
    private String systemNo;

    @ApiModelProperty(value = "pay下单号")
    private String orderNo;

    @ApiModelProperty(value = "pay订单号")
    private String payOrderNo;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "实际金额")
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "费率%")
    private BigDecimal fee;

    @ApiModelProperty(value = "回调ip")
    private String ip;

    @ApiModelProperty(value = "回调url")
    private String notifyUrl;

    @ApiModelProperty(value = "支付时间")
    private Timestamp payTime;

    @ApiModelProperty(value = "状态")
    private Integer payStatus;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "创建日期")
    private Timestamp createTime;

    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;
}