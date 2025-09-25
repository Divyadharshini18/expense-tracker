package com.expenseTracker.model;
import java.time.LocalDate;

public class Expenses {
    
    private int id;
    private String description;
    private int amount;
    private LocalDate date;
    private String category;

    public Expenses() {
        
    }

    public Expenses(int id, String description, int amount, LocalDate date, String category) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    public Expenses(String description, int amount, LocalDate date, String category) {
    this.description = description;
    this.amount = amount;
    this.date = date;
    this.category = category;
}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

}
