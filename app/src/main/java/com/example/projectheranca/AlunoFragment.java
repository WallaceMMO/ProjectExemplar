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
import com.example.projectheranca.model.Aluno;

import java.sql.SQLException;
import java.util.List;

public class AlunoFragment extends Fragment {

    private EditText txtRA;
    private EditText txtNome;
    private EditText txtEmail;
    private Button btnInserir;
    private Button btnAtualizar;
    private Button btnDeletar;
    private Button btnBuscarUm;
    private Button btnListarTodos;
    private TextView txtResultado;

    private AlunoDao alunoDao;

    public AlunoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_aluno, container, false);

        txtRA = view.findViewById(R.id.txtRA);
        txtNome = view.findViewById(R.id.txtNome);
        txtEmail = view.findViewById(R.id.txtEmail);
        btnInserir = view.findViewById(R.id.btnInserir);
        btnAtualizar = view.findViewById(R.id.btnAtualizar);
        btnDeletar = view.findViewById(R.id.btnDeletar);
        btnBuscarUm = view.findViewById(R.id.btnBuscarUm);
        btnListarTodos = view.findViewById(R.id.btnListarTodos);
        txtResultado = view.findViewById(R.id.txtResultado);

        alunoDao = new AlunoDao(getContext());

        btnInserir.setOnClickListener(v -> {
            Aluno aluno = new Aluno();
            String raString = txtRA.getText().toString();
            String nome = txtNome.getText().toString();
            String email = txtEmail.getText().toString();

            if (raString.isEmpty() || nome.isEmpty() || email.isEmpty()) {
                txtResultado.setText("Por favor, preencha todos os campos.");
                return;
            }

            int ra = Integer.parseInt(raString);
            aluno.setRA(ra);
            aluno.setNome(nome);
            aluno.setEmail(email);

            try {
                alunoDao.insert(aluno);
                txtResultado.setText("Aluno inserido com sucesso.");
                limparCampos();
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao inserir aluno.");
            }
        });

        btnAtualizar.setOnClickListener(v -> {
            Aluno aluno = new Aluno();
            String raString = txtRA.getText().toString();
            String nome = txtNome.getText().toString();
            String email = txtEmail.getText().toString();

            if (raString.isEmpty() || nome.isEmpty() || email.isEmpty()) {
                txtResultado.setText("Por favor, preencha todos os campos.");
                return;
            }

            int ra = Integer.parseInt(raString);
            aluno.setRA(ra);
            aluno.setNome(nome);
            aluno.setEmail(email);

            try {
                int result = alunoDao.update(aluno);
                if (result > 0) {
                    txtResultado.setText("Aluno atualizado com sucesso.");
                } else {
                    txtResultado.setText("Aluno não encontrado para atualização.");
                }
                limparCampos();
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao atualizar aluno.");
            }
        });

        btnDeletar.setOnClickListener(v -> {
            String raString = txtRA.getText().toString();

            if (raString.isEmpty()) {
                txtResultado.setText("Por favor, insira o RA do aluno a ser deletado.");
                return;
            }

            int ra = Integer.parseInt(raString);
            Aluno aluno = new Aluno();
            aluno.setRA(ra);

            try {
                alunoDao.delete(aluno);
                txtResultado.setText("Aluno deletado com sucesso.");
                limparCampos();
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao deletar aluno.");
            }
        });

        btnBuscarUm.setOnClickListener(v -> {
            String raString = txtRA.getText().toString();

            if (raString.isEmpty()) {
                txtResultado.setText("Por favor, insira o RA do aluno a ser buscado.");
                return;
            }

            int ra = Integer.parseInt(raString);
            Aluno aluno = new Aluno();
            aluno.setRA(ra);

            try {
                Aluno encontrado = alunoDao.findOne(aluno);
                if (encontrado != null) {
                    txtResultado.setText(encontrado.toString());
                } else {
                    txtResultado.setText("Aluno não encontrado.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao buscar aluno.");
            }
        });

        btnListarTodos.setOnClickListener(v -> {
            try {
                List<Aluno> alunos = alunoDao.findAll();
                if (alunos.isEmpty()) {
                    txtResultado.setText("Nenhum aluno cadastrado.");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (Aluno aluno : alunos) {
                        sb.append(aluno.toString()).append("\n\n");
                    }
                    txtResultado.setText(sb.toString());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao listar alunos.");
            }
        });

        return view;
    }

    private void limparCampos() {
        txtRA.setText("");
        txtNome.setText("");
        txtEmail.setText("");
    }
}