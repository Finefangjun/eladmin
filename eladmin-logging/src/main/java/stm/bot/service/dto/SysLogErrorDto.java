/*
 *  Copyright 2025-2030 zj
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
package stm.bot.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author zj
* @date 2025-03-22
*/
@Data
public class SysLogErrorDto implements Serializable {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "方法")
    private String method;

    @ApiModelProperty(value = "参数")
    private String params;

    @ApiModelProperty(value = "浏览器")
    private String browser;

    @ApiModelProperty(value = "请求ip")
    private String requestIp;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;
}