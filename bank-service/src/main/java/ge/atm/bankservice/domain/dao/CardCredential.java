package ge.atm.bankservice.domain.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
@Builder
public class CardCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_credential_id_seq_gen")
    @SequenceGenerator(name = "card_credential_id_seq_gen", allocationSize = 1, sequenceName = "card_credential_id_seq")
    private long id;

    private String secret;

    @ManyToOne
    private AuthMethod credentialType;

    @ManyToOne
    private Card card;
}
