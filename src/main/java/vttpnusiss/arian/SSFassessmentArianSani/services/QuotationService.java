package vttpnusiss.arian.SSFassessmentArianSani.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import vttpnusiss.arian.SSFassessmentArianSani.models.Quotation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            Quotation quote = create(resp.getBody());
            
            return Optional.of(quote);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();

    }

    public static Quotation create(String json) throws IOException{
        
        Quotation q = new Quotation();
       
        try(InputStream is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))){
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            q.setQuoteId(o.getString("quoteId"));
            JsonArray arr = o.getJsonArray("quotations");
            Map<String,Float> mapThis = new HashMap<>(); 
            arr.stream()
            .map(v-> (JsonObject)v)
            .forEach(v -> {
               logger.info("this is the value of quotation map"+ v.get("unitPrice"));
               mapThis.put(v.getString("item"), Float.parseFloat(v.get("unitPrice").toString()));
            });
            q.setQuotations(mapThis);
        }
        
        return q;
    }

    

}
