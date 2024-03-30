package com.tcc.idadeativa.objetos;

import java.util.Date;

public class Medicao {

    Date data;
    double valor;
    int userId;
    int diseaseId;

    public Date getData() {
        return data;
    }
    public void setData(Date data) {
        this.data = data;
    }
    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getDiseaseId() {
        return diseaseId;
    }
    public void setDiseaseId(int diseaseId) {
        this.diseaseId = diseaseId;
    }
}
