package com.burnoutmanager.dao;

import com.burnoutmanager.model.Recomendacao;
import com.burnoutmanager.model.Recomendacao.TipoIntervencao;

import java.time.LocalDate;
import java.util.List;

public interface RecomendacaoDAO {
    
    Recomendacao salvar(Recomendacao recomendacao);
    
    Recomendacao atualizar(Recomendacao recomendacao);
    
    Recomendacao buscarPorId(Long id);
    
    List<Recomendacao> listarPorAvaliacao(Long avaliacaoId);
    
    List<Recomendacao> listarPorFuncionario(Long funcionarioId);
    
    List<Recomendacao> listarPorTipoIntervencao(TipoIntervencao tipoIntervencao);
    
    List<Recomendacao> listarPorStatusImplementacao(boolean implementada);
    
    List<Recomendacao> listarPorPrazoPeriodo(LocalDate dataInicio, LocalDate dataFim);
    
    List<Recomendacao> listarRecomendacoesVencidas();
    
    boolean remover(Long id);
} 