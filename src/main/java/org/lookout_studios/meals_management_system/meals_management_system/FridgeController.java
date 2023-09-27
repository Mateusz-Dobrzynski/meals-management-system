package org.lookout_studios.meals_management_system.meals_management_system;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
public class FridgeController {
    private static final String requestSummary = """
            Create a new fridge.
            """;
    private static final String requestDescription = "${fridge.create.requestDescription}";
    private static final String okResponseCode = "200";
    private static final String okDescriptionTemplate = "${api.response-codes.ok.desc}";
    private static final String unauthorizedResponseCode = "401";
    private static final String unauthorizedDescription = "${fridge.create.unauthorized.desc}";
    private static final String serverErrorResponseCode = "500";
    private static final String serverErrorDescriptionTemplate = """
            ${api.response-codes.internalServerError.desc}
            """;
    Logger log = LoggerFactory.getLogger(FridgeController.class);

    @PostMapping(value = "/fridge/create", consumes = "application/json", produces = "application/json")
    @Tag(name = "fridge")
    @Operation(summary = requestSummary, description = requestDescription)
    @ApiResponse(responseCode = okResponseCode, description = okDescriptionTemplate, content = @Content)
    @ApiResponse(responseCode = unauthorizedResponseCode, description = unauthorizedDescription, content = @Content)
    @ApiResponse(responseCode = serverErrorResponseCode, description = serverErrorDescriptionTemplate, content = @Content)
    public ResponseEntity<String> createFridge(
        @RequestBody FridgeCreationRequest fridgeCreationRequest) throws Exception {
        int userId = fridgeCreationRequest.getUserId();
        String fridgeName = fridgeCreationRequest.getFridgeName();
        log.info(String.format("""
            Fridge creation request for user with id %d, fridge name: %s"
            """,
            userId, fridgeName));
        // TO-DO: fix bad values of userId and fridge name (always 0 and null)
        // TO-DO: check validity of user and refresh tokens
        DatabaseService database = new DatabaseService();
        database.createNewFridge(userId, fridgeName);
        return new ResponseEntity<String>(
            new ResponseBody(HttpStatus.OK).getResponseBody(),
            HttpStatus.OK);
    }
}
