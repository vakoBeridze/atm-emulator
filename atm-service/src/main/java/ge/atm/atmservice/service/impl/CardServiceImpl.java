package ge.atm.atmservice.service.impl;

import ge.atm.atmservice.domain.dao.Card;
import ge.atm.atmservice.repository.CardRepository;
import ge.atm.atmservice.service.CardService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
//    private final ModelMapper modelMapper;

    public Card getCard(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber)
//                .map(card -> modelMapper.map(card, CardDto.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card could not be found"));
    }
}
