package org.lookout_studios.meals_management_system.meals_management_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
public class FridgeController {
    private static final String requestSummary = """
            Create a new fridge.
            """;
    private static final String requestDescription = """
            Creates a fridge and assigns it to a currently logged-in user.
            """;
    private static final String okResponseCode = "200";
    private static final String okDescriptionTemplate = "${api.response-codes.ok.desc}";
    private static final String forbiddenResponseCode = "403";
    private static final String forbiddenDescription = "";
    private static final String serverErrorResponseCode = "500";
    private static final String serverErrorDescriptionTemplate = """
            ${api.response-codes.internalServerError.desc}
            """;
    private static final String userTokenDescription = """
            ${api.query-params.userToken.desc}
            """;
    private static final String refreshTokenDescription = """
            ${api.query-params.refreshToken.desc}
            """;

    Logger log = LoggerFactory.getLogger(FridgeController.class);

    @PostMapping(value = "/fridge/create", consumes = "application/json", produces = "application/json")
    @Tag(name = "fridge")
    @Operation(summary = requestSummary, description = requestDescription)
    @Parameter(name = "userToken", required = true, description = userTokenDescription)
    @Parameter(name = "refreshToken", required = true, description = refreshTokenDescription)
    @ApiResponse(responseCode = okResponseCode, description = okDescriptionTemplate, content = @Content)
    @ApiResponse(responseCode = forbiddenResponseCode, description = forbiddenDescription, content = @Content)
    @ApiResponse(responseCode = serverErrorResponseCode, description = serverErrorDescriptionTemplate, content = @Content)
    public ResponseEntity<String> createFridge(
            @RequestParam String userToken,
            @RequestParam String refreshToken,
            @RequestParam int userId,
            @RequestParam String fridgeName) {
        log.info(String.format("""
                Fridge creation request for user with id %d,
                fridge name: %s", userId, fridgeName
                """));
        // TO-DO: check validity of user and refresh tokens
        DatabaseService database = new DatabaseService();
        database.createNewFridge(userId, fridgeName);
        return new ResponseEntity<String>(
                new ResponseBody(HttpStatus.OK).getResponseBody(),
                HttpStatus.OK);
    }
}
