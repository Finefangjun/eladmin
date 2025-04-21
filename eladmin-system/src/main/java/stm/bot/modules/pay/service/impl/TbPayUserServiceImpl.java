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
package stm.bot.modules.pay.service.impl;

import stm.bot.modules.pay.domain.TbPayUser;
import stm.bot.utils.ValidationUtil;
import stm.bot.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import stm.bot.modules.pay.repository.TbPayUserRepository;
import stm.bot.modules.pay.service.TbPayUserService;
import stm.bot.modules.pay.service.dto.TbPayUserDto;
import stm.bot.modules.pay.service.dto.TbPayUserQueryCriteria;
import stm.bot.modules.pay.service.mapstruct.TbPayUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import stm.bot.utils.PageUtil;
import stm.bot.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import stm.bot.utils.PageResult;

/**
* @website https://awss.vip.com
* @description 服务实现
* @author admin
* @date 2025-04-20
**/
@Service
@RequiredArgsConstructor
public class TbPayUserServiceImpl implements TbPayUserService {

    private final TbPayUserRepository tbPayUserRepository;
    private final TbPayUserMapper tbPayUserMapper;

    @Override
    public PageResult<TbPayUserDto> queryAll(TbPayUserQueryCriteria criteria, Pageable pageable){
        Page<TbPayUser> page = tbPayUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(tbPayUserMapper::toDto));
    }

    @Override
    public List<TbPayUserDto> queryAll(TbPayUserQueryCriteria criteria){
        return tbPayUserMapper.toDto(tbPayUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TbPayUserDto findById(Integer id) {
        TbPayUser tbPayUser = tbPayUserRepository.findById(id).orElseGet(TbPayUser::new);
        ValidationUtil.isNull(tbPayUser.getId(),"TbPayUser","id",id);
        return tbPayUserMapper.toDto(tbPayUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(TbPayUser resources) {
        tbPayUserRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TbPayUser resources) {
        TbPayUser tbPayUser = tbPayUserRepository.findById(resources.getId()).orElseGet(TbPayUser::new);
        ValidationUtil.isNull( tbPayUser.getId(),"TbPayUser","id",resources.getId());
        tbPayUser.copy(resources);
        tbPayUserRepository.save(tbPayUser);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            tbPayUserRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TbPayUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TbPayUserDto tbPayUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户名", tbPayUser.getUserName());
            map.put("真实姓名", tbPayUser.getRealName());
            map.put("手机号", tbPayUser.getMobile());
            map.put("创建者", tbPayUser.getCreateBy());
            map.put("更新者", tbPayUser.getUpdateBy());
            map.put("创建日期", tbPayUser.getCreateTime());
            map.put("更新时间", tbPayUser.getUpdateTime());
            map.put("sys商户", tbPayUser.getSysMerchant());
            map.put("systemKey", tbPayUser.getSystemKey());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public TbPayUser findBySysMerchant(String sysMerchant) {
        return tbPayUserRepository.findBySysMerchant(sysMerchant);
    }
}