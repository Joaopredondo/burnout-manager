package com.burnoutmanager.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "recomendacoes")
public class Recomendacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "avaliacao_id", nullable = false)
    private AvaliacaoBurnout avaliacao;

    @Column(nullable = false)
    private String descricao;

    @Column(name = "tipo_intervencao")
    @Enumerated(EnumType.STRING)
    private TipoIntervencao tipoIntervencao;

    @Column(name = "data_recomendacao")
    private LocalDate dataRecomendacao;

    @Column(name = "data_prazo")
    private LocalDate dataPrazo;

    @Column
    private boolean implementada;

    @Column(name = "data_implementacao")
    private LocalDate dataImplementacao;

    @Column
    private String responsavel;

    @Column(length = 1000)
    private String observacoes;

    public enum TipoIntervencao {
        INDIVIDUAL("Individual"),
        ORGANIZACIONAL("Organizacional"),
        CLINICA("Clínica"),
        AFASTAMENTO("Afastamento");

        private final String descricao;

        TipoIntervencao(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public Recomendacao() {
        this.dataRecomendacao = LocalDate.now();
        this.implementada = false;
    }

    public Recomendacao(AvaliacaoBurnout avaliacao, String descricao, TipoIntervencao tipoIntervencao) {
        this.avaliacao = avaliacao;
        this.descricao = descricao;
        this.tipoIntervencao = tipoIntervencao;
        this.dataRecomendacao = LocalDate.now();
        this.implementada = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AvaliacaoBurnout getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(AvaliacaoBurnout avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoIntervencao getTipoIntervencao() {
        return tipoIntervencao;
    }

    public void setTipoIntervencao(TipoIntervencao tipoIntervencao) {
        this.tipoIntervencao = tipoIntervencao;
    }

    public LocalDate getDataRecomendacao() {
        return dataRecomendacao;
    }

    public void setDataRecomendacao(LocalDate dataRecomendacao) {
        this.dataRecomendacao = dataRecomendacao;
    }

    public LocalDate getDataPrazo() {
        return dataPrazo;
    }

    public void setDataPrazo(LocalDate dataPrazo) {
        this.dataPrazo = dataPrazo;
    }

    public boolean isImplementada() {
        return implementada;
    }

    public void setImplementada(boolean implementada) {
        this.implementada = implementada;
        if (implementada && this.dataImplementacao == null) {
            this.dataImplementacao = LocalDate.now();
        }
    }

    public LocalDate getDataImplementacao() {
        return dataImplementacao;
    }

    public void setDataImplementacao(LocalDate dataImplementacao) {
        this.dataImplementacao = dataImplementacao;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Override
    public String toString() {
        return "Recomendacao{" +
                "id=" + id +
                ", tipoIntervencao=" + tipoIntervencao +
                ", descricao='" + descricao + '\'' +
                ", implementada=" + implementada +
                '}';
    }
}