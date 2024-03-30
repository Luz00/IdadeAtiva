package com.tcc.idadeativa.objetos;

import java.util.List;

public class Pessoa {

    String nome, sexo, nomeSUS, dataNascimento, foto;
    Integer numSUS, pessoa_id;
    List<String> doencasPessoa;


    public List<String> getDoencasPessoa() {
        return doencasPessoa;
    }
    public void setDoencasPessoa(List<String> doencasPessoa) {
        this.doencasPessoa = doencasPessoa;
    }
    public Integer getPessoa_id() {
        return pessoa_id;
    }
    public void setPessoa_id(Integer pessoa_id) {
        this.pessoa_id = pessoa_id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getSexo() {
        return sexo;
    }
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    public String getNomeSUS() {
        return nomeSUS;
    }
    public void setNomeSUS(String nomeSUS) {
        this.nomeSUS = nomeSUS;
    }
    public String getDataNascimento() {
        return dataNascimento;
    }
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    public Integer getNumSUS() {
        return numSUS;
    }
    public void setNumSUS(Integer numSUS) {
        this.numSUS = numSUS;
    }
    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }
}
