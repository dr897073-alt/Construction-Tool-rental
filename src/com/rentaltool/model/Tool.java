package com.rentaltool.model;

public class Tool {

    private int id;
    private String name;
    private String category;
    private double pricePerDay;
    private int quantity;
    private String status;

    public Tool() {}

    public Tool(int id, String name, String category,
                double pricePerDay, int quantity, String status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.pricePerDay = pricePerDay;
        this.quantity = quantity;
        this.status = status;
    }

    public int getId()              { return id; }
    public String getName()         { return name; }
    public String getCategory()     { return category; }
    public double getPricePerDay()  { return pricePerDay; }
    public int getQuantity()        { return quantity; }
    public String getStatus()       { return status; }

    public void setId(int id)                   { this.id = id; }
    public void setName(String name)            { this.name = name; }
    public void setCategory(String category)    { this.category = category; }
    public void setPricePerDay(double price)    { this.pricePerDay = price; }
    public void setQuantity(int quantity)       { this.quantity = quantity; }
    public void setStatus(String status)        { this.status = status; }

    public String toCSV() {
        return id + "," + name + "," + category + "," +
               pricePerDay + "," + quantity + "," + status;
    }

    public static Tool fromCSV(String line) {
        String[] parts = line.split(",");
        return new Tool(
            Integer.parseInt(parts[0].trim()),
            parts[1].trim(),
            parts[2].trim(),
            Double.parseDouble(parts[3].trim()),
            Integer.parseInt(parts[4].trim()),
            parts[5].trim()
        );
    }

    @Override
    public String toString() {
        return name + " (" + category + ") - Rs." + pricePerDay + "/day";
    }
}