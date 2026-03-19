## Programming Style

For programming in Java we use [Google style](https://google.github.io/styleguide/javaguide.html).

## Repository Structure

We focus on the "**Basic Folder Structure**" and for the most text files (such as documentation, README.file, etc.) we are going to use markdown file (.md).
[Learn more](https://medium.com/code-factory-berlin/github-repository-structure-best-practices-248e6effc405). 

## Naming Convention (branches and commits)

Suggested branches naming convetion: `task_number-task_name`.

Commit message naming convention: `task_number if implemented this commit will`.

To get task numbers, use: [Trello Card Numbers Plus](https://chrome.google.com/webstore/detail/trello-card-numbers-plus/ncibjlmfhjcjnphnpphgphbflpdpliei/related)

## API documentation

Swagger 3 (OpenAPI 3) is used for API documentation.

### Writing an API documentation
To create API documentation with OpenAPI you need Spring Annotations.
In your controller classes, use Spring annotations to define API endpoints and provide
additional metadata for the OpenAPI documentation. Some commonly used annotations include:
- `@RestController: Indicate that the class is a controller for handling REST requests.`
- `@RequestMapping: Define the base path for the controller's endpoints.`
- `@GetMapping, @PostMapping, etc.: Define specific HTTP methods and paths for your endpoints.`
- `@Operation: Provide descriptions for your API operations.`
- `@Parameter: Describe parameters for your API methods.`
- `@ApiResponse: Define responses for different scenarios.`
- `@ApiResponses: Group multiple @ApiResponse annotations.`

An example from our project ([commit 2f85f5f](https://github.com/Mateusz-Dobrzynski/meals-management-system/commit/2f85f5ff6199b43c5d2c144b8359664f07cb451f)):

```java
@RestController
public class VerificationService {
    //Variables omitted for brevity
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
            @RequestParam String registrationToken) {
```

To use and create templates present in this example (e.g. `${api.response-codes.ok.desc}`,
refer to `application.properties file`).

To customize API documentation use `@Bean` and `@Configuration` annotations.

### Reading the API documentation
To access the API documentation turn the server online and open
http://localhost:8080/swagger-ui/swagger-ui/index.html in your browser.

OpenAPI provides a feature which allows user to send POST, GET, DELETE and PUT methods
and receive a response. To access this feature, simply find desired endpoint
in the documentation and click on it.

### Additional literature
- [OpenAPI 3, additional examples](https://asbnotebook.com/spring-boot-openapi-3-example/)
- [Springdoc documentation](https://springdoc.org/)
