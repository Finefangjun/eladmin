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
package stm.bot.modules.pay.service;

import stm.bot.modules.pay.domain.TbPayUser;
import stm.bot.modules.pay.service.dto.TbPayUserDto;
import stm.bot.modules.pay.service.dto.TbPayUserQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import stm.bot.utils.PageResult;

/**
* @website https://awss.vip.com
* @description 服务接口
* @author admin
* @date 2025-04-20
**/
public interface TbPayUserService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    PageResult<TbPayUserDto> queryAll(TbPayUserQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<TbPayUserDto>
    */
    List<TbPayUserDto> queryAll(TbPayUserQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return TbPayUserDto
     */
    TbPayUserDto findById(Integer id);

    /**
    * 创建
    * @param resources /
    */
    void create(TbPayUser resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(TbPayUser resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Integer[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<TbPayUserDto> all, HttpServletResponse response) throws IOException;

    TbPayUser findBySysMerchant(String sysMerchant);
}