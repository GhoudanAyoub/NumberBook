package com.example.testret.Models;

import com.google.gson.annotations.SerializedName;

public class Contact {

    @SerializedName("id")
    private int id;
    @SerializedName("nom")
    private String nom;
    @SerializedName("telephone")
    private String telephone;
    @SerializedName("countryCode")
    private String countryCode;
    @SerializedName("email")
    private String email;

    public Contact(String nom, String telephone) {
        this.nom = nom;
        this.telephone = telephone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
