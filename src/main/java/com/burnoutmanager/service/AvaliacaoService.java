package com.burnoutmanager.service;

import com.burnoutmanager.dao.FuncionarioDAO;
import com.burnoutmanager.model.AvaliacaoBurnout;
import com.burnoutmanager.model.Funcionario;
import com.burnoutmanager.model.Recomendacao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serviço que gerencia operações relacionadas às avaliações de burnout
 */
public class AvaliacaoService {

    private final FuncionarioDAO funcionarioDAO;

    public AvaliacaoService(FuncionarioDAO funcionarioDAO) {
        this.funcionarioDAO = funcionarioDAO;
    }

    /**
     * Registra uma nova avaliação de burnout para um funcionário
     * @param funcionarioId O ID do funcionário
     * @param nivelExaustao Nível de exaustão (1-10)
     * @param nivelCinismo Nível de cinismo (1-10)
     * @param nivelIneficacia Nível de ineficácia (1-10)
     * @param observacoes Observações sobre a avaliação
     * @return A avaliação registrada
     */
    public AvaliacaoBurnout registrarAvaliacao(
            Long funcionarioId,
            int nivelExaustao,
            int nivelCinismo,
            int nivelIneficacia,
            String observacoes) {
        
        Funcionario funcionario = funcionarioDAO.buscarPorId(funcionarioId);
        if (funcionario == null) {
            throw new IllegalArgumentException("Funcionário não encontrado com ID: " + funcionarioId);
        }
        
        // Validar níveis
        validarNivel(nivelExaustao, "exaustão");
        validarNivel(nivelCinismo, "cinismo");
        validarNivel(nivelIneficacia, "ineficácia");
        
        AvaliacaoBurnout avaliacao = new AvaliacaoBurnout();
        avaliacao.setFuncionario(funcionario);
        avaliacao.setDataAvaliacao(LocalDate.now());
        avaliacao.setNivelExaustao(nivelExaustao);
        avaliacao.setNivelCinismo(nivelCinismo);
        avaliacao.setNivelIneficacia(nivelIneficacia);
        avaliacao.setObservacoes(observacoes);
        
        // Adicionar a avaliação ao funcionário e persistir
        funcionario.adicionarAvaliacao(avaliacao);
        funcionarioDAO.atualizar(funcionario);
        
        // Gerar recomendações automáticas com base no nível de risco
        gerarRecomendacoesAutomaticas(avaliacao);
        
        return avaliacao;
    }
    
    /**
     * Gera recomendações automáticas com base no nível de risco da avaliação
     * @param avaliacao A avaliação para gerar recomendações
     */
    private void gerarRecomendacoesAutomaticas(AvaliacaoBurnout avaliacao) {
        switch (avaliacao.getNivelRisco()) {
            case BAIXO:
                adicionarRecomendacao(
                        avaliacao,
                        "Manter acompanhamento periódico",
                        Recomendacao.TipoIntervencao.INDIVIDUAL
                );
                break;
                
            case MODERADO:
                adicionarRecomendacao(
                        avaliacao,
                        "Realizar check-in semanal com o gestor",
                        Recomendacao.TipoIntervencao.INDIVIDUAL
                );
                adicionarRecomendacao(
                        avaliacao,
                        "Participar de workshop sobre gestão do tempo",
                        Recomendacao.TipoIntervencao.ORGANIZACIONAL
                );
                break;
                
            case ALTO:
                adicionarRecomendacao(
                        avaliacao,
                        "Revisar e reduzir carga de trabalho",
                        Recomendacao.TipoIntervencao.ORGANIZACIONAL
                );
                adicionarRecomendacao(
                        avaliacao,
                        "Iniciar acompanhamento com psicólogo organizacional",
                        Recomendacao.TipoIntervencao.CLINICA
                );
                adicionarRecomendacao(
                        avaliacao,
                        "Considerar realocação de projetos ou funções",
                        Recomendacao.TipoIntervencao.ORGANIZACIONAL
                );
                break;
                
            case CRITICO:
                adicionarRecomendacao(
                        avaliacao,
                        "Consulta com médico do trabalho",
                        Recomendacao.TipoIntervencao.CLINICA
                );
                adicionarRecomendacao(
                        avaliacao,
                        "Avaliação para possível afastamento",
                        Recomendacao.TipoIntervencao.AFASTAMENTO
                );
                adicionarRecomendacao(
                        avaliacao,
                        "Iniciar sessões de terapia",
                        Recomendacao.TipoIntervencao.CLINICA
                );
                adicionarRecomendacao(
                        avaliacao,
                        "Reavaliação completa da carga e condições de trabalho",
                        Recomendacao.TipoIntervencao.ORGANIZACIONAL
                );
                break;
        }
    }
    
    /**
     * Adiciona uma recomendação à avaliação
     */
    private void adicionarRecomendacao(
            AvaliacaoBurnout avaliacao,
            String descricao,
            Recomendacao.TipoIntervencao tipoIntervencao) {
        
        Recomendacao recomendacao = new Recomendacao();
        recomendacao.setAvaliacao(avaliacao);
        recomendacao.setDescricao(descricao);
        recomendacao.setTipoIntervencao(tipoIntervencao);
        recomendacao.setDataRecomendacao(LocalDate.now());
        avaliacao.adicionarRecomendacao(recomendacao);
    }
    
    /**
     * Valida se o nível está dentro da faixa permitida (1-10)
     */
    private void validarNivel(int nivel, String tipo) {
        if (nivel < 1 || nivel > 10) {
            throw new IllegalArgumentException(
                    "Nível de " + tipo + " deve estar entre 1 e 10");
        }
    }
    
    /**
     * Lista funcionários com aumento no nível de burnout entre avaliações consecutivas
     * @return Lista de funcionários com tendência de piora
     */
    public List<Funcionario> listarFuncionariosComTendenciaPiora() {
        List<Funcionario> funcionarios = funcionarioDAO.listarTodos();
        List<Funcionario> funcionariosEmPiora = new ArrayList<>();
        
        for (Funcionario funcionario : funcionarios) {
            List<AvaliacaoBurnout> avaliacoes = funcionario.getAvaliacoes();
            if (avaliacoes.size() < 2) {
                continue;
            }
            
            List<AvaliacaoBurnout> avaliacoesOrdenadas = avaliacoes.stream()
                    .sorted((a1, a2) -> a2.getDataAvaliacao().compareTo(a1.getDataAvaliacao()))
                    .collect(Collectors.toList());
            
            // Verifica se a avaliação mais recente tem pontuação maior que a anterior
            AvaliacaoBurnout ultimaAvaliacao = avaliacoesOrdenadas.get(0);
            AvaliacaoBurnout penultimaAvaliacao = avaliacoesOrdenadas.get(1);
            
            if (ultimaAvaliacao.getPontuacaoMedia() > penultimaAvaliacao.getPontuacaoMedia()) {
                funcionariosEmPiora.add(funcionario);
            }
        }
        
        return funcionariosEmPiora;
    }
    
    /**
     * Retorna estatísticas dos níveis de burnout por departamento
     * @return Mapa com estatísticas por departamento
     */
    public Map<String, Double> estatisticasPorDepartamento() {
        List<Funcionario> funcionarios = funcionarioDAO.listarTodos();
        
        return funcionarios.stream()
                .filter(f -> f.getUltimaAvaliacao() != null)
                .collect(Collectors.groupingBy(
                        Funcionario::getDepartamento,
                        Collectors.averagingDouble(f -> f.getUltimaAvaliacao().getPontuacaoMedia())
                ));
    }
} 