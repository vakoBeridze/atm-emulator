package ge.atm.bankservice.domain.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class CardCredential {
    @Id
    private long id;

    private String secret;

    @ManyToOne
    private AuthMethod credentialType;

    @ManyToOne
    private Card card;
}
