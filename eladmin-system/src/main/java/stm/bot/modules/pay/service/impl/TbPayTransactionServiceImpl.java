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

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stm.bot.modules.pay.domain.TbPayTransaction;
import stm.bot.modules.pay.repository.TbPayTransactionRepository;
import stm.bot.modules.pay.service.TbPayTransactionService;
import stm.bot.modules.pay.service.dto.TbPayTransactionDto;
import stm.bot.modules.pay.service.dto.TbPayTransactionQueryCriteria;
import stm.bot.modules.pay.service.mapstruct.TbPayTransactionMapper;
import stm.bot.utils.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @website https://awss.vip.com
 * @description 服务实现
 * @date 2025-04-18
 **/
@Service
@RequiredArgsConstructor
public class TbPayTransactionServiceImpl implements TbPayTransactionService {

    private final TbPayTransactionRepository tbPayTransactionRepository;
    private final TbPayTransactionMapper tbPayTransactionMapper;

    @Override
    public PageResult<TbPayTransactionDto> queryAll(TbPayTransactionQueryCriteria criteria, Pageable pageable) {
        Page<TbPayTransaction> page = tbPayTransactionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(tbPayTransactionMapper::toDto));
    }

    @Override
    public List<TbPayTransactionDto> queryAll(TbPayTransactionQueryCriteria criteria) {
        return tbPayTransactionMapper.toDto(tbPayTransactionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public TbPayTransactionDto findById(Integer id) {
        TbPayTransaction tbPayTransaction = tbPayTransactionRepository.findById(id).orElseGet(TbPayTransaction::new);
        ValidationUtil.isNull(tbPayTransaction.getId(), "TbPayTransaction", "id", id);
        return tbPayTransactionMapper.toDto(tbPayTransaction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(TbPayTransaction resources) {
        tbPayTransactionRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TbPayTransaction resources) {
        TbPayTransaction tbPayTransaction = tbPayTransactionRepository.findById(resources.getId()).orElseGet(TbPayTransaction::new);
        ValidationUtil.isNull(tbPayTransaction.getId(), "TbPayTransaction", "id", resources.getId());
        tbPayTransaction.copy(resources);
        tbPayTransactionRepository.save(tbPayTransaction);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            tbPayTransactionRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TbPayTransactionDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TbPayTransactionDto tbPayTransaction : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", tbPayTransaction.getUsername());
            map.put("用户类型", tbPayTransaction.getUserType());
            map.put("pay_method_id", tbPayTransaction.getPayMid());
            map.put("payCode", tbPayTransaction.getPayCode());
            map.put("通道", tbPayTransaction.getChannelId());
            map.put("商户号", tbPayTransaction.getMerchantId());
            map.put("sys单号", tbPayTransaction.getSystemNo());
            map.put("pay下单号", tbPayTransaction.getOrderNo());
            map.put("pay订单号", tbPayTransaction.getPayOrderNo());
            map.put("金额", tbPayTransaction.getAmount());
            map.put("实际金额", tbPayTransaction.getActualAmount());
            map.put("费率%", tbPayTransaction.getFee());
            map.put("回调ip", tbPayTransaction.getIp());
            map.put("回调url", tbPayTransaction.getNotifyUrl());
            map.put("支付时间", tbPayTransaction.getPayTime());
            map.put("状态", tbPayTransaction.getPayStatus());
            map.put("备注", tbPayTransaction.getRemark());
            map.put("创建者", tbPayTransaction.getCreateBy());
            map.put("更新者", tbPayTransaction.getUpdateBy());
            map.put("创建日期", tbPayTransaction.getCreateTime());
            map.put("更新时间", tbPayTransaction.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public TbPayTransaction findByOrderNo(String orderNo) {
        return tbPayTransactionRepository.findByOrderNo(orderNo);
    }
}