package com.example.projectheranca.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.projectheranca.model.Aluno;
import com.example.projectheranca.persistance.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlunoDao implements ICRUDDao<Aluno> {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public AlunoDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    public void insert(Aluno aluno) throws SQLException {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ra", aluno.getRA());
        values.put("nome", aluno.getNome());
        values.put("email", aluno.getEmail());
        db.insert("aluno", null, values);
        db.close();
    }

    @Override
    public int update(Aluno aluno) throws SQLException {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("email", aluno.getEmail());
        int rows = db.update("aluno", values, "ra = ?", new String[]{String.valueOf(aluno.getRA())});
        db.close();
        return rows;
    }

    @Override
    public void delete(Aluno aluno) throws SQLException {
        db = dbHelper.getWritableDatabase();
        db.delete("aluno", "ra = ?", new String[]{String.valueOf(aluno.getRA())});
        db.close();
    }

    @Override
    public Aluno findOne(Aluno aluno) throws SQLException {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("aluno", null, "ra = ?", new String[]{String.valueOf(aluno.getRA())}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Aluno encontrado = new Aluno();
            encontrado.setRA(cursor.getInt(cursor.getColumnIndexOrThrow("ra")));
            encontrado.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            encontrado.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
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
    public List<Aluno> findAll() throws SQLException {
        List<Aluno> alunos = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("aluno", null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Aluno aluno = new Aluno();
                aluno.setRA(cursor.getInt(cursor.getColumnIndexOrThrow("ra")));
                aluno.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                aluno.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                alunos.add(aluno);
            }
            cursor.close();
        }
        db.close();
        return alunos;
    }
}