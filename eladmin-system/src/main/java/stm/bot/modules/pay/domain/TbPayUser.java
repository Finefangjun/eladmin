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

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import stm.bot.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://awss.vip.com
* @description /
* @author admin
* @date 2025-04-20
**/
@Entity
@Data
@Table(name="tb_pay_user")
public class TbPayUser extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "主键")
    private Integer id;

    @Column(name = "`user_name`")
    @ApiModelProperty(value = "用户名")
    private String userName;

    @Column(name = "`real_name`")
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @Column(name = "`mobile`")
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @Column(name = "`sys_merchant`")
    @ApiModelProperty(value = "sys商户")
    private String sysMerchant;

    @Column(name = "`system_key`")
    @ApiModelProperty(value = "systemKey")
    private String systemKey;

    public void copy(TbPayUser source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
