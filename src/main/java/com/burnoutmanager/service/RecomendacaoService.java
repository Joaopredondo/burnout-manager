package com.burnoutmanager.service;

import com.burnoutmanager.dao.RecomendacaoDAO;
import com.burnoutmanager.model.Recomendacao;
import com.burnoutmanager.model.Recomendacao.TipoIntervencao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serviço que gerencia operações relacionadas às recomendações de intervenção
 */
public class RecomendacaoService {

    private final RecomendacaoDAO recomendacaoDAO;

    public RecomendacaoService(RecomendacaoDAO recomendacaoDAO) {
        this.recomendacaoDAO = recomendacaoDAO;
    }

    /**
     * Salva uma nova recomendação
     * @param recomendacao A recomendação a ser salva
     * @return A recomendação salva com ID
     */
    public Recomendacao salvarRecomendacao(Recomendacao recomendacao) {
        if (recomendacao.getDataRecomendacao() == null) {
            recomendacao.setDataRecomendacao(LocalDate.now());
        }
        return recomendacaoDAO.salvar(recomendacao);
    }

    /**
     * Atualiza uma recomendação existente
     * @param recomendacao A recomendação com dados atualizados
     * @return A recomendação atualizada
     */
    public Recomendacao atualizarRecomendacao(Recomendacao recomendacao) {
        return recomendacaoDAO.atualizar(recomendacao);
    }

    /**
     * Marca uma recomendação como implementada
     * @param recomendacaoId O ID da recomendação
     * @param observacoes Observações sobre a implementação
     * @return A recomendação atualizada
     */
    public Recomendacao marcarComoImplementada(Long recomendacaoId, String observacoes) {
        Recomendacao recomendacao = recomendacaoDAO.buscarPorId(recomendacaoId);
        if (recomendacao == null) {
            throw new IllegalArgumentException("Recomendação não encontrada com ID: " + recomendacaoId);
        }
        
        recomendacao.setImplementada(true);
        recomendacao.setDataImplementacao(LocalDate.now());
        
        if (observacoes != null && !observacoes.isEmpty()) {
            String obsAtuais = recomendacao.getObservacoes();
            String novasObs = (obsAtuais != null && !obsAtuais.isEmpty()) 
                    ? obsAtuais + "\n\nImplementação: " + observacoes 
                    : "Implementação: " + observacoes;
            recomendacao.setObservacoes(novasObs);
        }
        
        return recomendacaoDAO.atualizar(recomendacao);
    }

    /**
     * Define o responsável por uma recomendação
     * @param recomendacaoId O ID da recomendação
     * @param responsavel O nome do responsável
     * @return A recomendação atualizada
     */
    public Recomendacao definirResponsavel(Long recomendacaoId, String responsavel) {
        Recomendacao recomendacao = recomendacaoDAO.buscarPorId(recomendacaoId);
        if (recomendacao == null) {
            throw new IllegalArgumentException("Recomendação não encontrada com ID: " + recomendacaoId);
        }
        
        recomendacao.setResponsavel(responsavel);
        return recomendacaoDAO.atualizar(recomendacao);
    }

    /**
     * Define o prazo para implementação de uma recomendação
     * @param recomendacaoId O ID da recomendação
     * @param dataPrazo A data limite para implementação
     * @return A recomendação atualizada
     */
    public Recomendacao definirPrazo(Long recomendacaoId, LocalDate dataPrazo) {
        Recomendacao recomendacao = recomendacaoDAO.buscarPorId(recomendacaoId);
        if (recomendacao == null) {
            throw new IllegalArgumentException("Recomendação não encontrada com ID: " + recomendacaoId);
        }
        
        if (dataPrazo.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data de prazo não pode ser anterior à data atual");
        }
        
        recomendacao.setDataPrazo(dataPrazo);
        return recomendacaoDAO.atualizar(recomendacao);
    }

    /**
     * Lista recomendações pendentes (não implementadas)
     * @return Lista de recomendações pendentes
     */
    public List<Recomendacao> listarRecomendacoesPendentes() {
        return recomendacaoDAO.listarPorStatusImplementacao(false);
    }

    /**
     * Lista recomendações vencidas (prazo expirado e não implementadas)
     * @return Lista de recomendações vencidas
     */
    public List<Recomendacao> listarRecomendacoesVencidas() {
        return recomendacaoDAO.listarRecomendacoesVencidas();
    }

    /**
     * Lista recomendações de um funcionário específico
     * @param funcionarioId O ID do funcionário
     * @return Lista de recomendações do funcionário
     */
    public List<Recomendacao> listarRecomendacoesPorFuncionario(Long funcionarioId) {
        return recomendacaoDAO.listarPorFuncionario(funcionarioId);
    }

    /**
     * Lista recomendações por tipo de intervenção
     * @param tipoIntervencao O tipo de intervenção
     * @return Lista de recomendações do tipo especificado
     */
    public List<Recomendacao> listarPorTipoIntervencao(TipoIntervencao tipoIntervencao) {
        return recomendacaoDAO.listarPorTipoIntervencao(tipoIntervencao);
    }

    /**
     * Obtém estatísticas de recomendações por tipo de intervenção
     * @return Mapa com contagem por tipo de intervenção
     */
    public Map<TipoIntervencao, Long> estatisticasPorTipoIntervencao() {
        List<Recomendacao> todasRecomendacoes = new ArrayList<>();
        
        // Adiciona todas as recomendações implementadas
        todasRecomendacoes.addAll(recomendacaoDAO.listarPorStatusImplementacao(true));
        
        // Adiciona todas as recomendações pendentes
        todasRecomendacoes.addAll(recomendacaoDAO.listarPorStatusImplementacao(false));
        
        return todasRecomendacoes.stream()
                .collect(Collectors.groupingBy(
                        Recomendacao::getTipoIntervencao,
                        Collectors.counting()
                ));
    }
    
    /**
     * Calcula a taxa de implementação de recomendações por tipo
     * @return Mapa com a taxa de implementação por tipo de intervenção
     */
    public Map<TipoIntervencao, Double> taxaImplementacaoPorTipo() {
        Map<TipoIntervencao, List<Recomendacao>> recomendacoesPorTipo = new HashMap<>();
        
        for (TipoIntervencao tipo : TipoIntervencao.values()) {
            recomendacoesPorTipo.put(tipo, recomendacaoDAO.listarPorTipoIntervencao(tipo));
        }
        
        Map<TipoIntervencao, Double> taxasPorTipo = new HashMap<>();
        
        for (Map.Entry<TipoIntervencao, List<Recomendacao>> entry : recomendacoesPorTipo.entrySet()) {
            TipoIntervencao tipo = entry.getKey();
            List<Recomendacao> recomendacoes = entry.getValue();
            
            if (recomendacoes.isEmpty()) {
                taxasPorTipo.put(tipo, 0.0);
            } else {
                long implementadas = recomendacoes.stream()
                        .filter(Recomendacao::isImplementada)
                        .count();
                
                double taxa = (double) implementadas / recomendacoes.size() * 100.0;
                taxasPorTipo.put(tipo, taxa);
            }
        }
        
        return taxasPorTipo;
    }
} 