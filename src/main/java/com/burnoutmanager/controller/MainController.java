package com.burnoutmanager.controller;

import com.burnoutmanager.App;
import com.burnoutmanager.dao.AvaliacaoBurnoutDAO;
import com.burnoutmanager.dao.AvaliacaoBurnoutDAOImpl;
import com.burnoutmanager.dao.FuncionarioDAO;
import com.burnoutmanager.dao.FuncionarioDAOImpl;
import com.burnoutmanager.model.AvaliacaoBurnout;
import com.burnoutmanager.model.Funcionario;
import com.burnoutmanager.service.AvaliacaoBurnoutService;
import com.burnoutmanager.service.AvaliacaoService;
import com.burnoutmanager.service.FuncionarioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML
    private TableView<Funcionario> tabelaFuncionarios;
    
    @FXML
    private TableColumn<Funcionario, String> colunaNome;
    
    @FXML
    private TableColumn<Funcionario, String> colunaEmail;
    
    @FXML
    private TableColumn<Funcionario, String> colunaDepartamento;
    
    @FXML
    private TableColumn<Funcionario, String> colunaCargo;
    
    @FXML
    private TableColumn<Funcionario, String> colunaUltimaAvaliacao;
    
    @FXML
    private TableColumn<Funcionario, String> colunaNivelRisco;
    
    @FXML
    private ComboBox<String> comboDepartamento;
    
    @FXML
    private TextField txtBusca;
    
    @FXML
    private Button btnAdicionar;
    
    @FXML
    private Button btnEditar;
    
    @FXML
    private Button btnExcluir;
    
    @FXML
    private Button btnAvaliar;
    
    @FXML
    private Button btnDetalhes;
    
    @FXML
    private Button btnRelatorios;

    private FuncionarioService funcionarioService;
    private AvaliacaoService avaliacaoService;
    private AvaliacaoBurnoutService avaliacaoBurnoutService;

    private ObservableList<Funcionario> listaFuncionarios;

    @FXML
    public void initialize() {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAOImpl();
        AvaliacaoBurnoutDAO avaliacaoBurnoutDAO = new AvaliacaoBurnoutDAOImpl();
        
        funcionarioService = new FuncionarioService(funcionarioDAO);
        avaliacaoService = new AvaliacaoService(funcionarioDAO);
        avaliacaoBurnoutService = new AvaliacaoBurnoutService(avaliacaoBurnoutDAO, funcionarioDAO);
        
        configurarColunas();
        carregarFuncionarios();
        configurarEventosBotoes();
    }
    
    private void configurarColunas() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colunaDepartamento.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        colunaCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
    }
    
    private void configurarEventosBotoes() {
        btnAdicionar.setOnAction(event -> abrirFormularioFuncionario(null));
        btnEditar.setOnAction(event -> editarFuncionarioSelecionado());
        btnExcluir.setOnAction(event -> excluirFuncionarioSelecionado());
        btnAvaliar.setOnAction(event -> avaliarFuncionarioSelecionado());
        btnDetalhes.setOnAction(event -> verDetalhesFuncionarioSelecionado());
        btnRelatorios.setOnAction(event -> abrirDashboard());
        
        comboDepartamento.setOnAction(event -> filtrarPorDepartamento());
        
        txtBusca.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarPorTexto(newValue);
        });
    }
    
    private void abrirFormularioFuncionario(Funcionario funcionario) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/funcionario_form.fxml"));
            Parent root = loader.load();
            
            FuncionarioFormController controller = loader.getController();
            controller.setFuncionarioService(funcionarioService);
            
            if (funcionario != null) {
                controller.setFuncionario(funcionario);
            }
            
            Stage stage = new Stage();
            stage.setTitle(funcionario == null ? "Adicionar Funcionário" : "Editar Funcionário");
            stage.setScene(new Scene(root, 600, 400));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            carregarFuncionarios();
            
        } catch (IOException e) {
            exibirErro("Erro ao abrir formulário", e.getMessage());
        }
    }
    
    private void editarFuncionarioSelecionado() {
        Funcionario selecionado = tabelaFuncionarios.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            abrirFormularioFuncionario(selecionado);
        } else {
            exibirAlerta("Seleção Necessária", "Por favor, selecione um funcionário para editar.");
        }
    }
    
    private void excluirFuncionarioSelecionado() {
        Funcionario selecionado = tabelaFuncionarios.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Exclusão");
            confirmacao.setHeaderText("Excluir Funcionário");
            confirmacao.setContentText("Tem certeza que deseja excluir o funcionário " + selecionado.getNome() + "?");
            
            confirmacao.showAndWait().ifPresent(resposta -> {
                if (resposta == javafx.scene.control.ButtonType.OK) {
                    boolean sucesso = funcionarioService.getFuncionarioDAO().remover(selecionado.getId());
                    if (sucesso) {
                        carregarFuncionarios();
                        exibirInformacao("Funcionário Excluído", "Funcionário foi excluído com sucesso.");
                    } else {
                        exibirErro("Erro ao Excluir", "Não foi possível excluir o funcionário.");
                    }
                }
            });
        } else {
            exibirAlerta("Seleção Necessária", "Por favor, selecione um funcionário para excluir.");
        }
    }

    private void avaliarFuncionarioSelecionado() {
        Funcionario selecionado = tabelaFuncionarios.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/avaliacao_form.fxml"));
                Parent root = loader.load();
                
                AvaliacaoFormController controller = loader.getController();
                controller.setAvaliacaoService(avaliacaoBurnoutService);
                controller.setFuncionarioService(funcionarioService);
                controller.carregarFuncionarios();
                controller.setFuncionario(selecionado);
                
                Stage stage = new Stage();
                stage.setTitle("Avaliar Burnout - " + selecionado.getNome());
                stage.setScene(new Scene(root, 650, 500));
                stage.initModality(Modality.APPLICATION_MODAL);
                
                stage.showAndWait();
                carregarFuncionarios();
                
            } catch (IOException e) {
                exibirErro("Erro ao abrir formulário", e.getMessage());
            }
        } else {
            exibirAlerta("Seleção Necessária", "Por favor, selecione um funcionário para avaliar.");
        }
    }
    
    private void verDetalhesFuncionarioSelecionado() {
        Funcionario selecionado = tabelaFuncionarios.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/funcionario_detalhes.fxml"));
                Parent root = loader.load();
                
                FuncionarioDetalhesController controller = loader.getController();
                controller.setFuncionarioService(funcionarioService);
                controller.setFuncionario(selecionado);
                
                Stage stage = new Stage();
                stage.setTitle("Detalhes - " + selecionado.getNome());
                stage.setScene(new Scene(root, 800, 600));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                
            } catch (IOException e) {
                exibirErro("Erro ao abrir detalhes", e.getMessage());
            }
        } else {
            exibirAlerta("Seleção Necessária", "Por favor, selecione um funcionário para ver os detalhes.");
        }
    }
    
    private void abrirDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/dashboard.fxml"));
            Parent root = loader.load();
            
            DashboardController controller = loader.getController();
            controller.setFuncionarioService(funcionarioService);
            controller.setAvaliacaoService(avaliacaoService);
            controller.inicializarDashboard();
            
            Stage stage = new Stage();
            stage.setTitle("Dashboard - Burnout");
            stage.setScene(new Scene(root, 900, 700));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
        } catch (IOException e) {
            exibirErro("Erro ao abrir dashboard", e.getMessage());
        }
    }
    
    private void filtrarPorDepartamento() {
        String departamento = comboDepartamento.getValue();
        if (departamento != null && !departamento.isEmpty() && !departamento.equals("Todos")) {
            List<Funcionario> filtrados = funcionarioService.listarPorDepartamento(departamento);
            listaFuncionarios.setAll(filtrados);
        } else {
            carregarFuncionarios();
        }
    }
    
    private void filtrarPorTexto(String texto) {
        if (texto != null && !texto.isEmpty()) {
            final String textoFinal = texto.toLowerCase();
            List<Funcionario> todos = funcionarioService.listarTodos();
            List<Funcionario> filtrados = todos.stream()
                .filter(f -> f.getNome().toLowerCase().contains(textoFinal) || 
                            (f.getEmail() != null && f.getEmail().toLowerCase().contains(textoFinal)))
                .toList();
            listaFuncionarios.setAll(filtrados);
        } else {
            carregarFuncionarios();
        }
    }
    
    private void carregarFuncionarios() {
        List<Funcionario> funcionarios = funcionarioService.listarTodos();
        listaFuncionarios = FXCollections.observableArrayList(funcionarios);
        tabelaFuncionarios.setItems(listaFuncionarios);
    }
    
    private void exibirAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    private void exibirErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    private void exibirInformacao(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
} 