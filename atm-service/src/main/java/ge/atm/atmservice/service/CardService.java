package ge.atm.atmservice.service;

import ge.atm.bankservice.domain.dto.CardDto;
import org.springframework.stereotype.Service;

@Service
public interface CardService {

    CardDto getCard(String cardNumber);
}
