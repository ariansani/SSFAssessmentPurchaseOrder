package vttpnusiss.arian.SSFassessmentArianSani.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.RequestEntity.BodyBuilder;
import org.springframework.http.RequestEntity.HeadersBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import vttpnusiss.arian.SSFassessmentArianSani.models.Quotation;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class QuotationService {

    private static final Logger logger = LoggerFactory.getLogger(QuotationService.class);

    private static final String URL = "https://quotation.chuklee.com/quotation";

    public Optional<Quotation> getQuotations(List<String> items) {

        ResponseEntity<String> resp = null;

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        // request entity is created with request body and headers
        HttpEntity requestEntity = new HttpEntity<>(items, requestHeaders);
        RestTemplate restTemplate = new RestTemplate();

        try {

            resp = restTemplate.exchange(
                    URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);
            

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        if (resp.getStatusCodeValue() >= 400) {
            return Optional.empty();
        }

        try {
            // do something with this
            Quotation quote = Quotation.create(resp.getBody());
            return Optional.of(quote);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();

    }

}
