package com.mareinc.marcospedraza.buscomida.models;

public class SaldoUsuario {

    double saldo;
    boolean activo;


    public SaldoUsuario(){}


    public SaldoUsuario(double saldo, boolean activo) {
        this.saldo = saldo;
        this.activo = activo;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
