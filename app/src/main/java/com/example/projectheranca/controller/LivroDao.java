package com.example.projectheranca.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.projectheranca.model.Livro;
import com.example.projectheranca.persistance.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivroDao implements ICRUDDao<Livro> {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public LivroDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    public void insert(Livro livro) throws SQLException {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("codigo", livro.getCodigo());
        values.put("nome", livro.getNome());
        values.put("qtd_paginas", livro.getQtdPaginas());
        values.put("isbn", livro.getISBN());
        values.put("edicao", livro.getEdicao());
        db.insert("livro", null, values);
        db.close();
    }

    @Override
    public int update(Livro livro) throws SQLException {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", livro.getNome());
        values.put("qtd_paginas", livro.getQtdPaginas());
        values.put("isbn", livro.getISBN());
        values.put("edicao", livro.getEdicao());
        int rows = db.update("livro", values, "codigo = ?", new String[]{String.valueOf(livro.getCodigo())});
        db.close();
        return rows;
    }

    @Override
    public void delete(Livro livro) throws SQLException {
        db = dbHelper.getWritableDatabase();
        db.delete("livro", "codigo = ?", new String[]{String.valueOf(livro.getCodigo())});
        db.close();
    }

    @Override
    public Livro findOne(Livro livro) throws SQLException {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("livro", null, "codigo = ?", new String[]{String.valueOf(livro.getCodigo())}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Livro encontrado = new Livro();
            encontrado.setCodigo(cursor.getInt(cursor.getColumnIndexOrThrow("codigo")));
            encontrado.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            encontrado.setQtdPaginas(cursor.getInt(cursor.getColumnIndexOrThrow("qtd_paginas")));
            encontrado.setISBN(cursor.getString(cursor.getColumnIndexOrThrow("isbn")));
            encontrado.setEdicao(cursor.getInt(cursor.getColumnIndexOrThrow("edicao")));
            cursor.close();
            db.close();
            return encontrado;
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }

    @Override
    public List<Livro> findAll() throws SQLException {
        List<Livro> livros = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("livro", null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Livro livro = new Livro();
                livro.setCodigo(cursor.getInt(cursor.getColumnIndexOrThrow("codigo")));
                livro.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                livro.setQtdPaginas(cursor.getInt(cursor.getColumnIndexOrThrow("qtd_paginas")));
                livro.setISBN(cursor.getString(cursor.getColumnIndexOrThrow("isbn")));
                livro.setEdicao(cursor.getInt(cursor.getColumnIndexOrThrow("edicao")));
                livros.add(livro);
            }
            cursor.close();
        }
        db.close();
        return livros;
    }
}