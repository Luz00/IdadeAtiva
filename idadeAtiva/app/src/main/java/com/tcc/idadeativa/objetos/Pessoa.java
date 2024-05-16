package com.tcc.idadeativa.objetos;

import java.io.Serializable;
import java.util.List;

public class Pessoa implements Serializable {

    String pessoa_nome;
    String pessoa_sexo;
    String pessoa_nomeSUS;
    String pessoa_dataNascimento;
    String pessoa_foto;
    String pessoa_numSUS;
    Integer pessoa_ID;
//
    public String getPessoa_nome() {
        return pessoa_nome;
    }
    public void setPessoa_nome(String pessoa_nome) {
        this.pessoa_nome = pessoa_nome;
    }
    public String getPessoa_sexo() {
        return pessoa_sexo;
    }
    public void setPessoa_sexo(String pessoa_sexo) {
        this.pessoa_sexo = pessoa_sexo;
    }
    public String getPessoa_nomeSUS() {
        return pessoa_nomeSUS;
    }
    public void setPessoa_nomeSUS(String pessoa_nomeSUS) {
        this.pessoa_nomeSUS = pessoa_nomeSUS;
    }
    public String getPessoa_dataNascimento() {
        return pessoa_dataNascimento;
    }
    public void setPessoa_dataNascimento(String pessoa_dataNascimento) {
        this.pessoa_dataNascimento = pessoa_dataNascimento;
    }
    public String getPessoa_foto() {
        return pessoa_foto;
    }

    public void setPessoa_foto(String pessoa_foto) {
        this.pessoa_foto = pessoa_foto;
    }

    public String getPessoa_numSUS() {
        return pessoa_numSUS;
    }

    public void setPessoa_numSUS(String pessoa_numSUS) {
        this.pessoa_numSUS = pessoa_numSUS;
    }

    public Integer getPessoa_ID() {
        return pessoa_ID;
    }

    public void setPessoa_ID(Integer pessoa_ID) {
        this.pessoa_ID = pessoa_ID;
    }

}
