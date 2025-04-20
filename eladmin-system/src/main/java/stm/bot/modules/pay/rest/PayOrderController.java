package stm.bot.modules.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stm.bot.annotation.rest.AnonymousPostMapping;
import stm.bot.modules.pay.strategy.RechargeContextService;
import stm.bot.modules.pay.strategy.dto.PayInputDto;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Api(tags = "payOrder")
@RequestMapping("/api/payOrder")
public class PayOrderController {

    private final RechargeContextService rechargeContextService;

    @ApiOperation("创建支付订单")
    @AnonymousPostMapping("/create")
    public ResponseEntity<Object> createPayOrder(HttpServletRequest request, @RequestBody PayInputDto dto) {
        ResponseEntity baseResponse = rechargeContextService.toRecharge(dto);
        return baseResponse;
    }
}
