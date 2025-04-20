package stm.bot.modules.pay.strategy.impl;

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
import stm.bot.modules.pay.strategy.util.StrategyUtil;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

@Component
@Slf4j
public class TestStrategy implements RechargeStrategy {
    @Override
    public ResponseEntity toPayInfo(TzPayMethod tzPayMethod, PayInputDto dto) {
        String name = System.currentTimeMillis() / 1000 + "";
        Map<String, String> signParams = new TreeMap<String, String>();
        signParams.put("pid", tzPayMethod.getMerchantId());
        signParams.put("type", tzPayMethod.getPayCode());
        signParams.put("out_trade_no", dto.getOrderNo());
        signParams.put("notify_url", tzPayMethod.getNotifyUrl());
        signParams.put("return_url", dto.getReturnUrl());
        signParams.put("name", name);
        BigDecimal price = new BigDecimal(dto.getAmount()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
        String money = price.setScale(2, BigDecimal.ROUND_HALF_UP) + "";
        signParams.put("money", money);
        signParams.put("clientip", dto.getCreateIp());
        //pc 电脑,mobile h5,qq,wechat,alipay
        signParams.put("device", tzPayMethod.getPayTitle());

        String paramString = StrategyUtil.createParamStringValueNotNull(signParams);
        paramString = paramString + tzPayMethod.getPublicKey();
        System.out.println(paramString);
        String sign = StrategyUtil.getMD5(paramString);
        signParams.put("sign_type", "MD5");
        signParams.put("sign", sign);

        String params = StrategyUtil.createParamValueString(signParams);
        JSONObject resultJson = new JSONObject();
        String result = null;
        try {
            log.info(tzPayMethod.getApiUrl() + "TestStrategy 请求数据：\n" + params);
            HttpResponse httpResponse = HttpRequest.post(tzPayMethod.getApiUrl())
                    .header("accept", "*/*")
                    .header("Content-type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36")
                    .body(params)
                    .execute();
            if (httpResponse == null || httpResponse.getStatus() != 200 || StringUtils.isEmpty(httpResponse.body())) {
                log.error("TestStrategy 调用接口异常，params：" + params + " ,  dto信息：" + dto.toString() + "  ,返回状态码：" + (httpResponse != null ? httpResponse.getStatus() : "") + ",返回信息：" + (httpResponse != null ? httpResponse.body() : ""));
                resultJson.put("payUrl", "");
                return new ResponseEntity<>("返回信息异常" + resultJson, HttpStatus.BAD_REQUEST);
            }
            result = httpResponse.body();
            log.info("TestStrategy 调用接口成功，params：" + params + " ,  dto信息：" + dto.toString() + "  ,返回状态码：" + (httpResponse != null ? httpResponse.getStatus() : "") + ",返回信息：" + result);
        } catch (Exception e) {
            log.error("TestStrategy 调用接口异常，params：" + params + " ,  dto信息：" + dto.toString() + "  ,错误信息：" + e.getMessage(), e);
            resultJson.put("payUrl", "");
            return new ResponseEntity<>("请求异常1" + resultJson, HttpStatus.BAD_REQUEST);
        }

        JSONObject resJson = new JSONObject();
        try {
            resJson = JSON.parseObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ObjectUtils.isEmpty(resJson)) {
            log.error("TestStrategy 调用接口异常resJson null");
            resultJson.put("payUrl", "");
            return new ResponseEntity<>("请求异常2" + resultJson, HttpStatus.BAD_REQUEST);
        }

        if (ObjectUtils.isNotEmpty(resJson.getString("code")) && !"1".equals(resJson.getString("code"))) {
            log.error("TestStrategy 调用接口异常resJson code" + resJson.getString("code"));
            resultJson.put("payUrl", "");
            return new ResponseEntity<>("请求异常3" + resultJson, HttpStatus.BAD_REQUEST);
        }

        String returnUrl = "";
        if (resJson.containsKey("qrcode")) {
            //如果返回该字段，则直接跳转到该url支付
            returnUrl = resJson.getString("qrcode");
        }
        if (resJson.containsKey("payurl")) {
            //直接跳转到该url支付
            returnUrl = resJson.getString("payurl");
        }
        if (resJson.containsKey("urlscheme")) {
            //使用js跳转该url，可发起微信小程序支付
            returnUrl = resJson.getString("urlscheme");
        }
        resultJson.put("payUrl", returnUrl);
        return new ResponseEntity<>(resultJson, HttpStatus.OK);
    }

    @Override
    public boolean checkCallbackSign(String publicKey, String privateKey, Map<String, String> params, String requestData) {
        String paramString = StrategyUtil.createParamStringValueNotSign(params);
        paramString = paramString + publicKey;
        log.info(paramString);
        String sign = StrategyUtil.getMD5(paramString);
        log.info(sign);
        return params.get("sign").equals(sign);
    }
}
