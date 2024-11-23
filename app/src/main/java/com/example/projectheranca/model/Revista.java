package com.example.projectheranca.model;

public class Revista extends Exemplar {
    private String ISSN;

    public Revista() {}

    public String getISSN() {
        return ISSN;
    }

    public void setISSN(String ISSN) {
        this.ISSN = ISSN;
    }

    @Override
    public String toString() {
        return "Revista{" +
                "codigo=" + getCodigo() +
                ", nome='" + getNome() + '\'' +
                ", qtdPaginas=" + getQtdPaginas() +
                ", ISSN='" + ISSN + '\'' +
                '}';
    }
}
