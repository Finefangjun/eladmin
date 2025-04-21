package stm.bot.modules.pay.rest;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stm.bot.annotation.rest.AnonymousPostMapping;
import stm.bot.modules.pay.domain.TbPayTransaction;
import stm.bot.modules.pay.domain.TbPayUser;
import stm.bot.modules.pay.service.TbPayTransactionService;
import stm.bot.modules.pay.service.TbPayUserService;
import stm.bot.modules.pay.service.TzPayMethodService;
import stm.bot.modules.pay.service.dto.TzPayMethodDto;
import stm.bot.modules.pay.strategy.RechargeStrategy;
import stm.bot.modules.pay.strategy.util.ApplicationContextProvider;
import stm.bot.modules.pay.strategy.util.StrategyUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = "payOrder")
@RequestMapping("/api/payCallback")
public class PayCallbackController {

    private final TbPayUserService tbPayUserService;
    private final TbPayTransactionService tbPayTransactionService;
    private final TzPayMethodService tzPayMethodService;

    @ApiOperation("LeiDian回调")
    @AnonymousPostMapping("/postLeiDianCallBack")
    public String postLeiDianCallBack(HttpServletRequest request) {
        String returnMsg = "success";
        String returnErrorMsg = "fail";
        JSONObject resJson = new JSONObject();
        try {
            request.setCharacterEncoding("utf-8");
            log.info("postLeiDianCallBack " + "接收到回调信息时间:" + DateUtil.now());

            Map<String, String[]> parameterMap = request.getParameterMap();
            if (ObjectUtils.isNotEmpty(parameterMap)) {
                Set<Map.Entry<String, String[]>> set = parameterMap.entrySet();
                for (Map.Entry<String, String[]> entry : set) {
                    String[] value = entry.getValue();
                    resJson.put(entry.getKey(), value[0]);
                }
                log.info("postLeiDianCallBack " + "接收到回调信息 接收到form格式转json:" + resJson);
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String reqBody = sb.toString();
                resJson = JSON.parseObject(reqBody);
                log.info("postLeiDianCallBack " + "接收到回调信息 接收到json格式:" + resJson);
            }

            String orderno = resJson.getString("orderid");
            String payOrderCode = resJson.getString("transaction_id");
            String amount = resJson.getString("amount");
            String payStatus = resJson.getString("returncode");

            if (StringUtils.isBlank(payStatus) || !"00".equals(payStatus)) {
                log.info("postLeiDianPayCallBack" + "接收到回调信息 接收到json格式:" + resJson + " payStatus状态错误");
                return returnErrorMsg;
            }

            TbPayTransaction tbPayTransaction = tbPayTransactionService.findByOrderNo(orderno);
            if (tbPayTransaction == null) {
                return returnErrorMsg;
            }

            //验证签名方法：
            TzPayMethodDto tzPayMethod = tzPayMethodService.findById(tbPayTransaction.getPayMid());
            log.info("开始验签：");
            if (tzPayMethod != null && StringUtils.isNotEmpty(tzPayMethod.getClassAddres())) {
                RechargeStrategy rechargeStrategy = (RechargeStrategy) ApplicationContextProvider.getBean(tzPayMethod.getClassAddres());
                Map<String, String> params = JSONObject.parseObject(resJson.toJSONString(), Map.class);
                log.info("传参：" + params);
                //验证签名
                boolean valid = rechargeStrategy.checkCallbackSign(tzPayMethod.getPublicKey(), tzPayMethod.getPrivateKey(), params);
                if (!valid) {
                    return returnErrorMsg;
                }
            }
            log.info("验签：通过");

            //业务逻辑
            tbPayTransaction.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
            tbPayTransaction.setPayOrderNo(payOrderCode);
            tbPayTransaction.setActualAmount(new BigDecimal(amount).multiply(new BigDecimal("100")));
            tbPayTransaction.setPayTime(Timestamp.valueOf(LocalDateTime.now()));
            tbPayTransaction.setPayStatus(2);
            tbPayTransactionService.update(tbPayTransaction);

            //通知第四方----------------------------------------------------------------------------------------------------
            String url = tbPayTransaction.getNotifyUrl();
            //组装数据加密，发returnUrl,获取回调返回给第三方


            String finishedTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            Map<String, String> paramMap = new LinkedHashMap<>();
            paramMap.put("systemOrderNo", tbPayTransaction.getPayOrderNo());
            paramMap.put("orderNo", tbPayTransaction.getOrderNo());
            paramMap.put("totalAmount", tbPayTransaction.getAmount() + "");
            paramMap.put("merchantNo", tbPayTransaction.getUserType());
            paramMap.put("payStatus", "success");
            paramMap.put("payTime", finishedTime);
            String signParamString = StrategyUtil.createParamSignString(paramMap);
            log.info("calback " + paramMap);
            TbPayUser tbPayUser = tbPayUserService.findBySysMerchant(tbPayTransaction.getUserType());
            String reSign = StrategyUtil.getSign(signParamString, tbPayUser.getSystemKey());
            paramMap.put("sign", reSign);

            String params = StrategyUtil.createParamSignString(paramMap);
            HttpResponse httpResponse = null;
            try {
                Thread.sleep(1000);
                httpResponse = HttpRequest.post(url)
                        .header("Content-type", "application/x-www-form-urlencoded;charset=UTF-8")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36")
                        .body(params)
                        .execute();
            } catch (Exception e) {
                log.info("postDingRunPayCallBack调用接口异常请求信息：url:" + url + "   , params:" + params + "  ,错误信息：" + e.getMessage());
            }

            if (httpResponse == null || httpResponse.getStatus() != 200 || StringUtils.isEmpty(httpResponse.body())) {
                return returnErrorMsg;
            }
            String reBody = httpResponse.body();
            if (reBody.length() > 200) {
                tbPayTransaction.setRemark(reBody.substring(0, 200));
            } else {
                tbPayTransaction.setRemark(reBody);
            }
            tbPayTransactionService.update(tbPayTransaction);
            if (reBody.toLowerCase().contains("success")) {
                return returnMsg;
            }
            return returnErrorMsg;

        } catch (Exception e) {
            log.error("LeiDian支付交易异步回调接收,处理出错：" + e.getMessage(), e);
            return returnErrorMsg;
        }
    }
}
