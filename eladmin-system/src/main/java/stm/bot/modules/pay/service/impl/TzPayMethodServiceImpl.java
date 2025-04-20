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

import stm.bot.modules.pay.domain.TzPayMethod;
import stm.bot.utils.ValidationUtil;
import stm.bot.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import stm.bot.modules.pay.repository.TzPayMethodRepository;
import stm.bot.modules.pay.service.TzPayMethodService;
import stm.bot.modules.pay.service.dto.TzPayMethodDto;
import stm.bot.modules.pay.service.dto.TzPayMethodQueryCriteria;
import stm.bot.modules.pay.service.mapstruct.TzPayMethodMapper;
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
* @date 2025-04-18
**/
@Service
@RequiredArgsConstructor
public class TzPayMethodServiceImpl implements TzPayMethodService {

    private final TzPayMethodRepository tzPayMethodRepository;
    private final TzPayMethodMapper tzPayMethodMapper;

    @Override
    public PageResult<TzPayMethodDto> queryAll(TzPayMethodQueryCriteria criteria, Pageable pageable){
        Page<TzPayMethod> page = tzPayMethodRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(tzPayMethodMapper::toDto));
    }

    @Override
    public List<TzPayMethodDto> queryAll(TzPayMethodQueryCriteria criteria){
        return tzPayMethodMapper.toDto(tzPayMethodRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TzPayMethodDto findById(Integer id) {
        TzPayMethod tzPayMethod = tzPayMethodRepository.findById(id).orElseGet(TzPayMethod::new);
        ValidationUtil.isNull(tzPayMethod.getId(),"TzPayMethod","id",id);
        return tzPayMethodMapper.toDto(tzPayMethod);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(TzPayMethod resources) {
        tzPayMethodRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TzPayMethod resources) {
        TzPayMethod tzPayMethod = tzPayMethodRepository.findById(resources.getId()).orElseGet(TzPayMethod::new);
        ValidationUtil.isNull( tzPayMethod.getId(),"TzPayMethod","id",resources.getId());
        tzPayMethod.copy(resources);
        tzPayMethodRepository.save(tzPayMethod);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            tzPayMethodRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TzPayMethodDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TzPayMethodDto tzPayMethod : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("payid", tzPayMethod.getMethodId());
            map.put("pay名称", tzPayMethod.getMethodName());
            map.put("payCode", tzPayMethod.getPayCode());
            map.put("通道", tzPayMethod.getChannelId());
            map.put("sys商户", tzPayMethod.getSysMerchant());
            map.put("商户号", tzPayMethod.getMerchantId());
            map.put("publicKey", tzPayMethod.getPublicKey());
            map.put("privateKey", tzPayMethod.getPrivateKey());
            map.put("systemKey", tzPayMethod.getSystemKey());
            map.put("请求url", tzPayMethod.getApiUrl());
            map.put("同步url", tzPayMethod.getReturnUrl());
            map.put("回调url", tzPayMethod.getNotifyUrl());
            map.put("执行器", tzPayMethod.getClassAddres());
            map.put("费率%", tzPayMethod.getRate());
            map.put("标题", tzPayMethod.getPayTitle());
            map.put("内容", tzPayMethod.getPayContent());
            map.put("状态", tzPayMethod.getStatus());
            map.put("备注", tzPayMethod.getRemark());
            map.put("创建者", tzPayMethod.getCreateBy());
            map.put("更新者", tzPayMethod.getUpdateBy());
            map.put("创建日期", tzPayMethod.getCreateTime());
            map.put("更新时间", tzPayMethod.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}