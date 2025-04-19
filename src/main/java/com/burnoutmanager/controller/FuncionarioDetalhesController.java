package com.burnoutmanager.controller;

import com.burnoutmanager.model.AvaliacaoBurnout;
import com.burnoutmanager.model.Funcionario;
import com.burnoutmanager.model.Recomendacao;
import com.burnoutmanager.service.FuncionarioService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador para a tela de detalhes do funcionário
 */
public class FuncionarioDetalhesController {

    @FXML
    private Label lblNome;
    
    @FXML
    private Label lblEmail;
    
    @FXML
    private Label lblDepartamento;
    
    @FXML
    private Label lblCargo;
    
    @FXML
    private Label lblDataContratacao;
    
    @FXML
    private TableView<AvaliacaoBurnout> tabelaAvaliacoes;
    
    @FXML
    private TableColumn<AvaliacaoBurnout, String> colunaData;
    
    @FXML
    private TableColumn<AvaliacaoBurnout, Integer> colunaExaustao;
    
    @FXML
    private TableColumn<AvaliacaoBurnout, Integer> colunaCinismo;
    
    @FXML
    private TableColumn<AvaliacaoBurnout, Integer> colunaIneficacia;
    
    @FXML
    private TableColumn<AvaliacaoBurnout, String> colunaNivelRisco;
    
    @FXML
    private TableView<Recomendacao> tabelaRecomendacoes;
    
    @FXML
    private TableColumn<Recomendacao, String> colunaDescricao;
    
    @FXML
    private TableColumn<Recomendacao, String> colunaTipo;
    
    @FXML
    private TableColumn<Recomendacao, String> colunaImplementada;
    
    @FXML
    private LineChart<String, Number> graficoEvolucao;
    
    private Funcionario funcionario;
    private FuncionarioService funcionarioService;
    
    /**
     * Define o serviço de funcionários
     */
    public void setFuncionarioService(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }
    
    /**
     * Define o funcionário a ser exibido
     */
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
        carregarDados();
    }
    
    /**
     * Inicializa o controlador
     */
    @FXML
    public void initialize() {
        // Configurar colunas da tabela de avaliações
        colunaData.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDataAvaliacao()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
                    
        colunaExaustao.setCellValueFactory(new PropertyValueFactory<>("nivelExaustao"));
        colunaCinismo.setCellValueFactory(new PropertyValueFactory<>("nivelCinismo"));
        colunaIneficacia.setCellValueFactory(new PropertyValueFactory<>("nivelIneficacia"));
        
        colunaNivelRisco.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getNivelRisco().toString()));
            
        // Configurar colunas da tabela de recomendações
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        
        colunaTipo.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getTipoIntervencao().getDescricao()));
            
        colunaImplementada.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().isImplementada() ? "Sim" : "Não"));
    }
    
    /**
     * Carrega os dados do funcionário
     */
    private void carregarDados() {
        if (funcionario == null) return;
        
        // Dados básicos
        lblNome.setText(funcionario.getNome());
        lblEmail.setText(funcionario.getEmail());
        lblDepartamento.setText(funcionario.getDepartamento());
        lblCargo.setText(funcionario.getCargo());
        
        if (funcionario.getDataContratacao() != null) {
            lblDataContratacao.setText(funcionario.getDataContratacao()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        
        // Avaliações
        List<AvaliacaoBurnout> avaliacoes = funcionario.getAvaliacoes();
        tabelaAvaliacoes.setItems(FXCollections.observableArrayList(avaliacoes));
        
        // Recomendações
        List<Recomendacao> recomendacoes = avaliacoes.stream()
                .flatMap(a -> a.getRecomendacoes().stream())
                .toList();
        tabelaRecomendacoes.setItems(FXCollections.observableArrayList(recomendacoes));
        
        // Gráfico de evolução (simplificado)
        carregarGraficoEvolucao();
    }
    
    /**
     * Carrega o gráfico de evolução
     */
    private void carregarGraficoEvolucao() {
        // Implementação simplificada
        // O gráfico seria carregado com os dados de evolução do burnout ao longo do tempo
    }
} 