package ge.atm.atmservice.api;

import ge.atm.atmservice.service.AuthenticationService;
import ge.atm.atmservice.service.CardBalanceService;
import ge.atm.atmservice.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public abstract class BaseControllerTest {
	@MockBean
	protected AuthenticationService authenticationService;

	@MockBean
	protected CardBalanceService cardBalanceService;

	@MockBean
	protected CardService cardService;

	@Autowired
	protected MockMvc mockMvc;
}
