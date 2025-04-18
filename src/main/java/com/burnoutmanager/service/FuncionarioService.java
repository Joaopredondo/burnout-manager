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

public class FuncionarioService {

    private final FuncionarioDAO funcionarioDAO;

    public FuncionarioService(FuncionarioDAO funcionarioDAO) {
        this.funcionarioDAO = funcionarioDAO;
    }
    
    public FuncionarioDAO getFuncionarioDAO() {
        return funcionarioDAO;
    }

    public Funcionario cadastrarFuncionario(Funcionario funcionario) {
        return funcionarioDAO.salvar(funcionario);
    }

    public Funcionario atualizarFuncionario(Funcionario funcionario) {
        return funcionarioDAO.atualizar(funcionario);
    }

    public Funcionario buscarPorId(Long id) {
        return funcionarioDAO.buscarPorId(id);
    }

    public Funcionario buscarPorEmail(String email) {
        return funcionarioDAO.buscarPorEmail(email);
    }

    public List<Funcionario> listarTodos() {
        return funcionarioDAO.listarTodos();
    }

    public List<Funcionario> listarPorDepartamento(String departamento) {
        return funcionarioDAO.listarPorDepartamento(departamento);
    }

    public AvaliacaoBurnout adicionarAvaliacao(Long funcionarioId, AvaliacaoBurnout avaliacao) {
        Funcionario funcionario = buscarPorId(funcionarioId);
        if (funcionario == null) {
            throw new IllegalArgumentException("Funcionário não encontrado com ID: " + funcionarioId);
        }
        
        funcionario.adicionarAvaliacao(avaliacao);
        funcionarioDAO.atualizar(funcionario);
        return avaliacao;
    }

    public Map<String, Long> departamentosComMaiorBurnout() {
        List<Funcionario> funcionarios = listarTodos();
        
        return funcionarios.stream()
                .filter(f -> f.getUltimaAvaliacao() != null && 
                        (f.getUltimaAvaliacao().getNivelRisco() == AvaliacaoBurnout.NivelRisco.ALTO || 
                         f.getUltimaAvaliacao().getNivelRisco() == AvaliacaoBurnout.NivelRisco.CRITICO))
                .collect(Collectors.groupingBy(
                        Funcionario::getDepartamento,
                        Collectors.counting()
                ));
    }

    public List<Funcionario> funcionariosEmRiscoCritico() {
        List<Funcionario> funcionarios = listarTodos();
        List<Funcionario> funcionariosEmRisco = new ArrayList<>();
        
        for (Funcionario f : funcionarios) {
            try {
                AvaliacaoBurnout ultimaAvaliacao = f.getUltimaAvaliacao();
                if (ultimaAvaliacao != null && 
                        ultimaAvaliacao.getNivelRisco() == AvaliacaoBurnout.NivelRisco.CRITICO) {
                    funcionariosEmRisco.add(f);
                }
            } catch (Exception e) {
                System.err.println("Erro ao verificar risco crítico: " + e.getMessage());
            }
        }
        
        return funcionariosEmRisco;
    }

    public List<AvaliacaoBurnout> evolucaoBurnout(Long funcionarioId, int meses) {
        Funcionario funcionario = buscarPorId(funcionarioId);
        if (funcionario == null) {
            throw new IllegalArgumentException("Funcionário não encontrado com ID: " + funcionarioId);
        }
        
        LocalDate dataLimite = LocalDate.now().minusMonths(meses);
        
        return funcionario.getAvaliacoes().stream()
                .filter(a -> a.getDataAvaliacao().isAfter(dataLimite))
                .sorted((a1, a2) -> a1.getDataAvaliacao().compareTo(a2.getDataAvaliacao()))
                .collect(Collectors.toList());
    }
}