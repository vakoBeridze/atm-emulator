package ge.atm.atmservice.api;

import ge.atm.atmservice.domain.AuthenticationRequest;
import ge.atm.atmservice.domain.AuthenticationResponse;
import ge.atm.atmservice.service.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@Api("Authentication API")
@RequestMapping(value = "/api/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @ApiOperation(value = "Initiate authentication with card number")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = AuthenticationResponse.class),
            @ApiResponse(code = 400, message = "bad request"),
            @ApiResponse(code = 401, message = "unauthorized"),
            @ApiResponse(code = 403, message = "access is denied"),
            @ApiResponse(code = 417, message = "input validation failed")
    })
    @PostMapping("/initiate")
    public ResponseEntity<AuthenticationResponse> initiateAuthentication(@RequestBody final AuthenticationRequest request) {
        final AuthenticationResponse authenticationResponse = authenticationService.initiateAuthentication(request);
        return ResponseEntity.ok().body(authenticationResponse);
    }

    @ApiOperation(value = "Finalise authentication using preferred authentication method")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "bad request"),
            @ApiResponse(code = 401, message = "unauthorized"),
            @ApiResponse(code = 403, message = "access is denied"),
            @ApiResponse(code = 417, message = "input validation failed")
    })
    @PostMapping("/finalize")
    public ResponseEntity<AuthenticationResponse> finalizeAuthentication(@RequestBody final AuthenticationRequest request) {
        final AuthenticationResponse authenticationResponse = authenticationService.finalizeAuthentication(request);
        return ResponseEntity.ok().body(authenticationResponse);
    }
}

