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
// teste
public class DAO extends SQLiteOpenHelper {

    public DAO (Context context){
        super(context, "banco", null, 4);
    }

    //RODA NA PRIMEIRA EXECUÇÃO DA APLICAÇÃO PARA CRIAR O BANCO DE DADOS
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Cria a tabela pessoa
        String sqlPessoa = "CREATE TABLE pessoa (pessoa_ID INTEGER PRIMARY KEY, pessoa_nome TEXT, pessoa_sexo TEXT, pessoa_nomeSUS TEXT, pessoa_dataNascimento DATE, pessoa_foto TEXT, pessoa_numSUS TEXT);";
        db.execSQL(sqlPessoa);

        // Cria a tabela doenca
        String sqlDoenca = "CREATE TABLE doenca (doenca_ID INTEGER PRIMARY KEY, doenca_nome TEXT);";
        db.execSQL(sqlDoenca);

        // Cria a tabela medicao
        String sqlMedicao = "CREATE TABLE medicao (medicao_data DATE, medicao_valor REAL, medicao_pessoaID INTEGER, medicao_doencaID INTEGER, FOREIGN KEY (medicao_pessoaID) REFERENCES pessoa(pessoa_ID), FOREIGN KEY (medicao_doencaID) REFERENCES doenca(doenca_ID), PRIMARY KEY (medicao_data, medicao_valor, medicao_pessoaID, medicao_doencaID));";
        db.execSQL(sqlMedicao);

        // Cria a tabela pessoa_doenca
        String sqlPessoaDoenca = "CREATE TABLE pessoa_doenca (pessoaDoenca_pessoaID INTEGER, pessoaDoenca_doencaID INTEGER, FOREIGN KEY (pessoaDoenca_pessoaID) REFERENCES pessoa(pessoa_ID), FOREIGN KEY (pessoaDoenca_doencaID) REFERENCES doenca(doenca_ID), PRIMARY KEY (pessoaDoenca_pessoaID, pessoaDoenca_doencaID));";
        db.execSQL(sqlPessoaDoenca);
    }


    //APAGA O DATABASE EXISTENTE E CRIA OUTRO COM UMA VERSAO MAIS NOVA EM CASO DE ALTERAÇÕES
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        String sqlPessoa = "DROP TABLE IF EXISTS pessoa;";
        db.execSQL(sqlPessoa);

        String sqlDoenca = "DROP TABLE IF EXISTS doenca;";
        db.execSQL(sqlDoenca);

        String sqlMedicao = "DROP TABLE IF EXISTS medicao;";
        db.execSQL(sqlMedicao);

        String sqlPessoaDoenca = "DROP TABLE IF EXISTS pessoa_doenca;";
        db.execSQL(sqlPessoaDoenca);

        onCreate(db);
    }

    //METODO QUE INSERE PESSOA NO BANCO
    public void inserePessoa(Pessoa pessoa){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = new ContentValues();
        dados.put("nome", pessoa.getPessoa_nome());
        dados.put("sexo", pessoa.getPessoa_sexo());
        dados.put("nomeSUS", pessoa.getPessoa_nomeSUS());
        dados.put("dataNascimento", pessoa.getPessoa_dataNascimento()); // resolver problema de String =/= date
        dados.put("numSUS", pessoa.getPessoa_numSUS());
        dados.put("foto", pessoa.getPessoa_foto());

        db.insert("pessoa", null, dados);
    }

    @SuppressLint("Range")
    public List<Pessoa> buscaPessoa(){
        SQLiteDatabase db = getReadableDatabase();
        String sqlPessoa = "SELECT * FROM pessoa;";

        Cursor c = db.rawQuery(sqlPessoa, null);

        List<Pessoa> pessoas = new ArrayList<Pessoa>();

        while (c.moveToNext()){
            Pessoa pessoa = new Pessoa();
            pessoa.setPessoa_nome(c.getString(c.getColumnIndex("nome")));
            pessoa.setPessoa_sexo(c.getString(c.getColumnIndex("sexo")));
            pessoa.setPessoa_nomeSUS(c.getString(c.getColumnIndex("nomeSUS")));
            pessoa.setPessoa_dataNascimento(c.getString(c.getColumnIndex("dataNascimento")));
            pessoa.setPessoa_numSUS(c.getString(c.getColumnIndex("numSUS")));
            pessoa.setPessoa_foto(c.getString(c.getColumnIndex("foto")));
            pessoas.add(pessoa);
        }
        return pessoas;
    }
}
