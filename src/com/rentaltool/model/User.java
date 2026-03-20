package com.rentaltool.model;

public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private String role;

    public User() {}

    public User(int id, String username, String password, String email, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public int getId()            { return id; }
    public String getUsername()   { return username; }
    public String getPassword()   { return password; }
    public String getEmail()      { return email; }
    public String getRole()       { return role; }

    public void setId(int id)                 { this.id = id; }
    public void setUsername(String username)  { this.username = username; }
    public void setPassword(String password)  { this.password = password; }
    public void setEmail(String email)        { this.email = email; }
    public void setRole(String role)          { this.role = role; }

    public String toCSV() {
        return id + "," + username + "," + password + "," + email + "," + role;
    }

    public static User fromCSV(String line) {
        String[] parts = line.split(",");
        return new User(
            Integer.parseInt(parts[0].trim()),
            parts[1].trim(),
            parts[2].trim(),
            parts[3].trim(),
            parts[4].trim()
        );
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', role='" + role + "'}";
    }
}