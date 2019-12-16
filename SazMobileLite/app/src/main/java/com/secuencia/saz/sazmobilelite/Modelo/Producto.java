package com.secuencia.saz.sazmobilelite.Modelo;

public class Producto {

    private String color;
    private String acabado;
    private String marca;
    private String corrida;
    private String barcode;




    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAcabado() {
        return acabado;
    }

    public void setAcabado(String acabado) {
        this.acabado = acabado;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCorrida() {
        return corrida;
    }

    public void setCorrida(String corrida) {
        this.corrida = corrida;
    }


        @Override
        public String toString()
        {
            return color +", "+ acabado +", "+ marca + ", "+corrida+","+barcode;
        }


}
