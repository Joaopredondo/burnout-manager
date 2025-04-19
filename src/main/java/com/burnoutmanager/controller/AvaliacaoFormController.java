package com.burnoutmanager.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.burnoutmanager.model.Avaliacao;
import com.burnoutmanager.model.Funcionario;
import com.burnoutmanager.service.AvaliacaoService;
import com.burnoutmanager.service.AvaliacaoBurnoutService;
import com.burnoutmanager.service.FuncionarioService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class AvaliacaoFormController implements Initializable {

    @FXML
    private ComboBox<Funcionario> comboFuncionario;
    
    @FXML
    private DatePicker datePicker;
    
    @FXML
    private Slider sliderExaustao;
    
    @FXML
    private Slider sliderDespersonalizacao;
    
    @FXML
    private Slider sliderRealizacao;
    
    @FXML
    private Slider sliderCargaTrabalho;
    
    @FXML
    private Slider sliderApoioSocial;
    
    @FXML
    private TextArea txtObservacoes;
    
    @FXML
    private TextArea txtRecomendacoes;
    
    @FXML
    private Button btnCancelar;
    
    @FXML
    private Button btnSalvar;
    
    private AvaliacaoService avaliacaoService;
    private AvaliacaoBurnoutService avaliacaoBurnoutService;
    private FuncionarioService funcionarioService;
    private Avaliacao avaliacao;
    private boolean editando = false;
    
    public AvaliacaoFormController() {
    }
    
    public void setAvaliacaoService(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }
    
    public void setAvaliacaoService(AvaliacaoBurnoutService avaliacaoBurnoutService) {
        this.avaliacaoBurnoutService = avaliacaoBurnoutService;
    }
    
    public void setFuncionarioService(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }
    
    public void setFuncionario(Funcionario funcionario) {
        if (comboFuncionario != null) {
            comboFuncionario.setValue(funcionario);
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregarFuncionarios();
        configurarComponents();
        
        datePicker.setValue(LocalDate.now());
    }
    
    public void carregarFuncionarios() {
        try {
            if (funcionarioService != null) {
                comboFuncionario.setItems(FXCollections.observableArrayList(
                        funcionarioService.listarTodos()));
            }
        } catch (Exception e) {
            mostrarAlerta("Erro ao carregar funcionários: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    private void configurarComponents() {
        configurarSlider(sliderExaustao, 0, 10, 0);
        configurarSlider(sliderDespersonalizacao, 0, 10, 0);
        configurarSlider(sliderRealizacao, 0, 10, 0);
        configurarSlider(sliderCargaTrabalho, 0, 10, 0);
        configurarSlider(sliderApoioSocial, 0, 10, 0);
        
        btnCancelar.setOnAction(e -> cancelar());
        btnSalvar.setOnAction(e -> salvar());
    }
    
    private void configurarSlider(Slider slider, double min, double max, double valor) {
        slider.setMin(min);
        slider.setMax(max);
        slider.setValue(valor);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setBlockIncrement(1);
    }
    
    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
        this.editando = true;
        
        comboFuncionario.setValue(avaliacao.getFuncionario());
        datePicker.setValue(avaliacao.getData());
        sliderExaustao.setValue(avaliacao.getNivelExaustao());
        sliderDespersonalizacao.setValue(avaliacao.getNivelDespersonalizacao());
        sliderRealizacao.setValue(avaliacao.getNivelRealizacaoPessoal());
        sliderCargaTrabalho.setValue(avaliacao.getNivelCargaTrabalho());
        sliderApoioSocial.setValue(avaliacao.getNivelApoioSocial());
        txtObservacoes.setText(avaliacao.getObservacoes());
        txtRecomendacoes.setText(avaliacao.getRecomendacoes());
    }
    
    @FXML
    private void salvar() {
        try {
            if (!validarFormulario()) {
                return;
            }
            
            if (avaliacao == null) {
                avaliacao = new Avaliacao();
            }
            
            avaliacao.setFuncionario(comboFuncionario.getValue());
            avaliacao.setData(datePicker.getValue());
            avaliacao.setNivelExaustao(sliderExaustao.getValue());
            avaliacao.setNivelDespersonalizacao(sliderDespersonalizacao.getValue());
            avaliacao.setNivelRealizacaoPessoal(sliderRealizacao.getValue());
            avaliacao.setNivelCargaTrabalho(sliderCargaTrabalho.getValue());
            avaliacao.setNivelApoioSocial(sliderApoioSocial.getValue());
            avaliacao.setObservacoes(txtObservacoes.getText());
            avaliacao.setRecomendacoes(txtRecomendacoes.getText());
            
            avaliacao.calcularIndiceBurnout();
            
            if (avaliacaoBurnoutService != null) {
                if (editando) {
                    avaliacaoBurnoutService.atualizar(avaliacao);
                } else {
                    avaliacaoBurnoutService.salvar(avaliacao);
                }
                mostrarAlerta("Avaliação " + (editando ? "atualizada" : "registrada") + " com sucesso!", AlertType.INFORMATION);
            } else if (avaliacaoService != null) {
                mostrarAlerta("Avaliação " + (editando ? "atualizada" : "registrada") + " com sucesso!", AlertType.INFORMATION);
            } else {
                mostrarAlerta("Nenhum serviço de avaliação disponível!", AlertType.ERROR);
                return;
            }
            
            fecharJanela();
            
        } catch (Exception e) {
            mostrarAlerta("Erro ao salvar avaliação: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    private boolean validarFormulario() {
        StringBuilder erros = new StringBuilder();
        
        if (comboFuncionario.getValue() == null) {
            erros.append("- Selecione um funcionário\n");
        }
        
        if (datePicker.getValue() == null) {
            erros.append("- Informe a data da avaliação\n");
        }
        
        if (erros.length() > 0) {
            mostrarAlerta("Por favor, corrija os seguintes campos:\n" + erros.toString(), AlertType.WARNING);
            return false;
        }
        
        return true;
    }
    
    @FXML
    private void cancelar() {
        fecharJanela();
    }
    
    private void fecharJanela() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
    
    private void mostrarAlerta(String mensagem, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(tipo == AlertType.ERROR ? "Erro" : 
                      tipo == AlertType.WARNING ? "Aviso" : "Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
} 