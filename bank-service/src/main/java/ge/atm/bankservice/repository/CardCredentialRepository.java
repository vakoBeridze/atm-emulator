package ge.atm.bankservice.repository;

import ge.atm.bankservice.domain.dao.CardCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardCredentialRepository extends JpaRepository<CardCredential, Long> {

    Optional<CardCredential> findByCard_CardNumberAndCredentialType_Id(String cardNumber, Long credentialTypeId);
}
