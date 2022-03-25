package vttpnusiss.arian.SSFassessmentArianSani.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

// Important. You cannot modify this file

public class Quotation {

    private String quoteId;
    private Map<String, Float> quotations = new HashMap<>();

    public String getQuoteId() {
        return quoteId;
    }
    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    public Map<String, Float> getQuotations() {
        return quotations;
    }
    public void setQuotations(Map<String, Float> quotations) {
        this.quotations = quotations;
    }
    public void addQuotation(String item, Float unitPrice) {
        this.quotations.put(item, unitPrice);
    }
    public Float getQuotation(String item) {
        return this.quotations.getOrDefault((Object)item, -1000000f);
    }



    static final Logger logger = LoggerFactory.getLogger(Quotation.class);
   
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