package stm.bot.modules.pay.strategy;

import org.springframework.http.ResponseEntity;
import stm.bot.modules.pay.domain.TzPayMethod;
import stm.bot.modules.pay.strategy.dto.PayInputDto;

import java.util.Map;

public interface RechargeStrategy {
    /**
     * 获取支付信息
     *
     * @param tzPayMethod
     * @param dto
     * @return
     */
    ResponseEntity toPayInfo(TzPayMethod tzPayMethod, PayInputDto dto);

    /**
     * 验证支付交易返回结果签名
     *
     * @param publicKey   公钥
     * @param privateKey  私钥
     * @param params      原始请求信息
     * @param requestData 原始请求信息（json）
     * @return
     */
    boolean checkCallbackSign(String publicKey, String privateKey, Map<String, String> params, String requestData);

    /**
     * 验证支付交易返回结果签名
     *
     * @param publicKey  公钥
     * @param privateKey 私钥
     * @param params     原始请求信息
     * @return
     */
    default boolean checkCallbackSign(String publicKey, String privateKey, Map<String, String> params) {
        return checkCallbackSign(publicKey, privateKey, params, null);
    }

}