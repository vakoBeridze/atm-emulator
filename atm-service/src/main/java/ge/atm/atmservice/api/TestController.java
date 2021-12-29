package ge.atm.atmservice.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/api")
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<String> getVerificationInformation() {
        return ResponseEntity.ok("HELLO");
    }

}
