package com.tcc.idadeativa.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tcc.idadeativa.objetos.Pessoa;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class DAO extends SQLiteOpenHelper {

    public DAO (Context context){
        super(context, "banco", null, 2);
    }

    //RODA NA PRIMEIRA EXECUÇÃO DA APLICAÇÃO PARA CRIAR O BANCO DE DADOS
    @Override
    public void onCreate (SQLiteDatabase db){
        String sql = "CREATE TABLE pessoa(pessoa_id Integer PRIMARY KEY, nome TEXT, sexo TEXT, nomeSUS TEXT, dataNascimento TEXT, numSUS Integer, foto String);";
        db.execSQL(sql);
    }

    //APAGA O DATABASE EXISTENTE E CRIA OUTRO COM UMA VERSAO MAIS NOVA EM CASO DE ALTERAÇÕES
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        String sql = "DROP TABLE IF EXISTS pessoa;";
        db.execSQL(sql);
        onCreate(db);
    }

    //METODO QUE INSERE PESSOA NO BANCO
    public void inserePessoa(Pessoa pessoa){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = new ContentValues();
        dados.put("nome", pessoa.getNome());
        dados.put("sexo", pessoa.getSexo());
        dados.put("nomeSUS", pessoa.getNomeSUS());
        dados.put("dataNascimento", pessoa.getDataNascimento());
        dados.put("numSUS", pessoa.getNumSUS());
        dados.put("foto", pessoa.getFoto());

        db.insert("pessoa", null, dados);
    }

    @SuppressLint("Range")
    public List<Pessoa> buscaPessoa(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM pessoa;";

        Cursor c = db.rawQuery(sql, null);

        List<Pessoa> pessoas = new ArrayList<Pessoa>();

        while (c.moveToNext()){
            Pessoa pessoa = new Pessoa();
            pessoa.setNome(c.getString(c.getColumnIndex("nome")));
            pessoa.setSexo(c.getString(c.getColumnIndex("sexo")));
            pessoa.setNomeSUS(c.getString(c.getColumnIndex("nomeSUS")));
            pessoa.setDataNascimento(c.getString(c.getColumnIndex("dataNascimento")));
            pessoa.setNumSUS(Integer.valueOf(c.getString(c.getColumnIndex("numSUS"))));
            pessoa.setFoto(c.getString(c.getColumnIndex("foto")));
            pessoas.add(pessoa);
        }
        return pessoas;
    }
}
