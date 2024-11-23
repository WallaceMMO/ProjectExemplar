package com.example.projectheranca.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.projectheranca.model.Revista;
import com.example.projectheranca.persistance.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RevistaDao implements ICRUDDao<Revista> {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public RevistaDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    public void insert(Revista revista) throws SQLException {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("codigo", revista.getCodigo());
        values.put("nome", revista.getNome());
        values.put("qtd_paginas", revista.getQtdPaginas());
        values.put("issn", revista.getISSN());
        db.insert("revista", null, values);
        db.close();
    }

    @Override
    public int update(Revista revista) throws SQLException {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", revista.getNome());
        values.put("qtd_paginas", revista.getQtdPaginas());
        values.put("issn", revista.getISSN());
        int rows = db.update("revista", values, "codigo = ?", new String[]{String.valueOf(revista.getCodigo())});
        db.close();
        return rows;
    }

    @Override
    public void delete(Revista revista) throws SQLException {
        db = dbHelper.getWritableDatabase();
        db.delete("revista", "codigo = ?", new String[]{String.valueOf(revista.getCodigo())});
        db.close();
    }

    @Override
    public Revista findOne(Revista revista) throws SQLException {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("revista", null, "codigo = ?", new String[]{String.valueOf(revista.getCodigo())}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Revista encontrada = new Revista();
            encontrada.setCodigo(cursor.getInt(cursor.getColumnIndexOrThrow("codigo")));
            encontrada.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            encontrada.setQtdPaginas(cursor.getInt(cursor.getColumnIndexOrThrow("qtd_paginas")));
            encontrada.setISSN(cursor.getString(cursor.getColumnIndexOrThrow("issn")));
            cursor.close();
            db.close();
            return encontrada;
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }

    @Override
    public List<Revista> findAll() throws SQLException {
        List<Revista> revistas = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("revista", null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Revista revista = new Revista();
                revista.setCodigo(cursor.getInt(cursor.getColumnIndexOrThrow("codigo")));
                revista.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                revista.setQtdPaginas(cursor.getInt(cursor.getColumnIndexOrThrow("qtd_paginas")));
                revista.setISSN(cursor.getString(cursor.getColumnIndexOrThrow("issn")));
                revistas.add(revista);
            }
            cursor.close();
        }
        db.close();
        return revistas;
    }
}