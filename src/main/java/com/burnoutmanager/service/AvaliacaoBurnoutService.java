package com.burnoutmanager.service;

import com.burnoutmanager.dao.AvaliacaoBurnoutDAO;
import com.burnoutmanager.dao.FuncionarioDAO;
import com.burnoutmanager.model.AvaliacaoBurnout;
import com.burnoutmanager.model.Avaliacao;
import com.burnoutmanager.model.Funcionario;

import java.time.LocalDate;
import java.util.List;

/**
 * Serviço para gerenciar as avaliações de burnout utilizando o modelo de Avaliacao
 * adaptado para funcionar com a classe AvaliacaoBurnout
 */
public class AvaliacaoBurnoutService {

    private final AvaliacaoBurnoutDAO avaliacaoBurnoutDAO;
    private final FuncionarioDAO funcionarioDAO;

    public AvaliacaoBurnoutService(AvaliacaoBurnoutDAO avaliacaoBurnoutDAO, FuncionarioDAO funcionarioDAO) {
        this.avaliacaoBurnoutDAO = avaliacaoBurnoutDAO;
        this.funcionarioDAO = funcionarioDAO;
    }

    /**
     * Converte um objeto Avaliacao para AvaliacaoBurnout
     */
    private AvaliacaoBurnout converterParaAvaliacaoBurnout(Avaliacao avaliacao) {
        Funcionario funcionario = avaliacao.getFuncionario();
        
        AvaliacaoBurnout avaliacaoBurnout = new AvaliacaoBurnout();
        avaliacaoBurnout.setFuncionario(funcionario);
        avaliacaoBurnout.setDataAvaliacao(avaliacao.getData());
        avaliacaoBurnout.setNivelExaustao((int)avaliacao.getNivelExaustao());
        avaliacaoBurnout.setNivelCinismo((int)avaliacao.getNivelDespersonalizacao());
        avaliacaoBurnout.setNivelIneficacia((int)(10 - avaliacao.getNivelRealizacaoPessoal()));
        avaliacaoBurnout.setObservacoes(avaliacao.getObservacoes());
        
        return avaliacaoBurnout;
    }

    /**
     * Salva uma avaliação de burnout no formato Avaliacao
     */
    public void salvar(Avaliacao avaliacao) {
        AvaliacaoBurnout avaliacaoBurnout = converterParaAvaliacaoBurnout(avaliacao);
        Funcionario funcionario = funcionarioDAO.buscarPorId(avaliacao.getFuncionario().getId());
        funcionario.adicionarAvaliacao(avaliacaoBurnout);
        funcionarioDAO.atualizar(funcionario);
    }

    /**
     * Atualiza uma avaliação de burnout no formato Avaliacao
     */
    public void atualizar(Avaliacao avaliacao) {
        AvaliacaoBurnout avaliacaoBurnout = converterParaAvaliacaoBurnout(avaliacao);
        avaliacaoBurnoutDAO.atualizar(avaliacaoBurnout);
    }
} 