package stm.bot.modules.pay.strategy;


import org.springframework.http.ResponseEntity;
import stm.bot.modules.pay.strategy.dto.PayInputDto;

public interface IRechargeContextService {

    ResponseEntity toRecharge(PayInputDto dto);

}
