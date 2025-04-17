package com.burnoutmanager.dao;

import com.burnoutmanager.model.Funcionario;
import com.burnoutmanager.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class FuncionarioDAOImpl implements FuncionarioDAO {
    
    @Override
    public Funcionario salvar(Funcionario funcionario) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(funcionario);
            transaction.commit();
            return funcionario;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Funcionario atualizar(Funcionario funcionario) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(funcionario);
            transaction.commit();
            return funcionario;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Funcionario buscarPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Funcionario.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Funcionario buscarPorEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Funcionario> query = session.createQuery(
                    "FROM Funcionario WHERE email = :email", Funcionario.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<Funcionario> listarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Funcionario> query = session.createQuery(
                    "FROM Funcionario", Funcionario.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    @Override
    public List<Funcionario> listarPorDepartamento(String departamento) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Funcionario> query = session.createQuery(
                    "FROM Funcionario WHERE departamento = :departamento", Funcionario.class);
            query.setParameter("departamento", departamento);
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
            Funcionario funcionario = session.get(Funcionario.class, id);
            if (funcionario != null) {
                session.delete(funcionario);
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