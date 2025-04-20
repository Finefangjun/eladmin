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

/**
 * @author admin
 * @website https://awss.vip.com
 * @description /
 * @date 2025-04-18
 **/
@Entity
@Data
@Table(name = "tz_pay_method")
public class TzPayMethod extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Integer id;

    @Column(name = "`method_id`")
    @ApiModelProperty(value = "payid")
    private String methodId;

    @Column(name = "`method_name`")
    @ApiModelProperty(value = "pay名称")
    private String methodName;

    @Column(name = "`pay_code`")
    @ApiModelProperty(value = "payCode")
    private String payCode;

    @Column(name = "`channel_id`")
    @ApiModelProperty(value = "通道")
    private String channelId;

    @Column(name = "`sys_merchant`")
    @ApiModelProperty(value = "sys商户")
    private String sysMerchant;

    @Column(name = "`merchant_id`")
    @ApiModelProperty(value = "商户号")
    private String merchantId;

    @Column(name = "`public_key`")
    @ApiModelProperty(value = "publicKey")
    private String publicKey;

    @Column(name = "`private_key`")
    @ApiModelProperty(value = "privateKey")
    private String privateKey;

    @Column(name = "`system_key`")
    @ApiModelProperty(value = "systemKey")
    private String systemKey;

    @Column(name = "`api_url`")
    @ApiModelProperty(value = "请求url")
    private String apiUrl;

    @Column(name = "`return_url`")
    @ApiModelProperty(value = "同步url")
    private String returnUrl;

    @Column(name = "`notify_url`")
    @ApiModelProperty(value = "回调url")
    private String notifyUrl;

    @Column(name = "`class_addres`")
    @ApiModelProperty(value = "执行器")
    private String classAddres;

    @Column(name = "`rate`")
    @ApiModelProperty(value = "费率%")
    private BigDecimal rate;

    @Column(name = "`pay_title`")
    @ApiModelProperty(value = "标题")
    private String payTitle;

    @Column(name = "`pay_content`")
    @ApiModelProperty(value = "内容")
    private String payContent;

    @Column(name = "`status`")
    @ApiModelProperty(value = "状态")
    private Integer status;

    @Column(name = "`remark`")
    @ApiModelProperty(value = "备注")
    private String remark;

    public void copy(TzPayMethod source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
