package com.burnoutmanager.dao;

import com.burnoutmanager.model.Recomendacao;
import com.burnoutmanager.model.Recomendacao.TipoIntervencao;
import com.burnoutmanager.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class RecomendacaoDAOImpl implements RecomendacaoDAO {

    @Override
    public Recomendacao salvar(Recomendacao recomendacao) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(recomendacao);
            transaction.commit();
            return recomendacao;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Recomendacao atualizar(Recomendacao recomendacao) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(recomendacao);
            transaction.commit();
            return recomendacao;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Recomendacao buscarPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Recomendacao.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Recomendacao> listarPorAvaliacao(Long avaliacaoId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Recomendacao> query = session.createQuery(
                    "FROM Recomendacao WHERE avaliacao.id = :avaliacaoId", Recomendacao.class);
            query.setParameter("avaliacaoId", avaliacaoId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Recomendacao> listarPorFuncionario(Long funcionarioId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Recomendacao> query = session.createQuery(
                    "FROM Recomendacao r WHERE r.avaliacao.funcionario.id = :funcionarioId " +
                    "ORDER BY r.dataRecomendacao DESC", Recomendacao.class);
            query.setParameter("funcionarioId", funcionarioId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Recomendacao> listarPorTipoIntervencao(TipoIntervencao tipoIntervencao) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Recomendacao> query = session.createQuery(
                    "FROM Recomendacao WHERE tipoIntervencao = :tipoIntervencao " +
                    "ORDER BY dataRecomendacao DESC", Recomendacao.class);
            query.setParameter("tipoIntervencao", tipoIntervencao);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Recomendacao> listarPorStatusImplementacao(boolean implementada) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Recomendacao> query = session.createQuery(
                    "FROM Recomendacao WHERE implementada = :implementada " +
                    "ORDER BY dataRecomendacao DESC", Recomendacao.class);
            query.setParameter("implementada", implementada);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Recomendacao> listarPorPrazoPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Recomendacao> query = session.createQuery(
                    "FROM Recomendacao WHERE dataPrazo BETWEEN :dataInicio AND :dataFim " +
                    "ORDER BY dataPrazo ASC", Recomendacao.class);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Recomendacao> listarRecomendacoesVencidas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            LocalDate hoje = LocalDate.now();
            Query<Recomendacao> query = session.createQuery(
                    "FROM Recomendacao WHERE dataPrazo < :hoje AND implementada = false " +
                    "ORDER BY dataPrazo ASC", Recomendacao.class);
            query.setParameter("hoje", hoje);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public boolean remover(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Recomendacao recomendacao = session.get(Recomendacao.class, id);
            if (recomendacao != null) {
                session.delete(recomendacao);
                transaction.commit();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
}