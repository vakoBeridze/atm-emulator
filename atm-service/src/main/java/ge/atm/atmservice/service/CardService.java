package ge.atm.atmservice.service;

import ge.atm.atmservice.domain.dao.Card;
import org.springframework.stereotype.Service;

@Service
public interface CardService {

    Card getCard(String cardNumber);
}
