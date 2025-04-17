package com.burnoutmanager.dao;

import com.burnoutmanager.model.AvaliacaoBurnout;
import com.burnoutmanager.model.AvaliacaoBurnout.NivelRisco;

import java.time.LocalDate;
import java.util.List;

public interface AvaliacaoBurnoutDAO {
    
    AvaliacaoBurnout salvar(AvaliacaoBurnout avaliacao);
    
    AvaliacaoBurnout atualizar(AvaliacaoBurnout avaliacao);
    
    AvaliacaoBurnout buscarPorId(Long id);
    
    List<AvaliacaoBurnout> listarPorFuncionario(Long funcionarioId);
    
    List<AvaliacaoBurnout> listarUltimasAvaliacoes();
    
    List<AvaliacaoBurnout> listarPorNivelRisco(NivelRisco nivelRisco);
    
    List<AvaliacaoBurnout> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim);
    
    boolean remover(Long id);
}