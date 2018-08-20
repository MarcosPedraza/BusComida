package com.mareinc.marcospedraza.buscomida.models;

public class ItemCarrito {
    int cantidad;
    String id_platillo;
    double sub_total;
    String url_platillo_img;
    String id_item;

    public ItemCarrito() {
    }


    public ItemCarrito(int cantidad, String id_platillo, double sub_total, String url_platillo_img, String id_item) {
        this.cantidad = cantidad;
        this.id_platillo = id_platillo;
        this.sub_total = sub_total;
        this.url_platillo_img = url_platillo_img;
        this.id_item = id_item;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getId_platillo() {
        return id_platillo;
    }

    public void setId_platillo(String id_platillo) {
        this.id_platillo = id_platillo;
    }

    public double getSub_total() {
        return sub_total;
    }

    public void setSub_total(double sub_total) {
        this.sub_total = sub_total;
    }

    public String getUrl_platillo_img() {
        return url_platillo_img;
    }

    public void setUrl_platillo_img(String url_platillo_img) {
        this.url_platillo_img = url_platillo_img;
    }

    public String getId_item() {
        return id_item;
    }

    public void setId_item(String id_item) {
        this.id_item = id_item;
    }
}
