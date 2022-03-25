package vttpnusiss.arian.SSFassessmentArianSani;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttpnusiss.arian.SSFassessmentArianSani.controllers.PurchaseOrderRestController;
import vttpnusiss.arian.SSFassessmentArianSani.models.Quotation;
import vttpnusiss.arian.SSFassessmentArianSani.services.QuotationService;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class SsFassessmentArianSaniApplicationTests {


	@Autowired
	private PurchaseOrderRestController poCtrl;

	@Autowired
	private QuotationService quoteSvc;

	@Autowired
	MockMvc mvc;

	@Test
	void contextLoads() {
		assertNotNull(quoteSvc);
	}


	@Test
	void testQuoteSvc() throws Exception {	

		JsonArray reqPayload = Json.createArrayBuilder()
		.add("durian")
		.add("plum")
		.add("pear")
		.build();

		HttpHeaders requestHeaders = new HttpHeaders();
				requestHeaders.setContentType(MediaType.APPLICATION_JSON);
				requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		
				RequestBuilder req = MockMvcRequestBuilders.post("https://quotation.chuklee.com/quotation")
				.header("User-Agent", "junit")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(reqPayload.toString());

		// Make the invocation
		MvcResult resp = mvc.perform(req).andReturn();
		MockHttpServletResponse httpResp = resp.getResponse();

		// Check the status code
		assertEquals(200, httpResp.getStatus());

		// Check the payload
		Optional<JsonObject> opt = string2Json(httpResp.getContentAsString());
		assertTrue(opt.isPresent());

		JsonObject obj = opt.get();
		for (String s: List.of("invoiceId", "name", "total"))
			assertFalse(obj.isNull(s));

	}

	public static Optional<JsonObject> string2Json(String s) {
		try (InputStream is = new ByteArrayInputStream(s.getBytes())) {
			JsonReader reader = Json.createReader(is);
			return Optional.of(reader.readObject());
		} catch (Exception ex) {
			System.err.printf("Error: %s\n", ex.getMessage());
			return Optional.empty();
		}
	}




}
