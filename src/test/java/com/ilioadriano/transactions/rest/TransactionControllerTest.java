package com.ilioadriano.transactions.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilioadriano.transactions.TestUtils;
import com.ilioadriano.transactions.domain.model.Account;
import com.ilioadriano.transactions.domain.model.Transaction;
import com.ilioadriano.transactions.domain.repository.AccountRepository;
import com.ilioadriano.transactions.domain.repository.TransactionRepository;
import com.ilioadriano.transactions.rest.dto.ErrorResponseDTO;
import com.ilioadriano.transactions.rest.dto.transaction.TransactionCreationDTO;
import com.ilioadriano.transactions.rest.dto.transaction.TransactionResponseDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"mysql-test", "test"})
class TransactionControllerTest {

    @LocalServerPort
    private int serverPort;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Rest assured config is mandatory to library works with snake case
     */
    @BeforeEach
    void setup() {
        RestAssured.port = serverPort;
        RestAssured.config = TestUtils.buildRestAssuredConfig(objectMapper);
    }

    @Test
    void shouldUnprocessableEntityIfNotFoundAccount() throws URISyntaxException, IOException {
        String transactionCreationJson = TestUtils.readStringFromResource("json/transaction-creation-create-success.json");
        TransactionCreationDTO transactionCreationDTO = objectMapper.readValue(transactionCreationJson, TransactionCreationDTO.class);
        transactionCreationDTO.setAccountId(999);

        ErrorResponseDTO errorResponseDTO = given()
                .body(transactionCreationDTO)
                .contentType(ContentType.JSON)
            .when()
                .post("/transactions")
            .then()
                .log().ifError()
            .and().assertThat()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .contentType(ContentType.JSON)
            .and().extract()
                .response().body().as(ErrorResponseDTO.class);

        assertThat(errorResponseDTO.getErrors()).hasSize(1);
        assertThat(errorResponseDTO.getErrors().getFirst().getCause()).isEqualTo("Entity not found");
        assertThat(errorResponseDTO.getErrors().getFirst().getMessage()).isEqualTo("Account not found with id 999");
    }

    @Test
    void shouldUnprocessableEntityIfNotFoundOperationType() throws URISyntaxException, IOException {
        String transactionCreationJson = TestUtils.readStringFromResource("json/transaction-creation-create-success.json");
        TransactionCreationDTO transactionCreationDTO = objectMapper.readValue(transactionCreationJson, TransactionCreationDTO.class);
        transactionCreationDTO.setOperationTypeId(999);

        ErrorResponseDTO errorResponseDTO = given()
                .body(transactionCreationDTO)
                .contentType(ContentType.JSON)
            .when()
                .post("/transactions")
            .then()
                .log().ifError()
            .and().assertThat()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .contentType(ContentType.JSON)
            .and().extract()
                .response().body().as(ErrorResponseDTO.class);

        assertThat(errorResponseDTO.getErrors()).hasSize(1);
        assertThat(errorResponseDTO.getErrors().getFirst().getCause()).isEqualTo("Entity not found");
        assertThat(errorResponseDTO.getErrors().getFirst().getMessage()).isEqualTo("OperationType not found with id 999");
    }

    @Test
    void shouldCreateCreditTransaction() throws URISyntaxException, IOException {
        Account account = new Account();
        account.setDocumentNumber(TestUtils.generateRandomDocumentNumber());
        accountRepository.saveAndFlush(account);

        String transactionCreationJson = TestUtils.readStringFromResource("json/transaction-creation-create-success.json");
        TransactionCreationDTO transactionCreationDTO = objectMapper.readValue(transactionCreationJson, TransactionCreationDTO.class);
        // In dirty database context case, it will not fail
        transactionCreationDTO.setAccountId(account.getId());

        TransactionResponseDTO transactionResponseDTO = given()
                .body(transactionCreationDTO)
                .contentType(ContentType.JSON)
            .when()
                .post("/transactions")
            .then()
                .log().ifError()
            .and().assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON)
            .and().extract()
                .response().body().as(TransactionResponseDTO.class);

        assertThat(transactionResponseDTO).isNotNull();
        assertThat(transactionResponseDTO.getAmount()).isEqualByComparingTo(transactionCreationDTO.getAmount());
        assertThat(transactionResponseDTO.getEventDate()).isNotNull();

        Transaction transaction = transactionRepository.findById(transactionResponseDTO.getId()).orElseThrow();
        assertThat(transaction.getAmount()).isEqualByComparingTo(transactionCreationDTO.getAmount());
        assertThat(transaction.getAccount().getId()).isEqualTo(transactionCreationDTO.getAccountId());
        assertThat(transaction.getOperationType().getId()).isEqualTo(transactionCreationDTO.getOperationTypeId());
    }

    @Test
    void shouldCreateDebitTransaction() throws URISyntaxException, IOException {
        Account account = new Account();
        account.setDocumentNumber(TestUtils.generateRandomDocumentNumber());
        accountRepository.saveAndFlush(account);

        String transactionCreationJson = TestUtils.readStringFromResource("json/transaction-creation-debit-success.json");
        TransactionCreationDTO transactionCreationDTO = objectMapper.readValue(transactionCreationJson, TransactionCreationDTO.class);
        // In dirty database context case, it will not fail
        transactionCreationDTO.setAccountId(account.getId());

        TransactionResponseDTO transactionResponseDTO = given()
                .body(transactionCreationDTO)
                .contentType(ContentType.JSON)
            .when()
                .post("/transactions")
            .then()
                .log().ifError()
            .and().assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON)
            .and().extract()
                .response().body().as(TransactionResponseDTO.class);

        assertThat(transactionResponseDTO).isNotNull();
        assertThat(transactionResponseDTO.getAmount()).isEqualByComparingTo(transactionCreationDTO.getAmount().negate());
        assertThat(transactionResponseDTO.getEventDate()).isNotNull();

        Transaction transaction = transactionRepository.findById(transactionResponseDTO.getId()).orElseThrow();
        assertThat(transaction.getAmount()).isEqualByComparingTo(transactionCreationDTO.getAmount().negate());
        assertThat(transaction.getAccount().getId()).isEqualTo(transactionCreationDTO.getAccountId());
        assertThat(transaction.getOperationType().getId()).isEqualTo(transactionCreationDTO.getOperationTypeId());
    }

    @Test
    void shouldNotCreateTransactionWithNegativeAmount() throws URISyntaxException, IOException {
        String transactionCreationJson = TestUtils.readStringFromResource("json/transaction-creation-debit-success.json");
        TransactionCreationDTO transactionCreationDTO = objectMapper.readValue(transactionCreationJson, TransactionCreationDTO.class);
        transactionCreationDTO.setAmount(BigDecimal.TEN.negate());

        ErrorResponseDTO errorResponseDTO = given()
                .body(transactionCreationDTO)
                .contentType(ContentType.JSON)
            .when()
                .post("/transactions")
            .then()
                .log().ifError()
            .and().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(ContentType.JSON)
            .and().extract()
                .response().body().as(ErrorResponseDTO.class);

        assertThat(errorResponseDTO.getErrors()).hasSize(1);
        assertThat(errorResponseDTO.getErrors().getFirst().getCause()).isEqualTo("amount");
        assertThat(errorResponseDTO.getErrors().getFirst().getMessage()).isEqualTo("must be greater than 0.0");
    }

}