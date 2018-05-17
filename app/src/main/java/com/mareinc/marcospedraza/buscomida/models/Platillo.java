package com.mareinc.marcospedraza.buscomida.models;

public class Platillo {



    String nom_platillo;
    String desc;
    String img_url;
    double precio;


    public Platillo() {
    }


    public Platillo(String nom_platillo, String desc, String img_url, double precio) {

        this.nom_platillo = nom_platillo;
        this.desc = desc;
        this.img_url = img_url;
        this.precio = precio;
    }




    public void setNom_platillo(String nom_platillo) {
        this.nom_platillo = nom_platillo;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getNom_platillo() {
        return nom_platillo;
    }

    public String getDesc() {
        return desc;
    }

    public String getImg_url() {
        return img_url;
    }

    public double getPrecio() {
        return precio;
    }
}
