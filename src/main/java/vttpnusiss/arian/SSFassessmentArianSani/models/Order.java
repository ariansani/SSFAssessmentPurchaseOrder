package vttpnusiss.arian.SSFassessmentArianSani.models;

import java.util.List;

public class Order {
    private String name;
    private String address;
    private String email;
    private List<Item> itemsList;
    private Float totalCost;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public List<Item> getItemsList() {
        return itemsList;
    }
    public void setItemsList(List<Item> itemsList) {
        this.itemsList = itemsList;
    }

    public Float getTotalCost() {
        return totalCost;
    }
    public void setTotalCost(Float totalCost) {
        this.totalCost = totalCost;
    }
    

    
}
