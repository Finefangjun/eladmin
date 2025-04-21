package stm.bot.modules.pay.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import stm.bot.modules.pay.domain.TbPayTransaction;
import stm.bot.modules.pay.domain.TbPayUser;
import stm.bot.modules.pay.domain.TzPayMethod;
import stm.bot.modules.pay.repository.TbPayTransactionRepository;
import stm.bot.modules.pay.repository.TbPayUserRepository;
import stm.bot.modules.pay.repository.TzPayMethodRepository;
import stm.bot.modules.pay.strategy.dto.PayInputDto;
import stm.bot.modules.pay.strategy.util.ApplicationContextProvider;
import stm.bot.modules.pay.strategy.util.CommonUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RechargeContextService implements IRechargeContextService {

    private final TbPayUserRepository tbPayUserRepository;
    private final TzPayMethodRepository tzPayMethodRepository;
    private final TbPayTransactionRepository tbPayTransactionRepository;

    @Override
    public ResponseEntity toRecharge(PayInputDto dto) {
        log.info("收到参数： " + dto.toString());
        TbPayUser tbPayUser = tbPayUserRepository.findBySysMerchant(dto.getMerchantNo());
        if (ObjectUtils.isEmpty(tbPayUser)) {
            return new ResponseEntity<>(dto.getMerchantNo() + "错误1!", HttpStatus.BAD_REQUEST);
        }
        TzPayMethod tzPayMethod = tzPayMethodRepository.findByMethodIdAndSysMerchant(dto.getPayCode(), dto.getMerchantNo());
        if (ObjectUtils.isEmpty(tzPayMethod)) {
            return new ResponseEntity<>(dto.getMerchantNo() + "错误2!", HttpStatus.BAD_REQUEST);
        }
        String classAddres = tzPayMethod.getClassAddres();
        RechargeStrategy rechargeStrategy = (RechargeStrategy) ApplicationContextProvider.getBean(classAddres);
        if (ObjectUtils.isEmpty(rechargeStrategy)) {
            return new ResponseEntity<>(dto.getMerchantNo() + "支付系统网关错误!", HttpStatus.BAD_REQUEST);
        }
//        //账号日充值限额
//        if (ObjectUtils.isNotEmpty(tzPayMethod.getMaxTotalRecharge()) && tzPayMethod.getMaxTotalRecharge() > 0) {
//            BigDecimal todayPrice = tbPayTransactionRepository.countPayTransactionPrice(DateUtil.today() + " 00:00:00", DateUtil.today() + " 23:59:59", dto.getServiceCode(), dto.getMerchantNo());
//            log.info(" method_id:" + dto.getServiceCode() + " 限额:" + tzPayMethod.getMaxTotalRecharge() + " user_type:" + dto.getMerchantNo() + " 今日已充值:" + todayPrice + " 本次充值:" + dto.getTotalAmount());
//            todayPrice = todayPrice.add(new BigDecimal(dto.getTotalAmount()));
//            if (todayPrice.compareTo(new BigDecimal(tzPayMethod.getMaxTotalRecharge())) >= 0) {
//                return setResult(Constants.HTTP_RES_CODE_2000, "商户" + dto.getMerchantNo() + "通道" + dto.getServiceCode() + "已达到限额", null);
//            }
//        }

        dto.setSystemNo(CommonUtil.getIdByUUId());
        ResponseEntity rechargeResponse = rechargeStrategy.toPayInfo(tzPayMethod, dto);

        if (rechargeResponse.getStatusCode().equals(HttpStatus.OK)) {
            TbPayTransaction tbPayTransaction = new TbPayTransaction();
            tbPayTransaction.setUsername(dto.getUserId());
            tbPayTransaction.setUserType(dto.getMerchantNo());
            tbPayTransaction.setPayMid(tzPayMethod.getId());
            tbPayTransaction.setPayCode(tzPayMethod.getPayCode());
            tbPayTransaction.setChannelId(tzPayMethod.getChannelId());
            tbPayTransaction.setMerchantId(tzPayMethod.getMerchantId());
            tbPayTransaction.setAmount(new BigDecimal(dto.getAmount()));
            tbPayTransaction.setActualAmount(new BigDecimal(dto.getAmount()));
            tbPayTransaction.setSystemNo(dto.getSystemNo());
            tbPayTransaction.setOrderNo(dto.getOrderNo());
            tbPayTransaction.setIp(dto.getCreateIp());
            tbPayTransaction.setPayStatus(1);
            tbPayTransaction.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
            tbPayTransaction.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
            tbPayTransaction.setCreateBy(dto.getUserId());
            tbPayTransaction.setUpdateBy(dto.getUserId());

            tbPayTransaction.setFee(new BigDecimal("0"));
            tbPayTransaction.setOrderNo(dto.getOrderNo());
            tbPayTransaction.setNotifyUrl(dto.getNotifyUrl());
            tbPayTransaction.setIp(dto.getCreateIp());
            tbPayTransactionRepository.save(tbPayTransaction);
        }

        return rechargeResponse;
    }

}
