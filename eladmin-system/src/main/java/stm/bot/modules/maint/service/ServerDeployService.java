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
package stm.bot.modules.maint.service;

import stm.bot.modules.maint.domain.ServerDeploy;
import stm.bot.modules.maint.service.dto.ServerDeployDto;
import stm.bot.modules.maint.service.dto.ServerDeployQueryCriteria;
import stm.bot.utils.PageResult;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
* @author zhanghouying
* @date 2025-03-24
*/
public interface ServerDeployService {

    /**
     * 分页查询
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    PageResult<ServerDeployDto> queryAll(ServerDeployQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部数据
     * @param criteria 条件
     * @return /
     */
    List<ServerDeployDto> queryAll(ServerDeployQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    ServerDeployDto findById(Long id);

    /**
     * 创建
     * @param resources /
     */
    void create(ServerDeploy resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(ServerDeploy resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 根据IP查询
     * @param ip /
     * @return /
     */
    ServerDeployDto findByIp(String ip);

	/**
	 * 测试登录服务器
	 * @param resources /
	 * @return /
	 */
	Boolean testConnect(ServerDeploy resources);

    /**
     * 导出数据
     * @param queryAll /
     * @param response /
     * @throws IOException /
     */
    void download(List<ServerDeployDto> queryAll, HttpServletResponse response) throws IOException;
}
