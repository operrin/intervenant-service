package org.miage.intervenantservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.miage.intervenantservice.entity.Intervenant;
import org.miage.intervenantservice.boundary.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.Optional;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class IntervenantServiceApplicationTests {

	@LocalServerPort
	int port;

	@Autowired
	IntervenantRepository ir;

	@BeforeEach
	public void setupContext() {
		ir.deleteAll();
		RestAssured.port = port;
	}

	@Test
	void ping() {
		when().get("/intervenants").then().statusCode(HttpStatus.SC_OK);
	}

	@Test
	void getAllApi() {
		// given
		Intervenant i1 = new Intervenant(UUID.randomUUID().toString(),"Sawyer","Tom","Nancy","54000");
		ir.save(i1);
		Intervenant i2 = new Intervenant(UUID.randomUUID().toString(),"Blanc","Robert","Les Arcs","73000");
		ir.save(i2);
		// when
		when().get("/intervenants").then().statusCode(HttpStatus.SC_OK)
			.and().assertThat().body("size()",equalTo(2));
	}

	@Test
	void getOneApi() {
		// given
		Intervenant i1 = new Intervenant(UUID.randomUUID().toString(),"Sawyer","Tom","Nancy","54000");
		ir.save(i1);
		// when
		Response response = when().get("/intervenants/"+i1.getId()).then()
			.statusCode(HttpStatus.SC_OK).extract().response();
		String jsonAsString = response.asString();
		// then
		assertThat(jsonAsString,containsString("Tom"));
	}

	@Test
	void getNotFoundApi() {
		// given - vide
		// when
		// then
		when().get("/intervenants/150").then().statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	void postApi() throws Exception {
		Intervenant i1 = new Intervenant(UUID.randomUUID().toString(),"Sawyer","Tom","Nancy","54000");
		Response response = given().body(toJsonString(i1)).contentType(ContentType.JSON)
				.when().post("/intervenants").then().statusCode(HttpStatus.SC_CREATED)
				.extract().response();
		String location = response.getHeader("Location");
		when().get(location).then().statusCode(HttpStatus.SC_OK);
	}

	@Test
	void putBadRequestApi() throws Exception {
		Intervenant i1 = new Intervenant(UUID.randomUUID().toString(), "Sawyer", "Tom", "Nancy", "54000");
		ir.save(i1);
		given().body(Optional.ofNullable(null)).contentType(ContentType.JSON)
				.when().put("intervenants/" + i1.getId())
				.then().statusCode(HttpStatus.SC_BAD_REQUEST);
	}

	@Test
	void putBadIdApi() throws Exception {
		Intervenant i1 = new Intervenant(UUID.randomUUID().toString(), "Sawyer", "Tom", "Nancy", "54000");
		ir.save(i1);
		given().body(this.toJsonString(i1)).contentType(ContentType.JSON)
				.when().put("intervenants/42")
				.then().statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	void putApi() throws Exception {
		Intervenant i1 = new Intervenant(UUID.randomUUID().toString(),"Sawyer","Tom","Nancy","54000");
		ir.save(i1);
		i1.setCommune("Arbois");
		i1.setCodepostal("39000");
		given().body(this.toJsonString(i1)).contentType(ContentType.JSON)
			.when().put("intervenants/"+i1.getId()).then().statusCode(HttpStatus.SC_OK);
		given().headers("Accept", ContentType.JSON).when().get("intervenants/"+i1.getId())
			.then().statusCode(HttpStatus.SC_OK)
			.and().assertThat().body("codepostal", equalTo(i1.getCodepostal()));
	}

	@Test
	void deleteApi() {
		Intervenant i1 = new Intervenant(UUID.randomUUID().toString(),"Sawyer","Tom","Nancy","54000");
		ir.save(i1);
		when().delete("intervenants/"+i1.getId()).then().statusCode(HttpStatus.SC_NO_CONTENT);
		when().get("intervenants/"+i1.getId()).then().statusCode(HttpStatus.SC_NOT_FOUND);
	}

	private String toJsonString(Object r) throws Exception {
		ObjectMapper map = new ObjectMapper();
		return map.writeValueAsString(r);
	}
}