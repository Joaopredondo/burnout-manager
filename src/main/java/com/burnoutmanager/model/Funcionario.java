package com.burnoutmanager.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "funcionarios")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "data_contratacao")
    private LocalDate dataContratacao;

    @Column
    private String departamento;

    @Column
    private String cargo;

    @OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AvaliacaoBurnout> avaliacoes = new ArrayList<>();

    @Column(name = "data_admissao")
    private LocalDate dataAdmissao;

    @Column
    private String telefone;

    public Funcionario() {
    }

    public Funcionario(String nome, String email, LocalDate dataContratacao, String departamento, String cargo) {
        this.nome = nome;
        this.email = email;
        this.dataContratacao = dataContratacao;
        this.departamento = departamento;
        this.cargo = cargo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataContratacao() {
        return dataContratacao;
    }

    public void setDataContratacao(LocalDate dataContratacao) {
        this.dataContratacao = dataContratacao;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public List<AvaliacaoBurnout> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<AvaliacaoBurnout> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void adicionarAvaliacao(AvaliacaoBurnout avaliacao) {
        avaliacao.setFuncionario(this);
        this.avaliacoes.add(avaliacao);
    }

    public AvaliacaoBurnout getUltimaAvaliacao() {
        if (avaliacoes.isEmpty()) {
            return null;
        }
        return avaliacoes.stream()
                .sorted((a1, a2) -> a2.getDataAvaliacao().compareTo(a1.getDataAvaliacao()))
                .findFirst()
                .orElse(null);
    }

    public String getDescricaoCompleta() {
        return nome + " - " + cargo + " (" + departamento + ")";
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", departamento='" + departamento + '\'' +
                ", cargo='" + cargo + '\'' +
                '}';
    }
}