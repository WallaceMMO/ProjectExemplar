package com.example.projectheranca.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.projectheranca.model.Aluguel;
import com.example.projectheranca.model.Aluno;
import com.example.projectheranca.model.Exemplar;
import com.example.projectheranca.model.Livro;
import com.example.projectheranca.model.Revista;
import com.example.projectheranca.persistance.DatabaseHelper;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AluguelDao implements ICRUDDao<Aluguel> {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private Context context;

    public AluguelDao(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    public void insert(Aluguel aluguel) throws SQLException {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ra_aluno", aluguel.getAluno().getRA());
        values.put("codigo_exemplar", aluguel.getExemplar().getCodigo());
        values.put("tipo_exemplar", aluguel.getExemplar() instanceof Livro ? "Livro" : "Revista");
        values.put("data_retirada", aluguel.getDataRetirada().toString());
        values.put("data_devolucao", aluguel.getDataDevolucao().toString());
        db.insert("aluguel", null, values);
        db.close();
    }

    @Override
    public int update(Aluguel aluguel) throws SQLException {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("data_retirada", aluguel.getDataRetirada().toString());
        values.put("data_devolucao", aluguel.getDataDevolucao().toString());
        int rows = db.update("aluguel", values, "ra_aluno = ? AND codigo_exemplar = ? AND tipo_exemplar = ?",
                new String[]{
                        String.valueOf(aluguel.getAluno().getRA()),
                        String.valueOf(aluguel.getExemplar().getCodigo()),
                        aluguel.getExemplar() instanceof Livro ? "Livro" : "Revista"
                });
        db.close();
        return rows;
    }

    @Override
    public void delete(Aluguel aluguel) throws SQLException {
        db = dbHelper.getWritableDatabase();
        db.delete("aluguel", "ra_aluno = ? AND codigo_exemplar = ? AND tipo_exemplar = ?",
                new String[]{
                        String.valueOf(aluguel.getAluno().getRA()),
                        String.valueOf(aluguel.getExemplar().getCodigo()),
                        aluguel.getExemplar() instanceof Livro ? "Livro" : "Revista"
                });
        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Aluguel findOne(Aluguel aluguel) throws SQLException {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("aluguel", null,
                "ra_aluno = ? AND codigo_exemplar = ? AND tipo_exemplar = ?",
                new String[]{
                        String.valueOf(aluguel.getAluno().getRA()),
                        String.valueOf(aluguel.getExemplar().getCodigo()),
                        aluguel.getExemplar() instanceof Livro ? "Livro" : "Revista"
                }, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Aluguel encontrado = new Aluguel();
            AlunoDao alunoDao = new AlunoDao(context);
            Aluno aluno = new Aluno();
            aluno.setRA(cursor.getInt(cursor.getColumnIndexOrThrow("ra_aluno")));
            aluno = alunoDao.findOne(aluno);

            String tipoExemplar = cursor.getString(cursor.getColumnIndexOrThrow("tipo_exemplar"));
            Exemplar exemplar = null;
            if ("Livro".equals(tipoExemplar)) {
                LivroDao livroDao = new LivroDao(context);
                Livro livro = new Livro();
                livro.setCodigo(cursor.getInt(cursor.getColumnIndexOrThrow("codigo_exemplar")));
                exemplar = livroDao.findOne(livro);
            } else if ("Revista".equals(tipoExemplar)) {
                RevistaDao revistaDao = new RevistaDao(context);
                Revista revista = new Revista();
                revista.setCodigo(cursor.getInt(cursor.getColumnIndexOrThrow("codigo_exemplar")));
                exemplar = revistaDao.findOne(revista);
            }

            encontrado.setAluno(aluno);
            encontrado.setExemplar(exemplar);
            encontrado.setDataRetirada(LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("data_retirada"))));
            encontrado.setDataDevolucao(LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("data_devolucao"))));

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public List<Aluguel> findAll() throws SQLException {
        List<Aluguel> alugueis = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("aluguel", null, null, null, null, null, null);
        if (cursor != null) {
            AlunoDao alunoDao = new AlunoDao(context);
            LivroDao livroDao = new LivroDao(context);
            RevistaDao revistaDao = new RevistaDao(context);

            while (cursor.moveToNext()) {
                Aluguel aluguel = new Aluguel();
                Aluno aluno = new Aluno();
                aluno.setRA(cursor.getInt(cursor.getColumnIndexOrThrow("ra_aluno")));
                aluno = alunoDao.findOne(aluno);

                String tipoExemplar = cursor.getString(cursor.getColumnIndexOrThrow("tipo_exemplar"));
                Exemplar exemplar = null;
                if ("Livro".equals(tipoExemplar)) {
                    Livro livro = new Livro();
                    livro.setCodigo(cursor.getInt(cursor.getColumnIndexOrThrow("codigo_exemplar")));
                    exemplar = livroDao.findOne(livro);
                } else if ("Revista".equals(tipoExemplar)) {
                    Revista revista = new Revista();
                    revista.setCodigo(cursor.getInt(cursor.getColumnIndexOrThrow("codigo_exemplar")));
                    exemplar = revistaDao.findOne(revista);
                }

                aluguel.setAluno(aluno);
                aluguel.setExemplar(exemplar);
                aluguel.setDataRetirada(LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("data_retirada"))));
                aluguel.setDataDevolucao(LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("data_devolucao"))));

                alugueis.add(aluguel);
            }
            cursor.close();
        }
        db.close();
        return alugueis;
    }
}