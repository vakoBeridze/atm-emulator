package ge.atm.bankservice.service.impl;

import ge.atm.bankservice.domain.dto.AuthenticationMethod;
import ge.atm.bankservice.domain.dto.CardDto;
import ge.atm.bankservice.repository.CardRepository;
import ge.atm.bankservice.service.CardService;
import ge.atm.bankservice.service.CardValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;
    private final CardValidationService pinCodeValidator;
    private final CardValidationService fingerprintValidator;

    public CardServiceImpl(CardRepository cardRepository, ModelMapper modelMapper,
                           @Qualifier(CardValidationService.PIN_CODE_VALIDATOR) CardValidationService pinCodeValidator,
                           @Qualifier(CardValidationService.FINGERPRINT_VALIDATOR) CardValidationService fingerprintValidator) {
        this.cardRepository = cardRepository;
        this.modelMapper = modelMapper;
        this.pinCodeValidator = pinCodeValidator;
        this.fingerprintValidator = fingerprintValidator;
    }

    public CardDto getCard(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber)
                .map(card -> modelMapper.map(card, CardDto.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card could not be found"));
    }

    @Override
    public void validateSecret(String cardNumber, String secret, AuthenticationMethod preferredAuthenticationMethod) {

        switch (preferredAuthenticationMethod) {
            case PIN:
                pinCodeValidator.validate(cardNumber, secret);
                break;
            case FINGERPRINT:
                fingerprintValidator.validate(cardNumber, secret);
                break;
        }
    }
}
