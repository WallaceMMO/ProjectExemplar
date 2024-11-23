package com.example.projectheranca.persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "biblioteca.db";
    private static final int VERSAO_BANCO = 1;

    public DatabaseHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlAluno = "CREATE TABLE aluno (" +
                "ra INTEGER PRIMARY KEY," +
                "nome TEXT NOT NULL," +
                "email TEXT NOT NULL);";

        String sqlLivro = "CREATE TABLE livro (" +
                "codigo INTEGER PRIMARY KEY," +
                "nome TEXT NOT NULL," +
                "qtd_paginas INTEGER NOT NULL," +
                "isbn TEXT NOT NULL," +
                "edicao INTEGER NOT NULL);";

        String sqlRevista = "CREATE TABLE revista (" +
                "codigo INTEGER PRIMARY KEY," +
                "nome TEXT NOT NULL," +
                "qtd_paginas INTEGER NOT NULL," +
                "issn TEXT NOT NULL);";

        String sqlAluguel = "CREATE TABLE aluguel (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ra_aluno INTEGER NOT NULL," +
                "codigo_exemplar INTEGER NOT NULL," +
                "tipo_exemplar TEXT NOT NULL," +
                "data_retirada TEXT NOT NULL," +
                "data_devolucao TEXT NOT NULL," +
                "FOREIGN KEY(ra_aluno) REFERENCES aluno(ra));";

        db.execSQL(sqlAluno);
        db.execSQL(sqlLivro);
        db.execSQL(sqlRevista);
        db.execSQL(sqlAluguel);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}