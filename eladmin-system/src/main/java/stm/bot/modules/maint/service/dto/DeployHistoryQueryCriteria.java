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
package stm.bot.modules.maint.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import stm.bot.annotation.Query;

import java.sql.Timestamp;
import java.util.List;

/**
* @author zhanghouying
* @date 2025-03-24
*/
@Data
public class DeployHistoryQueryCriteria{

	@ApiModelProperty(value = "模糊查询")
	@Query(blurry = "appName,ip,deployUser")
	private String blurry;

	@Query
	@ApiModelProperty(value = "部署编号")
	private Long deployId;

	@ApiModelProperty(value = "部署时间")
	@Query(type = Query.Type.BETWEEN)
	private List<Timestamp> deployDate;
}
