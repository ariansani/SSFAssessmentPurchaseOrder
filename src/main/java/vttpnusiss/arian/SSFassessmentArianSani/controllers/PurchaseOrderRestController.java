package vttpnusiss.arian.SSFassessmentArianSani.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttpnusiss.arian.SSFassessmentArianSani.models.Item;
import vttpnusiss.arian.SSFassessmentArianSani.models.Order;
import vttpnusiss.arian.SSFassessmentArianSani.models.Quotation;
import vttpnusiss.arian.SSFassessmentArianSani.services.QuotationService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class PurchaseOrderRestController {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderRestController.class);

    @Autowired
    QuotationService quoteSvc;

    @PostMapping(path = "/po", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postPO(@RequestBody String payload) {

        JsonObject responseJson;
        try (InputStream is = new ByteArrayInputStream(payload.getBytes(StandardCharsets.UTF_8))) {
            JsonReader reader = Json.createReader(is);
            JsonObject o = reader.readObject();

      
            

            String orderName = o.getString("name");
            String address = o.getString("address");
            String email = o.getString("email");
            List<Item> itemsList = new LinkedList<>();
            List<String> itemsQuote = new ArrayList<>();

            Order custOrder = new Order();
            custOrder.setName(o.getString("name"));
            custOrder.setAddress(o.getString("address"));
            custOrder.setEmail(o.getString("email"));
            custOrder.setTotalCost(0.00f);

            JsonArray lineItems = o.getJsonArray("lineItems");

            o.getJsonArray("lineItems")
                    .stream()
                    .map(v -> (JsonObject) v)
                    .forEach((JsonObject v) -> {
                        Item item = new Item();
                        item.setItemName(v.getString("item"));
                        item.setQuantity(v.getInt("quantity"));
                        itemsQuote.add(v.getString("item"));
                        itemsList.add(item);
                    });

            Optional<Quotation> quote = quoteSvc.getQuotations(itemsQuote);

            if (quote.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Quotation returnedQuote = quote.get();
            List<Float> totalCostList = new LinkedList<>();
            Float totalCost;

            logger.info("This is my items name" + itemsList.get(0).getItemName());
            logger.info("This is my items quantity " + itemsList.get(0).getQuantity().toString());
            logger.info("This is my quotations" + returnedQuote.getQuotation(itemsList.get(0).getItemName()));
            
            logger.info("This is my quotations size" + returnedQuote.getQuotations().size());
            for (int i =0; i< returnedQuote.getQuotations().size(); i++){
                Float startingBalance = custOrder.getTotalCost(); 
                logger.info("This is my starting Balance" + startingBalance.toString());
                String itemName = itemsList.get(i).getItemName();
                Integer itemQty = itemsList.get(i).getQuantity();
                Float quotePrice = returnedQuote.getQuotation(itemName);
                Float singleCost = quotePrice * itemQty;
                Float addOrder = startingBalance + singleCost;
                custOrder.setTotalCost(addOrder);
      
            }
                logger.info("This is my totalCost"+ custOrder.getTotalCost().toString());


            responseJson = Json.createObjectBuilder()
                    .add("invoiceId", returnedQuote.getQuoteId())
                    .add("name", custOrder.getName())
                    .add("total", custOrder.getTotalCost())
                    .build();

        } catch (Exception e) {
            // TODO: handle exception
            JsonObject errJson = Json.createObjectBuilder()
                    .add("error", e.getMessage()).build();
            return ResponseEntity.status(400).body(errJson.toString());
        }

        return ResponseEntity.ok(responseJson.toString());
    }

}
