package com.ilioadriano.transactions.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilioadriano.transactions.TestUtils;
import com.ilioadriano.transactions.domain.model.Account;
import com.ilioadriano.transactions.domain.repository.AccountRepository;
import com.ilioadriano.transactions.rest.dto.ErrorResponseDTO;
import com.ilioadriano.transactions.rest.dto.account.AccountCreationDTO;
import com.ilioadriano.transactions.rest.dto.account.AccountResponseDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"mysql-test", "test"})
class AccountControllerTest {

    @LocalServerPort
    private Integer serverPort;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Rest assured config is mandatory to library works with snake case
     */
    @BeforeEach
    void beforeEach() {
        RestAssured.port = serverPort;
        RestAssured.config = TestUtils.buildRestAssuredConfig(objectMapper);
    }

    @Test
    void shouldCreateAccountByApi() throws IOException, URISyntaxException {
        String accountCreationJson = TestUtils.readStringFromResource("json/account-creation-success.json");
        AccountCreationDTO accountCreationDTO = objectMapper.readValue(accountCreationJson, AccountCreationDTO.class);

        AccountResponseDTO accountResponseDTO = given()
                    .contentType(ContentType.JSON)
                    .body(accountCreationJson)
                .when()
                    .post("/accounts")
                .then().assertThat()
                    .statusCode(HttpStatus.CREATED.value())
                    .contentType(ContentType.JSON)
                .and().extract()
                    .response().body().as(AccountResponseDTO.class);

        assertThat(accountResponseDTO).isNotNull();
        assertThat(accountResponseDTO.getId()).isNotNull();
        assertThat(accountResponseDTO.getDocumentNumber()).isEqualTo(accountCreationDTO.getDocumentNumber());


        Account databaseAccount = accountRepository.findById(accountResponseDTO.getId()).orElseThrow();
        assertThat(databaseAccount.getDocumentNumber()).isEqualTo(accountCreationDTO.getDocumentNumber());
    }

    @Test
    void shouldThrowErrorOnDuplicatedDocument() throws IOException, URISyntaxException {
        String accountCreationJson = TestUtils.readStringFromResource("json/account-creation-success.json");
        AccountCreationDTO accountCreationDTO = objectMapper.readValue(accountCreationJson, AccountCreationDTO.class);

        given()
                .contentType(ContentType.JSON)
                .body(accountCreationJson)
            .when()
                .post("/accounts")
            .then().log().ifError();

        ErrorResponseDTO errorResponseDTO = given()
                    .contentType(ContentType.JSON)
                    .body(accountCreationJson)
                .when()
                    .post("/accounts")
                .then().assertThat()
                    .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .contentType(ContentType.JSON)
                .extract().response()
                    .body().as(ErrorResponseDTO.class);

        assertThat(errorResponseDTO.getErrors()).hasSize(1);
        assertThat(errorResponseDTO.getErrors().getFirst().getCause()).isEqualTo("Duplicate entry");
        assertThat(errorResponseDTO.getErrors().getFirst().getMessage()).isEqualTo("Already exists an Account with this document number");
    }

    @Test
    void shouldGetAccountById() {
        String documentNumber = TestUtils.generateRandomDocumentNumber();

        Account account = new Account();
        account.setDocumentNumber(documentNumber);

        accountRepository.saveAndFlush(account);

        AccountResponseDTO accountResponseDTO = given()
                    .contentType(ContentType.JSON)
                .when()
                    .get("/accounts/" + account.getId())
                .then().assertThat()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(ContentType.JSON)
                .and().extract()
                    .response().body().as(AccountResponseDTO.class);

        assertThat(accountResponseDTO.getId()).isEqualTo(account.getId());
        assertThat(accountResponseDTO.getDocumentNumber()).isEqualTo(documentNumber);
    }

}