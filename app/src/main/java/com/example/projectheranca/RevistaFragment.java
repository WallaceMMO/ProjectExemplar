package com.example.projectheranca;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.projectheranca.controller.RevistaDao;
import com.example.projectheranca.model.Revista;

import java.sql.SQLException;
import java.util.List;

public class RevistaFragment extends Fragment {

    private EditText txtCodigo;
    private EditText txtNome;
    private EditText txtQtdPaginas;
    private EditText txtISSN;
    private Button btnInserir;
    private Button btnAtualizar;
    private Button btnDeletar;
    private Button btnBuscarUm;
    private Button btnListarTodos;
    private TextView txtResultado;

    private RevistaDao revistaDao;

    public RevistaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_revista, container, false);

        txtCodigo = view.findViewById(R.id.txtCodigo);
        txtNome = view.findViewById(R.id.txtNome);
        txtQtdPaginas = view.findViewById(R.id.txtQtdPaginas);
        txtISSN = view.findViewById(R.id.txtISSN);
        btnInserir = view.findViewById(R.id.btnInserir);
        btnAtualizar = view.findViewById(R.id.btnAtualizar);
        btnDeletar = view.findViewById(R.id.btnDeletar);
        btnBuscarUm = view.findViewById(R.id.btnBuscarUm);
        btnListarTodos = view.findViewById(R.id.btnListarTodos);
        txtResultado = view.findViewById(R.id.txtResultado);

        revistaDao = new RevistaDao(getContext());

        btnInserir.setOnClickListener(v -> {
            Revista revista = new Revista();
            String codigoString = txtCodigo.getText().toString();
            String nome = txtNome.getText().toString();
            String qtdPaginasString = txtQtdPaginas.getText().toString();
            String issn = txtISSN.getText().toString();

            if (codigoString.isEmpty() || nome.isEmpty() || qtdPaginasString.isEmpty() || issn.isEmpty()) {
                txtResultado.setText("Por favor, preencha todos os campos.");
                return;
            }

            int codigo = Integer.parseInt(codigoString);
            int qtdPaginas = Integer.parseInt(qtdPaginasString);

            revista.setCodigo(codigo);
            revista.setNome(nome);
            revista.setQtdPaginas(qtdPaginas);
            revista.setISSN(issn);

            try {
                revistaDao.insert(revista);
                txtResultado.setText("Revista inserida com sucesso.");
                limparCampos();
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao inserir revista.");
            }
        });

        btnAtualizar.setOnClickListener(v -> {
            Revista revista = new Revista();
            String codigoString = txtCodigo.getText().toString();
            String nome = txtNome.getText().toString();
            String qtdPaginasString = txtQtdPaginas.getText().toString();
            String issn = txtISSN.getText().toString();

            if (codigoString.isEmpty() || nome.isEmpty() || qtdPaginasString.isEmpty() || issn.isEmpty()) {
                txtResultado.setText("Por favor, preencha todos os campos.");
                return;
            }

            int codigo = Integer.parseInt(codigoString);
            int qtdPaginas = Integer.parseInt(qtdPaginasString);

            revista.setCodigo(codigo);
            revista.setNome(nome);
            revista.setQtdPaginas(qtdPaginas);
            revista.setISSN(issn);

            try {
                int result = revistaDao.update(revista);
                if (result > 0) {
                    txtResultado.setText("Revista atualizada com sucesso.");
                } else {
                    txtResultado.setText("Revista não encontrada para atualização.");
                }
                limparCampos();
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao atualizar revista.");
            }
        });

        btnDeletar.setOnClickListener(v -> {
            String codigoString = txtCodigo.getText().toString();

            if (codigoString.isEmpty()) {
                txtResultado.setText("Por favor, insira o código da revista a ser deletada.");
                return;
            }

            int codigo = Integer.parseInt(codigoString);
            Revista revista = new Revista();
            revista.setCodigo(codigo);

            try {
                revistaDao.delete(revista);
                txtResultado.setText("Revista deletada com sucesso.");
                limparCampos();
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao deletar revista.");
            }
        });

        btnBuscarUm.setOnClickListener(v -> {
            String codigoString = txtCodigo.getText().toString();

            if (codigoString.isEmpty()) {
                txtResultado.setText("Por favor, insira o código da revista a ser buscada.");
                return;
            }

            int codigo = Integer.parseInt(codigoString);
            Revista revista = new Revista();
            revista.setCodigo(codigo);

            try {
                Revista encontrada = revistaDao.findOne(revista);
                if (encontrada != null) {
                    txtResultado.setText(encontrada.toString());
                } else {
                    txtResultado.setText("Revista não encontrada.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao buscar revista.");
            }
        });

        btnListarTodos.setOnClickListener(v -> {
            try {
                List<Revista> revistas = revistaDao.findAll();
                if (revistas.isEmpty()) {
                    txtResultado.setText("Nenhuma revista cadastrada.");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (Revista revista : revistas) {
                        sb.append(revista.toString()).append("\n\n");
                    }
                    txtResultado.setText(sb.toString());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao listar revistas.");
            }
        });

        return view;
    }

    private void limparCampos() {
        txtCodigo.setText("");
        txtNome.setText("");
        txtQtdPaginas.setText("");
        txtISSN.setText("");
    }
}