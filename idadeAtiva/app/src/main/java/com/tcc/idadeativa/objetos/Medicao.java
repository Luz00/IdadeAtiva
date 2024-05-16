package com.tcc.idadeativa.objetos;

import java.util.Date;

public class Medicao {

    Date medicao_data;
    Double medicao_valor;
    Integer medicao_pessoaID;
    Integer medicao_doencaID;

    public Date getMedicao_data() {
        return medicao_data;
    }

    public void setMedicao_data(Date medicao_data) {
        this.medicao_data = medicao_data;
    }

    public Double getMedicao_valor() {
        return medicao_valor;
    }

    public void setMedicao_valor(Double medicao_valor) {
        this.medicao_valor = medicao_valor;
    }

    public Integer getMedicao_pessoaID() {
        return medicao_pessoaID;
    }

    public void setMedicao_pessoaID(Integer medicao_pessoaID) {
        this.medicao_pessoaID = medicao_pessoaID;
    }

    public Integer getMedicao_doencaID() {
        return medicao_doencaID;
    }

    public void setMedicao_doencaID(Integer medicao_doencaID) {
        this.medicao_doencaID = medicao_doencaID;
    }
}
