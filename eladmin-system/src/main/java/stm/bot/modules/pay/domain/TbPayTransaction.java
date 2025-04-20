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
package stm.bot.modules.pay.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import stm.bot.base.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author admin
 * @website https://awss.vip.com
 * @description /
 * @date 2025-04-18
 **/
@Entity
@Data
@Table(name = "tb_pay_transaction")
public class TbPayTransaction extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Integer id;

    @Column(name = "`username`")
    @ApiModelProperty(value = "用户名")
    private String username;

    @Column(name = "`user_type`")
    @ApiModelProperty(value = "用户类型")
    private String userType;

    @Column(name = "`pay_mid`")
    @ApiModelProperty(value = "pay_method_id")
    private Integer payMid;

    @Column(name = "`pay_code`")
    @ApiModelProperty(value = "payCode")
    private String payCode;

    @Column(name = "`channel_id`")
    @ApiModelProperty(value = "通道")
    private String channelId;

    @Column(name = "`merchant_id`")
    @ApiModelProperty(value = "商户号")
    private String merchantId;

    @Column(name = "`system_no`")
    @ApiModelProperty(value = "sys单号")
    private String systemNo;

    @Column(name = "`order_no`")
    @ApiModelProperty(value = "pay下单号")
    private String orderNo;

    @Column(name = "`pay_order_no`")
    @ApiModelProperty(value = "pay订单号")
    private String payOrderNo;

    @Column(name = "`amount`")
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @Column(name = "`actual_amount`")
    @ApiModelProperty(value = "实际金额")
    private BigDecimal actualAmount;

    @Column(name = "`fee`")
    @ApiModelProperty(value = "费率%")
    private BigDecimal fee;

    @Column(name = "`ip`")
    @ApiModelProperty(value = "回调ip")
    private String ip;

    @Column(name = "`notify_url`")
    @ApiModelProperty(value = "回调url")
    private String notifyUrl;

    @Column(name = "`pay_time`")
    @ApiModelProperty(value = "支付时间")
    private Timestamp payTime;

    @Column(name = "`pay_status`")
    @ApiModelProperty(value = "状态")
    private Integer payStatus;

    @Column(name = "`remark`")
    @ApiModelProperty(value = "备注")
    private String remark;

    public void copy(TbPayTransaction source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
