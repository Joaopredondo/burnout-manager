package com.burnoutmanager.dao;

import com.burnoutmanager.model.AvaliacaoBurnout;
import com.burnoutmanager.model.AvaliacaoBurnout.NivelRisco;
import com.burnoutmanager.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class AvaliacaoBurnoutDAOImpl implements AvaliacaoBurnoutDAO {

    @Override
    public AvaliacaoBurnout salvar(AvaliacaoBurnout avaliacao) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(avaliacao);
            transaction.commit();
            return avaliacao;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public AvaliacaoBurnout atualizar(AvaliacaoBurnout avaliacao) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(avaliacao);
            transaction.commit();
            return avaliacao;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public AvaliacaoBurnout buscarPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(AvaliacaoBurnout.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<AvaliacaoBurnout> listarPorFuncionario(Long funcionarioId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<AvaliacaoBurnout> query = session.createQuery(
                    "FROM AvaliacaoBurnout WHERE funcionario.id = :funcionarioId " +
                    "ORDER BY dataAvaliacao DESC", AvaliacaoBurnout.class);
            query.setParameter("funcionarioId", funcionarioId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<AvaliacaoBurnout> listarUltimasAvaliacoes() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT a FROM AvaliacaoBurnout a WHERE a.dataAvaliacao = " +
                    "(SELECT MAX(b.dataAvaliacao) FROM AvaliacaoBurnout b WHERE b.funcionario.id = a.funcionario.id)";
            Query<AvaliacaoBurnout> query = session.createQuery(hql, AvaliacaoBurnout.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<AvaliacaoBurnout> listarPorNivelRisco(NivelRisco nivelRisco) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<AvaliacaoBurnout> query = session.createQuery(
                    "FROM AvaliacaoBurnout WHERE nivelRisco = :nivelRisco " +
                    "ORDER BY dataAvaliacao DESC", AvaliacaoBurnout.class);
            query.setParameter("nivelRisco", nivelRisco);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<AvaliacaoBurnout> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<AvaliacaoBurnout> query = session.createQuery(
                    "FROM AvaliacaoBurnout WHERE dataAvaliacao BETWEEN :dataInicio AND :dataFim " +
                    "ORDER BY dataAvaliacao DESC", AvaliacaoBurnout.class);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
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
            AvaliacaoBurnout avaliacao = session.get(AvaliacaoBurnout.class, id);
            if (avaliacao != null) {
                session.delete(avaliacao);
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