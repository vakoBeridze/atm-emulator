package ge.atm.bankservice.domain.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class AuthMethod {

    @Id
    private long id;
    private String description;

    public AuthMethod(long id) {
        this.id = id;
    }
}
