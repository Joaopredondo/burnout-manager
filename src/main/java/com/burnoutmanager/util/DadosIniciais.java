package com.burnoutmanager.util;

import com.burnoutmanager.dao.FuncionarioDAO;
import com.burnoutmanager.dao.FuncionarioDAOImpl;
import com.burnoutmanager.model.AvaliacaoBurnout;
import com.burnoutmanager.model.Funcionario;
import com.burnoutmanager.model.Recomendacao;
import com.burnoutmanager.service.AvaliacaoService;
import com.burnoutmanager.service.FuncionarioService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DadosIniciais {
    
    private static final Random random = new Random();
    
    public static void carregarDados() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            
            FuncionarioDAO funcionarioDAO = new FuncionarioDAOImpl();
            FuncionarioService funcionarioService = new FuncionarioService(funcionarioDAO);
            AvaliacaoService avaliacaoService = new AvaliacaoService(funcionarioDAO);
            
            List<String> departamentos = Arrays.asList(
                    "Tecnologia", "Recursos Humanos", "Financeiro", 
                    "Marketing", "Vendas", "Operações", "Suporte"
            );
            
            List<List<String>> cargosPorDepartamento = Arrays.asList(
                    Arrays.asList("Desenvolvedor", "Analista de Sistemas", "Arquiteto de Software", "DevOps", "Gerente de TI"),
                    Arrays.asList("Analista de RH", "Recrutador", "Gerente de RH", "Especialista em Benefícios"),
                    Arrays.asList("Analista Financeiro", "Contador", "Controller", "Gerente Financeiro"),
                    Arrays.asList("Analista de Marketing", "Social Media", "Gerente de Marketing", "Designer"),
                    Arrays.asList("Vendedor", "Gerente de Contas", "Gerente de Vendas", "Analista Comercial"),
                    Arrays.asList("Analista de Operações", "Coordenador de Logística", "Gerente de Operações"),
                    Arrays.asList("Analista de Suporte", "Técnico de Suporte", "Gerente de Suporte")
            );
            
            for (int i = 0; i < departamentos.size(); i++) {
                String departamento = departamentos.get(i);
                List<String> cargos = cargosPorDepartamento.get(i);
                
                int numFuncionarios = 3 + random.nextInt(3);
                for (int j = 0; j < numFuncionarios; j++) {
                    String cargo = cargos.get(random.nextInt(cargos.size()));
                    String nomeFuncionario = gerarNomeFuncionario();
                    String email = gerarEmail(nomeFuncionario, departamento);
                    
                    if (funcionarioService.buscarPorEmail(email) != null) {
                        email = gerarEmail(nomeFuncionario, departamento);
                        
                        if (funcionarioService.buscarPorEmail(email) != null) {
                            continue;
                        }
                    }
                    
                    Funcionario funcionario = new Funcionario(
                            nomeFuncionario,
                            email,
                            LocalDate.now().minusMonths(random.nextInt(36)),
                            departamento,
                            cargo
                    );
                    
                    session.save(funcionario);
                    
                    int numAvaliacoes = 1 + random.nextInt(3);
                    for (int k = 0; k < numAvaliacoes; k++) {
                        LocalDate dataAvaliacao = LocalDate.now().minusDays(random.nextInt(180));
                        
                        int nivelExaustao = 1 + random.nextInt(10);
                        int nivelCinismo = 1 + random.nextInt(10);
                        int nivelIneficacia = 1 + random.nextInt(10);
                        
                        AvaliacaoBurnout avaliacao = new AvaliacaoBurnout(
                                funcionario,
                                nivelExaustao,
                                nivelCinismo,
                                nivelIneficacia
                        );
                        avaliacao.setDataAvaliacao(dataAvaliacao);
                        avaliacao.setObservacoes("Avaliação realizada em " + dataAvaliacao);
                        
                        funcionario.adicionarAvaliacao(avaliacao);
                        session.save(avaliacao);
                    }
                    
                    session.update(funcionario);
                }
            }
            
            transaction.commit();
            System.out.println("Dados iniciais carregados com sucesso!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Erro ao carregar dados iniciais: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    private static String gerarNomeFuncionario() {
        List<String> nomes = Arrays.asList(
                "Ana", "Carlos", "Maria", "João", "Pedro", "Mariana", "Lucas", "Juliana",
                "Rafael", "Fernanda", "Gabriel", "Aline", "Ricardo", "Camila", "Bruno",
                "Patricia", "Rodrigo", "Luciana", "Paulo", "Cristina", "Felipe", "Tatiana"
        );
        
        List<String> sobrenomes = Arrays.asList(
                "Silva", "Santos", "Oliveira", "Souza", "Costa", "Pereira", "Ferreira", "Rodrigues",
                "Almeida", "Nascimento", "Lima", "Araújo", "Fernandes", "Carvalho", "Gomes",
                "Martins", "Rocha", "Ribeiro", "Alves", "Monteiro", "Mendes", "Barros", "Freitas", "Barbosa"
        );
        
        String nome = nomes.get(random.nextInt(nomes.size()));
        String sobrenome = sobrenomes.get(random.nextInt(sobrenomes.size()));
        
        return nome + " " + sobrenome;
    }
    
    private static String gerarEmail(String nome, String departamento) {
        String[] partes = nome.toLowerCase().split(" ");
        String primeiroNome = partes[0];
        String ultimoNome = partes[partes.length - 1];
        
        int sufixo = random.nextInt(1000);
        return primeiroNome + "." + ultimoNome + sufixo + "@empresa.com";
    }
}