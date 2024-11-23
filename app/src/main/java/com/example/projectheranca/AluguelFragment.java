package com.example.projectheranca;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.projectheranca.controller.AluguelDao;
import com.example.projectheranca.controller.AlunoDao;
import com.example.projectheranca.controller.LivroDao;
import com.example.projectheranca.controller.RevistaDao;
import com.example.projectheranca.model.Aluguel;
import com.example.projectheranca.model.Aluno;
import com.example.projectheranca.model.Exemplar;
import com.example.projectheranca.model.Livro;
import com.example.projectheranca.model.Revista;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public class AluguelFragment extends Fragment {

    private EditText txtRA;
    private EditText txtCodigoExemplar;
    private RadioGroup rgTipoExemplar;
    private RadioButton rbLivro;
    private RadioButton rbRevista;
    private EditText txtDataRetirada;
    private EditText txtDataDevolucao;
    private Button btnInserir;
    private Button btnAtualizar;
    private Button btnDeletar;
    private Button btnBuscarUm;
    private Button btnListarTodos;
    private TextView txtResultado;

    private AluguelDao aluguelDao;
    private AlunoDao alunoDao;
    private LivroDao livroDao;
    private RevistaDao revistaDao;

    public AluguelFragment() {}

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_aluguel, container, false);

        txtRA = view.findViewById(R.id.txtRA);
        txtCodigoExemplar = view.findViewById(R.id.txtCodigoExemplar);
        rgTipoExemplar = view.findViewById(R.id.rgTipoExemplar);
        rbLivro = view.findViewById(R.id.rbLivro);
        rbRevista = view.findViewById(R.id.rbRevista);
        txtDataRetirada = view.findViewById(R.id.txtDataRetirada);
        txtDataDevolucao = view.findViewById(R.id.txtDataDevolucao);
        btnInserir = view.findViewById(R.id.btnInserir);
        btnAtualizar = view.findViewById(R.id.btnAtualizar);
        btnDeletar = view.findViewById(R.id.btnDeletar);
        btnBuscarUm = view.findViewById(R.id.btnBuscarUm);
        btnListarTodos = view.findViewById(R.id.btnListarTodos);
        txtResultado = view.findViewById(R.id.txtResultado);

        aluguelDao = new AluguelDao(getContext());
        alunoDao = new AlunoDao(getContext());
        livroDao = new LivroDao(getContext());
        revistaDao = new RevistaDao(getContext());

        txtDataRetirada.setOnClickListener(v -> showDatePickerDialog(txtDataRetirada));
        txtDataDevolucao.setOnClickListener(v -> showDatePickerDialog(txtDataDevolucao));

        btnInserir.setOnClickListener(v -> {
            String raString = txtRA.getText().toString();
            String codigoExemplarString = txtCodigoExemplar.getText().toString();
            String dataRetiradaString = txtDataRetirada.getText().toString();
            String dataDevolucaoString = txtDataDevolucao.getText().toString();

            if (raString.isEmpty() || codigoExemplarString.isEmpty() ||
                    dataRetiradaString.isEmpty() || dataDevolucaoString.isEmpty() ||
                    rgTipoExemplar.getCheckedRadioButtonId() == -1) {
                txtResultado.setText("Por favor, preencha todos os campos.");
                return;
            }

            int ra = Integer.parseInt(raString);
            int codigoExemplar = Integer.parseInt(codigoExemplarString);
            LocalDate dataRetirada = LocalDate.parse(dataRetiradaString);
            LocalDate dataDevolucao = LocalDate.parse(dataDevolucaoString);

            Aluno aluno = new Aluno();
            aluno.setRA(ra);

            try {
                Aluno alunoEncontrado = alunoDao.findOne(aluno);
                if (alunoEncontrado == null) {
                    txtResultado.setText("Aluno não encontrado.");
                    return;
                }

                Exemplar exemplar = null;
                int selectedExemplarId = rgTipoExemplar.getCheckedRadioButtonId();
                if (selectedExemplarId == R.id.rbLivro) {
                    Livro livro = new Livro();
                    livro.setCodigo(codigoExemplar);
                    Livro livroEncontrado = livroDao.findOne(livro);
                    if (livroEncontrado == null) {
                        txtResultado.setText("Livro não encontrado.");
                        return;
                    }
                    exemplar = livroEncontrado;
                } else if (selectedExemplarId == R.id.rbRevista) {
                    Revista revista = new Revista();
                    revista.setCodigo(codigoExemplar);
                    Revista revistaEncontrada = revistaDao.findOne(revista);
                    if (revistaEncontrada == null) {
                        txtResultado.setText("Revista não encontrada.");
                        return;
                    }
                    exemplar = revistaEncontrada;
                }

                Aluguel aluguel = new Aluguel();
                aluguel.setAluno(alunoEncontrado);
                aluguel.setExemplar(exemplar);
                aluguel.setDataRetirada(dataRetirada);
                aluguel.setDataDevolucao(dataDevolucao);

                aluguelDao.insert(aluguel);
                txtResultado.setText("Aluguel inserido com sucesso.");
                limparCampos();
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao inserir aluguel.");
            }
        });

        btnAtualizar.setOnClickListener(v -> {
            String raString = txtRA.getText().toString();
            String codigoExemplarString = txtCodigoExemplar.getText().toString();
            String dataRetiradaString = txtDataRetirada.getText().toString();
            String dataDevolucaoString = txtDataDevolucao.getText().toString();

            if (raString.isEmpty() || codigoExemplarString.isEmpty() ||
                    dataRetiradaString.isEmpty() || dataDevolucaoString.isEmpty() ||
                    rgTipoExemplar.getCheckedRadioButtonId() == -1) {
                txtResultado.setText("Por favor, preencha todos os campos.");
                return;
            }

            int ra = Integer.parseInt(raString);
            int codigoExemplar = Integer.parseInt(codigoExemplarString);
            LocalDate dataRetirada = LocalDate.parse(dataRetiradaString);
            LocalDate dataDevolucao = LocalDate.parse(dataDevolucaoString);

            Aluno aluno = new Aluno();
            aluno.setRA(ra);

            try {
                Aluno alunoEncontrado = alunoDao.findOne(aluno);
                if (alunoEncontrado == null) {
                    txtResultado.setText("Aluno não encontrado.");
                    return;
                }

                Exemplar exemplar = null;
                int selectedExemplarId = rgTipoExemplar.getCheckedRadioButtonId();
                if (selectedExemplarId == R.id.rbLivro) {
                    Livro livro = new Livro();
                    livro.setCodigo(codigoExemplar);
                    Livro livroEncontrado = livroDao.findOne(livro);
                    if (livroEncontrado == null) {
                        txtResultado.setText("Livro não encontrado.");
                        return;
                    }
                    exemplar = livroEncontrado;
                } else if (selectedExemplarId == R.id.rbRevista) {
                    Revista revista = new Revista();
                    revista.setCodigo(codigoExemplar);
                    Revista revistaEncontrada = revistaDao.findOne(revista);
                    if (revistaEncontrada == null) {
                        txtResultado.setText("Revista não encontrada.");
                        return;
                    }
                    exemplar = revistaEncontrada;
                }

                Aluguel aluguel = new Aluguel();
                aluguel.setAluno(alunoEncontrado);
                aluguel.setExemplar(exemplar);
                aluguel.setDataRetirada(dataRetirada);
                aluguel.setDataDevolucao(dataDevolucao);

                int result = aluguelDao.update(aluguel);
                if (result > 0) {
                    txtResultado.setText("Aluguel atualizado com sucesso.");
                } else {
                    txtResultado.setText("Aluguel não encontrado para atualização.");
                }
                limparCampos();
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao atualizar aluguel.");
            }
        });

        btnDeletar.setOnClickListener(v -> {
            String raString = txtRA.getText().toString();
            String codigoExemplarString = txtCodigoExemplar.getText().toString();

            if (raString.isEmpty() || codigoExemplarString.isEmpty() ||
                    rgTipoExemplar.getCheckedRadioButtonId() == -1) {
                txtResultado.setText("Por favor, insira o RA e o código do exemplar.");
                return;
            }

            int ra = Integer.parseInt(raString);
            int codigoExemplar = Integer.parseInt(codigoExemplarString);

            Aluno aluno = new Aluno();
            aluno.setRA(ra);

            try {
                Aluno alunoEncontrado = alunoDao.findOne(aluno);
                if (alunoEncontrado == null) {
                    txtResultado.setText("Aluno não encontrado.");
                    return;
                }

                Exemplar exemplar = null;
                int selectedExemplarId = rgTipoExemplar.getCheckedRadioButtonId();
                if (selectedExemplarId == R.id.rbLivro) {
                    Livro livro = new Livro();
                    livro.setCodigo(codigoExemplar);
                    Livro livroEncontrado = livroDao.findOne(livro);
                    if (livroEncontrado == null) {
                        txtResultado.setText("Livro não encontrado.");
                        return;
                    }
                    exemplar = livroEncontrado;
                } else if (selectedExemplarId == R.id.rbRevista) {
                    Revista revista = new Revista();
                    revista.setCodigo(codigoExemplar);
                    Revista revistaEncontrada = revistaDao.findOne(revista);
                    if (revistaEncontrada == null) {
                        txtResultado.setText("Revista não encontrada.");
                        return;
                    }
                    exemplar = revistaEncontrada;
                }

                Aluguel aluguel = new Aluguel();
                aluguel.setAluno(alunoEncontrado);
                aluguel.setExemplar(exemplar);

                aluguelDao.delete(aluguel);
                txtResultado.setText("Aluguel deletado com sucesso.");
                limparCampos();
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao deletar aluguel.");
            }
        });

        btnBuscarUm.setOnClickListener(v -> {
            String raString = txtRA.getText().toString();
            String codigoExemplarString = txtCodigoExemplar.getText().toString();

            if (raString.isEmpty() || codigoExemplarString.isEmpty() ||
                    rgTipoExemplar.getCheckedRadioButtonId() == -1) {
                txtResultado.setText("Por favor, insira o RA e o código do exemplar.");
                return;
            }

            int ra = Integer.parseInt(raString);
            int codigoExemplar = Integer.parseInt(codigoExemplarString);

            Aluno aluno = new Aluno();
            aluno.setRA(ra);

            try {
                Aluno alunoEncontrado = alunoDao.findOne(aluno);
                if (alunoEncontrado == null) {
                    txtResultado.setText("Aluno não encontrado.");
                    return;
                }

                Exemplar exemplar = null;
                int selectedExemplarId = rgTipoExemplar.getCheckedRadioButtonId();
                if (selectedExemplarId == R.id.rbLivro) {
                    Livro livro = new Livro();
                    livro.setCodigo(codigoExemplar);
                    Livro livroEncontrado = livroDao.findOne(livro);
                    if (livroEncontrado == null) {
                        txtResultado.setText("Livro não encontrado.");
                        return;
                    }
                    exemplar = livroEncontrado;
                } else if (selectedExemplarId == R.id.rbRevista) {
                    Revista revista = new Revista();
                    revista.setCodigo(codigoExemplar);
                    Revista revistaEncontrada = revistaDao.findOne(revista);
                    if (revistaEncontrada == null) {
                        txtResultado.setText("Revista não encontrada.");
                        return;
                    }
                    exemplar = revistaEncontrada;
                }

                Aluguel aluguel = new Aluguel();
                aluguel.setAluno(alunoEncontrado);
                aluguel.setExemplar(exemplar);

                Aluguel encontrado = aluguelDao.findOne(aluguel);
                if (encontrado != null) {
                    txtResultado.setText(encontrado.toString());
                } else {
                    txtResultado.setText("Aluguel não encontrado.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao buscar aluguel.");
            }
        });

        btnListarTodos.setOnClickListener(v -> {
            try {
                List<Aluguel> alugueis = aluguelDao.findAll();
                if (alugueis.isEmpty()) {
                    txtResultado.setText("Nenhum aluguel cadastrado.");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (Aluguel aluguel : alugueis) {
                        sb.append(aluguel.toString()).append("\n\n");
                    }
                    txtResultado.setText(sb.toString());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                txtResultado.setText("Erro ao listar alugueis.");
            }
        });

        return view;
    }

    private void limparCampos() {
        txtRA.setText("");
        txtCodigoExemplar.setText("");
        rgTipoExemplar.clearCheck();
        txtDataRetirada.setText("");
        txtDataDevolucao.setText("");
    }

    private void showDatePickerDialog(EditText editText) {
        Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    String data = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    editText.setText(data);
                }, ano, mes, dia);
        datePickerDialog.show();
    }
}