package com.arrenda.common

import com.google.gson.Gson
import io.restassured.RestAssured
import io.restassured.parsing.Parser
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = ["/sql/clean-db.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BaseIT {
    val gson: Gson = Gson()

    @LocalServerPort
    private var port: Int? = null

    @BeforeEach
    fun setUp() {
        RestAssured.port = port ?: throw IllegalStateException("Port is not initialized")
        RestAssured.defaultParser = Parser.JSON
    }
}