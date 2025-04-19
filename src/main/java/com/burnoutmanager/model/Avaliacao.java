package com.burnoutmanager.model;

import java.time.LocalDate;

/**
 * Classe que representa uma avaliação de burnout para um funcionário
 */
public class Avaliacao {
    
    private Long id;
    private Funcionario funcionario;
    private LocalDate data;
    private double nivelExaustao;
    private double nivelDespersonalizacao;
    private double nivelRealizacaoPessoal;
    private double nivelCargaTrabalho;
    private double nivelApoioSocial;
    private double indiceBurnout;
    private String observacoes;
    private String recomendacoes;
    
    public Avaliacao() {
        this.data = LocalDate.now();
    }
    
    public Avaliacao(Funcionario funcionario) {
        this();
        this.funcionario = funcionario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public double getNivelExaustao() {
        return nivelExaustao;
    }

    public void setNivelExaustao(double nivelExaustao) {
        this.nivelExaustao = nivelExaustao;
    }

    public double getNivelDespersonalizacao() {
        return nivelDespersonalizacao;
    }

    public void setNivelDespersonalizacao(double nivelDespersonalizacao) {
        this.nivelDespersonalizacao = nivelDespersonalizacao;
    }

    public double getNivelRealizacaoPessoal() {
        return nivelRealizacaoPessoal;
    }

    public void setNivelRealizacaoPessoal(double nivelRealizacaoPessoal) {
        this.nivelRealizacaoPessoal = nivelRealizacaoPessoal;
    }

    public double getNivelCargaTrabalho() {
        return nivelCargaTrabalho;
    }

    public void setNivelCargaTrabalho(double nivelCargaTrabalho) {
        this.nivelCargaTrabalho = nivelCargaTrabalho;
    }

    public double getNivelApoioSocial() {
        return nivelApoioSocial;
    }

    public void setNivelApoioSocial(double nivelApoioSocial) {
        this.nivelApoioSocial = nivelApoioSocial;
    }

    public double getIndiceBurnout() {
        return indiceBurnout;
    }

    public void setIndiceBurnout(double indiceBurnout) {
        this.indiceBurnout = indiceBurnout;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getRecomendacoes() {
        return recomendacoes;
    }

    public void setRecomendacoes(String recomendacoes) {
        this.recomendacoes = recomendacoes;
    }
    
    /**
     * Calcula o índice de burnout com base nas três dimensões
     * Fórmula: (Exaustão + Despersonalização + (10 - Realização Pessoal)) / 3
     * Considerando que a escala de realização pessoal é invertida
     */
    public void calcularIndiceBurnout() {
        // A realização pessoal tem escala invertida (quanto maior, melhor)
        // então invertemos para o cálculo
        double realizacaoInvertida = 10 - nivelRealizacaoPessoal;
        
        // Média dos três fatores principais do MBI (Maslach Burnout Inventory)
        this.indiceBurnout = (nivelExaustao + nivelDespersonalizacao + realizacaoInvertida) / 3;
    }
    
    /**
     * Retorna a classificação do nível de burnout baseado no índice calculado
     */
    public String getClassificacaoBurnout() {
        if (indiceBurnout < 3) {
            return "Baixo";
        } else if (indiceBurnout < 6) {
            return "Moderado";
        } else {
            return "Alto";
        }
    }
    
    @Override
    public String toString() {
        return "Avaliação #" + id + " - " + funcionario.getNome() + " (" + data + ")";
    }
} 