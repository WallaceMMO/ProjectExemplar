package com.example.projectheranca;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.projectheranca.controller.AlunoDao;
import com.example.projectheranca.controller.LivroDao;
import com.example.projectheranca.model.Aluno;
import com.example.projectheranca.model.Livro;

import java.sql.SQLException;
import java.util.List;

public class LivroFragment extends Fragment {

    private EditText txtCodigo;
    private EditText txtNome;
    private EditText txtQtdPaginas;
    private EditText txtISBN;
    private EditText txtEdicao;
    private Button btnInserir;
    private Button btnAtualizar;
    private Button btnDeletar;
    private Button btnBuscarUm;
    private Button btnListarTodos;
    private TextView txtResultado;

    private LivroDao livroDao;

    public LivroFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_livro, container, false);

        txtCodigo = view.findViewById(R.id.txtCodigo);
        txtNome = view.findViewById(R.id.txtNome);
        txtQtdPaginas = view.findViewById(R.id.txtQtdPaginas);
        txtISBN = view.findViewById(R.id.txtISBN);
        txtEdicao = view.findViewById(R.id.txtEdicao);
        btnInserir = view.findViewById(R.id.btnInserir);
        btnAtualizar = view.findViewById(R.id.btnAtualizar);
        btnDeletar = view.findViewById(R.id.btnDeletar);
        btnBuscarUm = view.findViewById(R.id.btnBuscarUm);
        btnListarTodos = view.findViewById(R.id.btnListarTodos);
        txtResultado = view.findViewById(R.id.txtResultado);

        livroDao = new LivroDao(getContext());

        btnInserir.setOnClickListener(v -> {
            Livro livro = new Livro();
            String codigoString = txtCodigo.getText().toString();
            String nome = txtNome.getText().toString();
            String qtdPaginasString = txtQtdPaginas.getText().toString();
            String isbn = txtISBN.getText().toString();
            String edicaoString = txtEdicao.getText().toString();

            if (codigoString.isEmpty() || nome.isEmpty() || qtdPaginasString.isEmpty() || isbn.isEmpty() || edicaoString.isEmpty()) {
                txtResultado.setText("Por favor, preencha todos os campos.");
                return;
            }

            int codigo = Integer.parseInt(codigoString);
            int qtdPaginas = Integer.parseInt(qtdPaginasString);
            int edicao = Integer.parseInt(edicaoString);

            livro.setCodigo(codigo);
            livro.setNome(nome);
            livro.setQtdPaginas(qtdPaginas);
            livro.setISBN(isbn);
            livro.setEdicao(edicao);

            try {
                livroDao.insert(livro);
                txtResultado.setText("Livro inserido com sucesso.");
                limparCampos();
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao inserir livro.");
            }
        });

        btnAtualizar.setOnClickListener(v -> {
            Livro livro = new Livro();
            String codigoString = txtCodigo.getText().toString();
            String nome = txtNome.getText().toString();
            String qtdPaginasString = txtQtdPaginas.getText().toString();
            String isbn = txtISBN.getText().toString();
            String edicaoString = txtEdicao.getText().toString();

            if (codigoString.isEmpty() || nome.isEmpty() || qtdPaginasString.isEmpty() || isbn.isEmpty() || edicaoString.isEmpty()) {
                txtResultado.setText("Por favor, preencha todos os campos.");
                return;
            }

            int codigo = Integer.parseInt(codigoString);
            int qtdPaginas = Integer.parseInt(qtdPaginasString);
            int edicao = Integer.parseInt(edicaoString);

            livro.setCodigo(codigo);
            livro.setNome(nome);
            livro.setQtdPaginas(qtdPaginas);
            livro.setISBN(isbn);
            livro.setEdicao(edicao);

            try {
                int result = livroDao.update(livro);
                if (result > 0) {
                    txtResultado.setText("Livro atualizado com sucesso.");
                } else {
                    txtResultado.setText("Livro não encontrado para atualização.");
                }
                limparCampos();
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao atualizar livro.");
            }
        });

        btnDeletar.setOnClickListener(v -> {
            String codigoString = txtCodigo.getText().toString();

            if (codigoString.isEmpty()) {
                txtResultado.setText("Por favor, insira o código do livro a ser deletado.");
                return;
            }

            int codigo = Integer.parseInt(codigoString);
            Livro livro = new Livro();
            livro.setCodigo(codigo);

            try {
                livroDao.delete(livro);
                txtResultado.setText("Livro deletado com sucesso.");
                limparCampos();
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao deletar livro.");
            }
        });

        btnBuscarUm.setOnClickListener(v -> {
            String codigoString = txtCodigo.getText().toString();

            if (codigoString.isEmpty()) {
                txtResultado.setText("Por favor, insira o código do livro a ser buscado.");
                return;
            }

            int codigo = Integer.parseInt(codigoString);
            Livro livro = new Livro();
            livro.setCodigo(codigo);

            try {
                Livro encontrado = livroDao.findOne(livro);
                if (encontrado != null) {
                    txtResultado.setText(encontrado.toString());
                } else {
                    txtResultado.setText("Livro não encontrado.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao buscar livro.");
            }
        });

        btnListarTodos.setOnClickListener(v -> {
            try {
                List<Livro> livros = livroDao.findAll();
                if (livros.isEmpty()) {
                    txtResultado.setText("Nenhum livro cadastrado.");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (Livro livro : livros) {
                        sb.append(livro.toString()).append("\n\n");
                    }
                    txtResultado.setText(sb.toString());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao listar livros.");
            }
        });

        return view;
    }

    private void limparCampos() {
        txtCodigo.setText("");
        txtNome.setText("");
        txtQtdPaginas.setText("");
        txtISBN.setText("");
        txtEdicao.setText("");
    }
}