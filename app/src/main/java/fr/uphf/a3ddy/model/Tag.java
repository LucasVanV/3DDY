package fr.uphf.a3ddy.model;

import com.google.gson.annotations.SerializedName;

public class Tag {
    @SerializedName("nom")
    private String nom;

    public Tag() {}

    public Tag(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
