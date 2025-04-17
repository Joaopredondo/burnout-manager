package com.burnoutmanager.dao;

import com.burnoutmanager.model.Funcionario;
import java.util.List;

public interface FuncionarioDAO {
    
    Funcionario salvar(Funcionario funcionario);
    
    Funcionario atualizar(Funcionario funcionario);
    
    Funcionario buscarPorId(Long id);
    
    Funcionario buscarPorEmail(String email);
    
    List<Funcionario> listarTodos();
    
    List<Funcionario> listarPorDepartamento(String departamento);
    
    boolean remover(Long id);
} 