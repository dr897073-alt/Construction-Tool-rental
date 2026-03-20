package com.rentaltool.model;

public class Rental {

    private int id;
    private int toolId;
    private String toolName;
    private String customerName;
    private String customerPhone;
    private String rentDate;
    private String returnDate;
    private int days;
    private double totalPrice;
    private String status;

    public Rental() {}

    public Rental(int id, int toolId, String toolName, String customerName,
                  String customerPhone, String rentDate, String returnDate,
                  int days, double totalPrice, String status) {
        this.id = id;
        this.toolId = toolId;
        this.toolName = toolName;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.rentDate = rentDate;
        this.returnDate = returnDate;
        this.days = days;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public int getId()                { return id; }
    public int getToolId()            { return toolId; }
    public String getToolName()       { return toolName; }
    public String getCustomerName()   { return customerName; }
    public String getCustomerPhone()  { return customerPhone; }
    public String getRentDate()       { return rentDate; }
    public String getReturnDate()     { return returnDate; }
    public int getDays()              { return days; }
    public double getTotalPrice()     { return totalPrice; }
    public String getStatus()         { return status; }

    public void setId(int id)                        { this.id = id; }
    public void setToolId(int toolId)                { this.toolId = toolId; }
    public void setToolName(String toolName)         { this.toolName = toolName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setCustomerPhone(String phone)       { this.customerPhone = phone; }
    public void setRentDate(String rentDate)         { this.rentDate = rentDate; }
    public void setReturnDate(String returnDate)     { this.returnDate = returnDate; }
    public void setDays(int days)                    { this.days = days; }
    public void setTotalPrice(double totalPrice)     { this.totalPrice = totalPrice; }
    public void setStatus(String status)             { this.status = status; }
}