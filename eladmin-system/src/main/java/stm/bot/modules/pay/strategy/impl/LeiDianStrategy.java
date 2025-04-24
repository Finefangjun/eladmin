package stm.bot.modules.pay.strategy.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import stm.bot.modules.pay.domain.TzPayMethod;
import stm.bot.modules.pay.strategy.RechargeStrategy;
import stm.bot.modules.pay.strategy.dto.PayInputDto;
import stm.bot.modules.pay.strategy.util.NumberUtils;
import stm.bot.modules.pay.strategy.util.StrategyUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
@Slf4j
public class LeiDianStrategy implements RechargeStrategy {
    @Override
    public ResponseEntity toPayInfo(TzPayMethod tzPayMethod, PayInputDto dto) {

        Map<String, String> signParams = new HashMap<String, String>();
        signParams.put("pay_memberid", tzPayMethod.getMerchantId());
        signParams.put("pay_bankcode", tzPayMethod.getPayCode());
        signParams.put("pay_orderid", dto.getOrderNo());

        int defNum = 0;
        if (ObjectUtils.isNotEmpty(tzPayMethod.getRemark()) && NumberUtils.isNumeric(tzPayMethod.getRemark())) {
            defNum = Integer.parseInt(tzPayMethod.getRemark());
            Random random = new Random();
            defNum = random.nextInt(defNum) + 1;
        }
        BigDecimal defPrice = new BigDecimal(defNum + "").divide(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN);
        String money = new BigDecimal(dto.getAmount()).subtract(defPrice).setScale(2, BigDecimal.ROUND_HALF_UP) + "";

        signParams.put("pay_amount", money);
        signParams.put("pay_applydate", DateUtil.now());
        signParams.put("pay_notifyurl", tzPayMethod.getNotifyUrl());
        signParams.put("pay_callbackurl", dto.getReturnUrl());


        String signParamString = StrategyUtil.createParamStringValueNotNull(signParams);
        String reSign = StrategyUtil.getSignThree(signParamString, tzPayMethod.getPublicKey()).toUpperCase();
        signParams.put("pay_md5sign", reSign);
        if (ObjectUtils.isNotEmpty(tzPayMethod.getPayTitle())) {
            signParams.put("pay_productname", tzPayMethod.getPayTitle());
        }
        if (ObjectUtils.isNotEmpty(dto.getUserId())) {
            signParams.put("pay_user_id", dto.getUserId());
        }
        if (ObjectUtils.isNotEmpty(dto.getCreateIp())) {
            signParams.put("pay_user_ip", dto.getCreateIp());
        }

        String params = StrategyUtil.createParamSignString(signParams);

        String url = tzPayMethod.getApiUrl();
        JSONObject resultJson = new JSONObject();
        String result = null;
        try {
            log.info(tzPayMethod.getApiUrl() + " LeiDianStrategy 请求数据：" + params);
            HttpResponse httpResponse = HttpRequest.post(url)
                    .header("Content-type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36")
                    .body(params)
                    .setConnectionTimeout(30000)
                    .execute();
            if (httpResponse == null || httpResponse.getStatus() != 200 || StringUtils.isEmpty(httpResponse.body())) {
                log.error("LeiDianStrategy 调用接口异常，params：" + params + " ,  dto信息：" + dto.toString() + "  ,返回状态码：" + (httpResponse != null ? httpResponse.getStatus() : "") + ",返回信息：" + (httpResponse != null ? httpResponse.body() : ""));
                resultJson.put("code", "101");
                resultJson.put("payUrl", "");
                resultJson.put("msg", "返回信息异常");
                return new ResponseEntity<>(resultJson, HttpStatus.BAD_REQUEST);
            }
            result = httpResponse.body();
        } catch (Exception e) {
            log.error("LeiDianStrategy 调用接口异常，params：" + params + " ,  dto信息：" + dto.toString() + "  ,错误信息：" + e.getMessage(), e);
            resultJson.put("code", "101");
            resultJson.put("payUrl", "");
            resultJson.put("msg", "请求异常1");
            return new ResponseEntity<>(resultJson, HttpStatus.BAD_REQUEST);
        }

        JSONObject resJson = new JSONObject();
        try {
            resJson = JSON.parseObject(result);
        } catch (Exception e) {
            log.error("LeiDianStrategy 调用接口异常resJson null");
            resultJson.put("code", "102");
            resultJson.put("payUrl", "");
            if (result.length() > 100) {
                result = result.substring(0, 100);
            }
            resultJson.put("msg", result);
            return new ResponseEntity<>(resultJson, HttpStatus.BAD_REQUEST);
        }
        if (ObjectUtils.isEmpty(resJson)) {
            log.error("LeiDianStrategy 调用接口异常resJson null");
            resultJson.put("code", "102");
            resultJson.put("payUrl", "");
            resultJson.put("msg", "请求异常2");
            return new ResponseEntity<>(resultJson, HttpStatus.BAD_REQUEST);
        }

        if (ObjectUtils.isEmpty(resJson.getString("status")) || !"success".equals(resJson.getString("status"))) {
            log.error("LeiDianStrategy 调用接口异常resJson code" + resJson);
            resultJson.put("code", "103");
            resultJson.put("payUrl", "");
            resultJson.put("msg", "请求异常3");
            return new ResponseEntity<>(resultJson, HttpStatus.BAD_REQUEST);
        }

        JSONObject midJson = resJson.getJSONObject("data");
        if (ObjectUtils.isEmpty(midJson)) {
            log.error("LeiDianStrategy 调用接口异常resJson data" + resJson);
            resultJson.put("code", "104");
            resultJson.put("payUrl", "");
            resultJson.put("msg", "请求异常4");
            return new ResponseEntity<>(resultJson, HttpStatus.BAD_REQUEST);
        }

        String returnUrl = "";
        if (midJson.containsKey("pay_url")) {
            returnUrl = midJson.getString("pay_url");
        }
        resultJson.put("code", "200");
        resultJson.put("payUrl", returnUrl);
        resultJson.put("msg", "success");
        return new ResponseEntity<>(resultJson, HttpStatus.OK);
    }

    @Override
    public boolean checkCallbackSign(String publicKey, String privateKey, Map<String, String> params, String requestData) {
        String signParamString = StrategyUtil.createParamStringValueNotNull(params);
        String sign = StrategyUtil.getSignThree(signParamString, publicKey).toUpperCase();
        log.info("leidian校验sign", "接口返回sign: " + params.get("sign") + " 参数加密后sign: " + sign, false);
        return params.get("sign").equals(sign);
    }

}
