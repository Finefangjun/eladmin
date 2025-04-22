package stm.bot.modules.pay.rest;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stm.bot.annotation.rest.AnonymousPostMapping;
import stm.bot.modules.pay.domain.TbPayTransaction;
import stm.bot.modules.pay.domain.TbPayUser;
import stm.bot.modules.pay.service.TbPayTransactionService;
import stm.bot.modules.pay.service.TbPayUserService;
import stm.bot.modules.pay.strategy.RechargeContextService;
import stm.bot.modules.pay.strategy.dto.PayInputDto;
import stm.bot.modules.pay.strategy.util.StrategyUtil;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = "payOrder")
@RequestMapping("/api/payOrder")
public class PayOrderController {

    private final RechargeContextService rechargeContextService;
    private final TbPayTransactionService tbPayTransactionService;
    private final TbPayUserService tbPayUserService;

    @ApiOperation("创建支付订单")
    @AnonymousPostMapping("/create")
    public ResponseEntity<Object> createPayOrder(HttpServletRequest request, @RequestBody PayInputDto dto) {
        ResponseEntity baseResponse = rechargeContextService.toRecharge(dto);
        return baseResponse;
    }

    @ApiOperation(value = "根据orderNo手动回调--payStatus状态2")
    @GetMapping(value = "/payManualCallback")
    public String payManualCallback(String orderNo) {
        TbPayTransaction tbPayTransaction = tbPayTransactionService.findByOrderNo(orderNo);
        if (ObjectUtils.isEmpty(tbPayTransaction)) {
            return "不存在该订单";
        }

        String finishedTime = new SimpleDateFormat("yyyyMMddHHmmss").format(tbPayTransaction.getPayTime());
        Map<String, String> paramMap = new LinkedHashMap<>();
        paramMap.put("systemOrderNo", tbPayTransaction.getPayOrderNo());
        paramMap.put("orderNo", tbPayTransaction.getOrderNo());
        paramMap.put("totalAmount", tbPayTransaction.getAmount() + "");
        paramMap.put("merchantNo", tbPayTransaction.getUserType());
        paramMap.put("payStatus", "success");
        paramMap.put("payTime", finishedTime);
        String signParamString = StrategyUtil.createParamSignString(paramMap);

        TbPayUser tbPayUser = tbPayUserService.findBySysMerchant(tbPayTransaction.getUserType());
        String reSign = StrategyUtil.getSign(signParamString, tbPayUser.getSystemKey());
        paramMap.put("sign", reSign);

        String params = StrategyUtil.createParamSignString(paramMap);

        String url = tbPayTransaction.getNotifyUrl();
        HttpResponse httpResponse = null;
        try {
            Thread.sleep(200);
            log.info("payManualCallback url " + url + " params " + params);
            httpResponse = HttpRequest.post(url)
                    .header("Content-type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36")
                    .body(params)
                    .execute();
        } catch (Exception e) {
            log.info("payManualCallback调用接口异常请求信息：url:" + url + "   , params:" + params + "  ,错误信息：" + e.getMessage());
        }

        if (httpResponse == null || httpResponse.getStatus() != 200 || StringUtils.isEmpty(httpResponse.body())) {
            return "回调" + orderNo + "返回错误";
        }
        return "回调" + orderNo + "返回信息为： " + httpResponse.body();
    }
}
