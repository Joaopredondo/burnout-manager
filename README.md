# Sistema de Gerenciamento de Burnout

Este sistema foi desenvolvido para ajudar empresas a monitorar, avaliar e gerenciar o nível de burnout de seus funcionários.

## Funcionalidades

- Cadastro e gestão de funcionários
- Registro e histórico de avaliações de burnout
- Classificação automática de risco (Baixo, Moderado, Alto, Crítico)
- Geração de recomendações de intervenção baseadas no nível de burnout
- Acompanhamento de implementação das recomendações
- Dashboard com estatísticas e alertas
- Relatórios por departamento e níveis de risco
- Identificação precoce de tendências de burnout

## Modelo de avaliação

O sistema utiliza três dimensões principais para avaliar o burnout:

1. **Exaustão**: Sensação de esgotamento, fadiga crônica e falta de energia
2. **Cinismo**: Distanciamento emocional do trabalho e atitudes negativas
3. **Ineficácia**: Sentimento de incompetência e falta de realização profissional

Cada dimensão é avaliada em uma escala de 1 a 10, e uma fórmula ponderada gera o nível de risco.

## Tipos de intervenções

O sistema sugere quatro tipos de intervenções:

- **Individual**: Ações focadas no colaborador (ex: técnicas de gestão de tempo)
- **Organizacional**: Mudanças no ambiente de trabalho (ex: redistribuição de tarefas)
- **Clínica**: Acompanhamento com profissionais de saúde (ex: psicólogo)
- **Afastamento**: Quando necessário para recuperação do funcionário

## Requisitos técnicos

- Java 11 ou superior
- Maven 3.6 ou superior
- JavaFX 17 (interface temporária)
- Hibernate 5.6 (ORM)
- Banco de dados H2 (em memória)

## Interface do Usuário

Atualmente, a interface do usuário está sendo desenvolvida em JavaFX como uma solução temporária. Está planejada uma migração futura para React com chadcn/ui, que proporcionará uma experiência mais moderna e responsiva.

## Como executar

1. Clone o repositório
2. Execute `mvn clean install` para compilar e baixar as dependências
3. Execute `mvn javafx:run` para iniciar a aplicação

## Estrutura do projeto

- `model`: Classes que representam as entidades do sistema
- `dao`: Classes para acesso a dados (DAO pattern)
- `service`: Classes com a lógica de negócio
- `controller`: Controladores da interface gráfica
- `view`: Arquivos FXML para a interface gráfica (será migrado para React)
- `util`: Classes utilitárias

## Banco de dados

O sistema utiliza um banco de dados H2 em memória por padrão, que é inicializado com dados de exemplo. Os dados são perdidos ao encerrar a aplicação, mas isso pode ser alterado na configuração do Hibernate para manter persistência em arquivo.

## Possíveis melhorias futuras

- Migração da interface para React com chadcn/ui
- Integração com sistemas de RH
- Comparação entre departamentos e equipes
- Análise de tendências de longo prazo
- Versão mobile para avaliações
- Notificações automatizadas
- Integração com calendário para acompanhamento