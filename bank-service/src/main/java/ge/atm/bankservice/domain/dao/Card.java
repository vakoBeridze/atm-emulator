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
import javax.persistence.Version;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_id_seq_gen")
    @SequenceGenerator(name = "card_id_seq_gen", allocationSize = 1, sequenceName = "card_id_seq")
    private long id;

    private String cardNumber;
    private BigDecimal balance;
    private boolean blocked;
    private int incorrectAttempts;

    @ManyToOne
    private AuthMethod preferredAuth;

    @Version
    private long version;
}
