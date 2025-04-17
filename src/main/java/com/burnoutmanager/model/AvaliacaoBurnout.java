package com.burnoutmanager.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "avaliacoes_burnout")
public class AvaliacaoBurnout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @Column(name = "data_avaliacao", nullable = false)
    private LocalDate dataAvaliacao;

    @Column(name = "nivel_exaustao")
    private int nivelExaustao;

    @Column(name = "nivel_cinismo")
    private int nivelCinismo;

    @Column(name = "nivel_ineficacia")
    private int nivelIneficacia;

    @Column(name = "horas_trabalhadas_semana")
    private Integer horasTrabalhadasSemana;

    @Column(name = "dias_afastamento")
    private Integer diasAfastamento;

    @Column(length = 1000)
    private String observacoes;

    @Column(name = "nivel_risco")
    @Enumerated(EnumType.STRING)
    private NivelRisco nivelRisco;

    @OneToMany(mappedBy = "avaliacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recomendacao> recomendacoes = new ArrayList<>();

    public enum NivelRisco {
        BAIXO,
        MODERADO,
        ALTO,
        CRITICO
    }

    public AvaliacaoBurnout() {
        this.dataAvaliacao = LocalDate.now();
    }

    public AvaliacaoBurnout(Funcionario funcionario, int nivelExaustao, int nivelCinismo, int nivelIneficacia) {
        this.funcionario = funcionario;
        this.dataAvaliacao = LocalDate.now();
        this.nivelExaustao = nivelExaustao;
        this.nivelCinismo = nivelCinismo;
        this.nivelIneficacia = nivelIneficacia;
        calcularNivelRisco();
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

    public LocalDate getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(LocalDate dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }

    public int getNivelExaustao() {
        return nivelExaustao;
    }

    public void setNivelExaustao(int nivelExaustao) {
        this.nivelExaustao = nivelExaustao;
        calcularNivelRisco();
    }

    public int getNivelCinismo() {
        return nivelCinismo;
    }

    public void setNivelCinismo(int nivelCinismo) {
        this.nivelCinismo = nivelCinismo;
        calcularNivelRisco();
    }

    public int getNivelIneficacia() {
        return nivelIneficacia;
    }

    public void setNivelIneficacia(int nivelIneficacia) {
        this.nivelIneficacia = nivelIneficacia;
        calcularNivelRisco();
    }

    public Integer getHorasTrabalhadasSemana() {
        return horasTrabalhadasSemana;
    }

    public void setHorasTrabalhadasSemana(Integer horasTrabalhadasSemana) {
        this.horasTrabalhadasSemana = horasTrabalhadasSemana;
    }

    public Integer getDiasAfastamento() {
        return diasAfastamento;
    }

    public void setDiasAfastamento(Integer diasAfastamento) {
        this.diasAfastamento = diasAfastamento;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public NivelRisco getNivelRisco() {
        return nivelRisco;
    }

    public void setNivelRisco(NivelRisco nivelRisco) {
        this.nivelRisco = nivelRisco;
    }

    public List<Recomendacao> getRecomendacoes() {
        return recomendacoes;
    }

    public void setRecomendacoes(List<Recomendacao> recomendacoes) {
        this.recomendacoes = recomendacoes;
    }

    public void adicionarRecomendacao(Recomendacao recomendacao) {
        recomendacao.setAvaliacao(this);
        this.recomendacoes.add(recomendacao);
    }

    private void calcularNivelRisco() {
        double media = (nivelExaustao * 0.4) + (nivelCinismo * 0.3) + (nivelIneficacia * 0.3);
        
        if (media <= 3) {
            this.nivelRisco = NivelRisco.BAIXO;
        } else if (media <= 5) {
            this.nivelRisco = NivelRisco.MODERADO;
        } else if (media <= 7) {
            this.nivelRisco = NivelRisco.ALTO;
        } else {
            this.nivelRisco = NivelRisco.CRITICO;
        }
    }

    public double getPontuacaoMedia() {
        return (nivelExaustao + nivelCinismo + nivelIneficacia) / 3.0;
    }

    @Override
    public String toString() {
        return "AvaliacaoBurnout{" +
                "id=" + id +
                ", funcionario=" + (funcionario != null ? funcionario.getNome() : "null") +
                ", dataAvaliacao=" + dataAvaliacao +
                ", nivelRisco=" + nivelRisco +
                '}';
    }
}