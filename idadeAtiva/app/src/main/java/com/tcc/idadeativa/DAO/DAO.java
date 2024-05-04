package com.tcc.idadeativa.DAO;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tcc.idadeativa.objetos.Medicao;
import com.tcc.idadeativa.objetos.Pessoa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DAO extends SQLiteOpenHelper {

    public DAO (Context context){
        super(context, "banco", null, 52);
    }

    //RODA NA PRIMEIRA EXECUÇÃO DA APLICAÇÃO PARA CRIAR O BANCO DE DADOS
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Cria a tabela pessoa
        //String sqlPessoa = "CREATE TABLE pessoa (pessoa_ID INTEGER PRIMARY KEY AUTOINCREMENT, pessoa_nome TEXT, pessoa_sexo TEXT, pessoa_nomeSUS TEXT, pessoa_dataNascimento DATE, pessoa_foto TEXT, pessoa_numSUS TEXT, doenca_pessoa TEXT);";
        String sqlPessoa = "CREATE TABLE pessoa (pessoa_ID INTEGER PRIMARY KEY AUTOINCREMENT, pessoa_nome TEXT, pessoa_sexo TEXT, pessoa_nomeSUS TEXT, pessoa_dataNascimento DATE, pessoa_foto TEXT, pessoa_numSUS TEXT);";
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

        String sqlCriaDoenca = "INSERT INTO doenca (doenca_ID, doenca_nome) VALUES (1, 'Arritimia'), (2, 'Depressão'), (3, 'Diabetes'), (4, 'Hipertensão'),  (5, 'Hipotensão');";
        db.execSQL(sqlCriaDoenca);
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
    public long inserePessoa(Pessoa pessoa) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = new ContentValues();
        dados.put("pessoa_nome", pessoa.getPessoa_nome());
        dados.put("pessoa_sexo", pessoa.getPessoa_sexo());
        dados.put("pessoa_nomeSUS", pessoa.getPessoa_nomeSUS());
        dados.put("pessoa_dataNascimento", pessoa.getPessoa_dataNascimento());
        dados.put("pessoa_numSUS", pessoa.getPessoa_numSUS());
        dados.put("pessoa_foto", pessoa.getPessoa_foto());
        long idUsuario = db.insert("pessoa", null, dados);
        db.close(); // Não se esqueça de fechar o banco de dados após a inserção
        return idUsuario;
    }


    // Dentro da classe DAO

    @SuppressLint("Range")
    public List<Pessoa> buscaPessoa() {
        SQLiteDatabase db = getReadableDatabase();
        String sqlPessoa = "SELECT * FROM pessoa;";
        Cursor c = db.rawQuery(sqlPessoa, null);
        List<Pessoa> pessoas = new ArrayList<Pessoa>();

        while (c.moveToNext()) {
            Pessoa pessoa = new Pessoa();
            pessoa.setPessoa_ID(Integer.valueOf(c.getString(c.getColumnIndex("pessoa_ID"))));
            pessoa.setPessoa_nome(c.getString(c.getColumnIndex("pessoa_nome")));
            pessoa.setPessoa_sexo(c.getString(c.getColumnIndex("pessoa_sexo")));
            pessoa.setPessoa_nomeSUS(c.getString(c.getColumnIndex("pessoa_nomeSUS")));
            pessoa.setPessoa_dataNascimento(c.getString(c.getColumnIndex("pessoa_dataNascimento")));
            pessoa.setPessoa_numSUS(c.getString(c.getColumnIndex("pessoa_numSUS")));
            pessoa.setPessoa_foto(c.getString(c.getColumnIndex("pessoa_foto")));
            pessoas.add(pessoa);
        }
        c.close();
        return pessoas;
    }
    @SuppressLint("Range")
    public List<String> buscarNomesDoencas() {
        List<String> nomesDoencas = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String[] colunas = {"doenca_nome"};
        Cursor cursor = db.query("doenca", colunas, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String nomeDoenca = cursor.getString(cursor.getColumnIndex("doenca_nome"));
            nomesDoencas.add(nomeDoenca);
        }
        cursor.close();
        db.close();
        return nomesDoencas;
    }

    public void inserePessoaDoenca(int idUsuario, int idDoenca) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pessoaDoenca_pessoaID", idUsuario);
        values.put("pessoaDoenca_doencaID", idDoenca);

        db.insert("pessoa_doenca", null, values);
        db.close();
    }

    @SuppressLint("Range")
    public List<String> buscarDoencasDoUsuario(int idUsuario) {
        List<String> nomesDoencas = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT d.doenca_nome " +
                "FROM pessoa_doenca pd " +
                "JOIN doenca d ON pd.pessoaDoenca_doencaID = d.doenca_ID " +
                "WHERE pd.pessoaDoenca_pessoaID = ?;";
        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(idUsuario)});

        while (cursor.moveToNext()) {
            String nomeDoenca = cursor.getString(cursor.getColumnIndex("doenca_nome"));
            nomesDoencas.add(nomeDoenca);
        }
        cursor.close();
        db.close();
        return nomesDoencas;
    }
    @SuppressLint("Range")
    public String buscarSexoDaPessoa(int idUsuario) {
        SQLiteDatabase db = getReadableDatabase();
        String sexo = "";

        String query = "SELECT pessoa_sexo FROM pessoa WHERE pessoa_ID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idUsuario)});

        if (cursor.moveToFirst()) {
            sexo = cursor.getString(cursor.getColumnIndex("pessoa_sexo"));
        }

        cursor.close();
        db.close();

        return sexo;
    }

    public boolean atualizarPessoa(Pessoa pessoa) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("pessoa_nome", pessoa.getPessoa_nome());
        valores.put("pessoa_sexo", pessoa.getPessoa_sexo());
        valores.put("pessoa_nomeSUS", pessoa.getPessoa_nomeSUS());
        valores.put("pessoa_dataNascimento", pessoa.getPessoa_dataNascimento());
        valores.put("pessoa_numSUS", pessoa.getPessoa_numSUS());
        valores.put("pessoa_foto", pessoa.getPessoa_foto());

        // Especificar a cláusula WHERE para atualizar o registro correto
        String whereClause = "pessoa_ID = ?";
        String[] whereArgs = {String.valueOf(pessoa.getPessoa_ID())};

        // Realizar a atualização no banco de dados
        int linhasAfetadas = db.update("pessoa", valores, whereClause, whereArgs);

        db.close();

        // Verificar se a atualização foi bem-sucedida
        return linhasAfetadas > 0;
    }
    @SuppressLint("Range")
    public void atualizarPessoaDoenca(int idUsuario, List<String> nomesDoencasSelecionadas) {
        SQLiteDatabase db = getWritableDatabase();

        // Remover todas as associações de doenças para o usuário específico
        String deleteQuery = "DELETE FROM pessoa_doenca WHERE pessoaDoenca_pessoaID = ?";
        db.execSQL(deleteQuery, new String[]{String.valueOf(idUsuario)});

        // Inserir novamente as associações com base nas doenças selecionadas
        for (String nomeDoenca : nomesDoencasSelecionadas) {
            // Primeiro, precisamos obter o ID da doença com base no nome
            String selectQuery = "SELECT doenca_ID FROM doenca WHERE doenca_nome = ?";
            Cursor cursor = db.rawQuery(selectQuery, new String[]{nomeDoenca});

            if (cursor.moveToFirst()) {
                int idDoenca = cursor.getInt(cursor.getColumnIndex("doenca_ID"));

                // Agora, podemos inserir a associação na tabela Pessoa-Doença
                ContentValues valores = new ContentValues();
                valores.put("pessoaDoenca_pessoaID", idUsuario);
                valores.put("pessoaDoenca_doencaID", idDoenca);
                db.insert("pessoa_doenca", null, valores);
            }

            cursor.close();
        }

        db.close();
    }
    public boolean excluirPessoa(int idPessoa) {
        SQLiteDatabase db = getWritableDatabase();

        // Especificar a cláusula WHERE para excluir o registro correto
        String whereClause = "pessoa_ID = ?";
        String[] whereArgs = {String.valueOf(idPessoa)};

        // Realizar a exclusão no banco de dados
        int linhasAfetadas = db.delete("pessoa", whereClause, whereArgs);

        db.close();

        // Verificar se a exclusão foi bem-sucedida
        return linhasAfetadas > 0;
    }
    @SuppressLint("Range")
    public boolean atualizarFotoPessoa(Pessoa pessoa) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("pessoa_foto", pessoa.getPessoa_foto());

        // Especificar a cláusula WHERE para atualizar o registro correto
        String whereClause = "pessoa_ID = ?";
        String[] whereArgs = {String.valueOf(pessoa.getPessoa_ID())};

        // Realizar a atualização no banco de dados
        int linhasAfetadas = db.update("pessoa", valores, whereClause, whereArgs);

        db.close();

        // Verificar se a atualização foi bem-sucedida
        return linhasAfetadas > 0;
    }

    // Adicione este método à sua classe DAO para inserir uma nova medição
    public boolean inserirMedicao(String data, double valor, int pessoaID, int doencaID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("medicao_data", data);
        valores.put("medicao_valor", valor);
        valores.put("medicao_pessoaID", pessoaID);
        valores.put("medicao_doencaID", doencaID);

        // Insere a nova medição no banco de dados
        long result = db.insert("medicao", null, valores);
        db.close();

        // Retorna true se a inserção foi bem-sucedida
        return result != -1;
    }
    @SuppressLint("Range")
    public List<Medicao> obterMedicoesPorDoencaEUsuario(int idDoenca, int idUsuario) {
        List<Medicao> medicoes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        // Consulta SQL para selecionar os atributos VALOR e DATA da tabela MEDIÇÃO com base no ID do usuário e da doença
        String query = "SELECT medicao_valor, medicao_data FROM medicao WHERE medicao_pessoaID = ? AND medicao_doencaID = ?";
        String[] selectionArgs = {String.valueOf(idUsuario), String.valueOf(idDoenca)};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        // Iterar sobre o cursor e adicionar os resultados à lista de medições
        if (cursor != null) {
            while (cursor.moveToNext()) {
                double valor = cursor.getDouble(cursor.getColumnIndex("medicao_valor"));
                String data = cursor.getString(cursor.getColumnIndex("medicao_data"));
                Medicao medicao = new Medicao();
                medicao.setMedicao_valor(valor);
                Date dataMedicao = null;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
                    dataMedicao = sdf.parse(data);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                medicao.setMedicao_data(dataMedicao);
                medicoes.add(medicao);
            }
            cursor.close();
        }
        db.close();
        return medicoes;
    }
    @SuppressLint("Range")
    public List<Medicao> obterUltimasMedicoes(int idDoenca, int idUsuario, int limite) {
        List<Medicao> medicoes = new ArrayList<>();

        // Consulta para obter as últimas "limite" entradas de medição para o usuário e a doença especificados
        String query = "SELECT * FROM medicao WHERE id_doenca = ? AND id_usuario = ? ORDER BY medicao_data DESC LIMIT ?";
        String[] selectionArgs = {String.valueOf(idDoenca), String.valueOf(idUsuario), String.valueOf(limite)};
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            cursor = db.rawQuery(query, selectionArgs);

            // Iterar sobre o cursor e adicionar as medições à lista
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idMedicao = cursor.getInt(cursor.getColumnIndex("id_medicao"));
                    double valorMedicao = cursor.getDouble(cursor.getColumnIndex("valor_medicao"));
                    String dataMedicao = cursor.getString(cursor.getColumnIndex("data_medicao"));

                    // Criar um objeto Medicao e adicioná-lo à lista
                    Medicao medicao = new Medicao();
                    medicoes.add(medicao);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao obter as últimas medições: " + e.getMessage());
        } finally {
            // Fechar o cursor e o banco de dados
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return medicoes;
    }

}
