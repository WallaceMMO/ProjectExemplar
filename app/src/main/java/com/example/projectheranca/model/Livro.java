package com.example.projectheranca.model;

public class Livro extends Exemplar {
    private String ISBN;
    private int edicao;

    public Livro() {}

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getEdicao() {
        return edicao;
    }

    public void setEdicao(int edicao) {
        this.edicao = edicao;
    }

    @Override
    public String toString() {
        return "Livro{" +
                "codigo=" + getCodigo() +
                ", nome='" + getNome() + '\'' +
                ", qtdPaginas=" + getQtdPaginas() +
                ", ISBN='" + ISBN + '\'' +
                ", edicao=" + edicao +
                '}';
    }
}
