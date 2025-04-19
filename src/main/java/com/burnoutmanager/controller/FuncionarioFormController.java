package com.burnoutmanager.controller;

import com.burnoutmanager.model.Funcionario;
import com.burnoutmanager.service.FuncionarioService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Controlador para o formulário de cadastro e edição de funcionários
 */
public class FuncionarioFormController {

    @FXML
    private TextField txtNome;
    
    @FXML
    private TextField txtEmail;
    
    @FXML
    private ComboBox<String> comboDepartamento;
    
    @FXML
    private TextField txtCargo;
    
    @FXML
    private DatePicker dateContratacao;
    
    @FXML
    private Button btnSalvar;
    
    @FXML
    private Button btnCancelar;
    
    private Funcionario funcionario;
    private FuncionarioService funcionarioService;
    private boolean modoEdicao = false;
    
    /**
     * Define o serviço de funcionários
     */
    public void setFuncionarioService(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }
    
    /**
     * Define o funcionário para edição
     */
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
        this.modoEdicao = true;
        preencherCampos();
    }
    
    /**
     * Inicializa o controlador
     */
    @FXML
    public void initialize() {
        // Configurar departamentos
        comboDepartamento.getItems().addAll(
                "Tecnologia", "Recursos Humanos", "Financeiro", 
                "Marketing", "Vendas", "Operações", "Suporte"
        );
        
        // Configurar eventos
        btnSalvar.setOnAction(event -> salvarFuncionario());
        btnCancelar.setOnAction(event -> fecharJanela());
        
        // Data de contratação padrão
        dateContratacao.setValue(LocalDate.now());
    }
    
    /**
     * Preenche os campos com os dados do funcionário
     */
    private void preencherCampos() {
        if (funcionario != null) {
            txtNome.setText(funcionario.getNome());
            txtEmail.setText(funcionario.getEmail());
            comboDepartamento.setValue(funcionario.getDepartamento());
            txtCargo.setText(funcionario.getCargo());
            
            if (funcionario.getDataContratacao() != null) {
                dateContratacao.setValue(funcionario.getDataContratacao());
            }
        }
    }
    
    /**
     * Salva o funcionário
     */
    private void salvarFuncionario() {
        if (validarCampos()) {
            String nome = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            String departamento = comboDepartamento.getValue();
            String cargo = txtCargo.getText().trim();
            LocalDate dataContratacao = dateContratacao.getValue();
            
            try {
                if (modoEdicao) {
                    // Atualizar funcionário existente
                    funcionario.setNome(nome);
                    funcionario.setEmail(email);
                    funcionario.setDepartamento(departamento);
                    funcionario.setCargo(cargo);
                    funcionario.setDataContratacao(dataContratacao);
                    
                    funcionarioService.atualizarFuncionario(funcionario);
                } else {
                    // Criar novo funcionário
                    Funcionario novoFuncionario = new Funcionario(nome, email, dataContratacao, departamento, cargo);
                    funcionarioService.cadastrarFuncionario(novoFuncionario);
                }
                
                fecharJanela();
            } catch (Exception e) {
                // Tratamento de erro simplificado
                System.err.println("Erro ao salvar funcionário: " + e.getMessage());
            }
        }
    }
    
    /**
     * Valida os campos do formulário
     */
    private boolean validarCampos() {
        // Implementação simplificada da validação
        return txtNome.getText() != null && !txtNome.getText().trim().isEmpty() &&
                txtEmail.getText() != null && !txtEmail.getText().trim().isEmpty() &&
                comboDepartamento.getValue() != null &&
                txtCargo.getText() != null && !txtCargo.getText().trim().isEmpty();
    }
    
    /**
     * Fecha a janela do formulário
     */
    private void fecharJanela() {
        ((Stage) btnCancelar.getScene().getWindow()).close();
    }
} 