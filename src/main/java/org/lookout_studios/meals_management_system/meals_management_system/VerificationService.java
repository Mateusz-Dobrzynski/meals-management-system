package org.lookout_studios.meals_management_system.meals_management_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
public class VerificationService {
    private final String requestSummary = "Verify user email";
    private final String requestDescription = """
            Verifies a user email and grants the user access to the API.
            URL with this request is enclosed in a verification email.
            Once the request is made, the user email is verified
            and the user can log in to the API and use it without further restrictions.
            """;
    private final String userIdDescription = "ID of a user to be verified";
    private final String registrationTokenDescription = """
                Registration token generated by the API
                and enclosed in a verification email
            """;
    private String verificationUnsuccessfulMessage = """
            The registration token does not belong to user with this ID
            """;
    private String verificationSuccessfulMessage = "Verification successful";
    Logger log = LoggerFactory.getLogger(VerificationService.class);

    @GetMapping(value = "/verify", produces = "application/json")
    @Tag(name = "user")
    @Operation(summary = requestSummary, description = requestDescription)
    @Parameter(name = "registrationToken", required = true, description = registrationTokenDescription)
    @Parameter(name = "userId", required = true, description = userIdDescription)
    @ApiResponse(responseCode = "200", description = "${api.response-codes.ok.desc}", content = @Content)
    @ApiResponse(responseCode = "403", description = "Registration token does not belong to user with provided ID", content = @Content)
    @ApiResponse(responseCode = "500", description = "${api.response-codes.internalServerError.desc}", content = @Content)
    public ResponseEntity<String> verifyUser(
            @RequestParam int userId,
            @RequestParam String registrationToken)
            throws Exception {
        log.info(String.format(
                "New registration request for user with id %d and registration token '%s'",
                userId, registrationToken));
        DatabaseService databaseService = new DatabaseService();
        if (!databaseService.verifyRegistrationToken(userId, registrationToken)) {
            log.info(String.format(
                    "Token '%s' does not belong to user with id %d, verification unsuccessful",
                    registrationToken,
                    userId));
            return new ResponseEntity<String>(
                    new ResponseBody(
                            HttpStatus.FORBIDDEN,
                            verificationUnsuccessfulMessage).getResponseBody(),
                    HttpStatus.FORBIDDEN);
        }
        databaseService.markUserAsVerified(userId);
        log.info(String.format(
                "User with id %d verified successfully",
                userId));
        return new ResponseEntity<String>(
                new ResponseBody(
                        HttpStatus.OK,
                        verificationSuccessfulMessage)
                        .getResponseBody(),
                HttpStatus.OK);
    }
}