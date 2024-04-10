package br.com.imc.DAO;

import br.com.imc.util.HibernateUtil;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class GenericDAO<Entity> {

    private final Class<Entity> classe;

    @SuppressWarnings("unchecked")
    public GenericDAO() {
        //conseguir a classe filha
        this.classe = (Class<Entity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void salvar(Entity entity) {
        //abrir uma sessão
        Session session = HibernateUtil.getSessionFactory().openSession();
        //criando uma transação
        Transaction transaction = null;
        try {
            //pegar uma sessão aberta para iniciar a transação
            transaction = session.beginTransaction();
            session.save(entity);
            //finaliza a transação
            transaction.commit();
        } catch (RuntimeException ex) {
            //verifica se a transação foi aberta, se sim, houve um erro
            if (transaction != null) {
                //desfazer as alterações
                transaction.rollback();
            }
            //mostrar a mensagem de erro
            throw ex;
        } finally {
            //fechar a sessão
            session.close();
        }
    }

    public void atualizar(Entity entity) {
        //abrir uma sessão
        Session session = HibernateUtil.getSessionFactory().openSession();
        //criando uma transação
        Transaction transaction = null;
        try {
            //pegar uma sessão aberta para iniciar a transação
            transaction = session.beginTransaction();
            session.update(entity);
            //finaliza a transação
            transaction.commit();
        } catch (RuntimeException ex) {
            //verifica se a transação foi aberta, se sim, houve um erro
            if (transaction != null) {
                //desfazer as alterações
                transaction.rollback();
            }
            //mostrar a mensagem de erro
            throw ex;
        } finally {
            //fechar a sessão
            session.close();
        }
    }

    public void excluir(Entity entity) {
        //abrir uma sessão
        Session session = HibernateUtil.getSessionFactory().openSession();
        //criando uma transação
        Transaction transaction = null;
        try {
            //pegar uma sessão aberta para iniciar a transação
            transaction = session.beginTransaction();
            session.delete(entity);
            //finaliza a transação
            transaction.commit();
        } catch (RuntimeException ex) {
            //verifica se a transação foi aberta, se sim, houve um erro
            if (transaction != null) {
                //desfazer as alterações
                transaction.rollback();
            }
            //mostrar a mensagem de erro
            throw ex;
        } finally {
            //fechar a sessão
            session.close();
        }
    }

    public Entity merge(Entity entity) {
        //abrir uma sessão
        Session session = HibernateUtil.getSessionFactory().openSession();
        //criando uma transação
        Transaction transaction = null;
        try {
            //pegar uma sessão aberta para iniciar a transação
            transaction = session.beginTransaction();
            @SuppressWarnings("unchecked")
            Entity resultado = (Entity) session.merge(entity);
            //finaliza a transação
            transaction.commit();
            return resultado;
        } catch (RuntimeException ex) {
            //verifica se a transação foi aberta, se sim, houve um erro
            if (transaction != null) {
                //desfazer as alterações
                transaction.rollback();
            }
            //mostrar a mensagem de erro
            throw ex;
        } finally {
            //fechar a sessão
            session.close();
        }
    }

    public List<Entity> listar() {
        //abrir uma sessão
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            //                   createCriteria(classe);
            //acessar a tabela do banco de dados Aluno, Turma ou Imc
            @SuppressWarnings("deprecation")
            Criteria c = session.createCriteria(classe);
            @SuppressWarnings("unchecked")
            List<Entity> resultado = c.list();
            return resultado;
        } catch (RuntimeException ex) {
            throw ex;
        } finally {
            session.close();
        }

    }

    public Entity buscar(Integer id) {
        //abrir uma sessão
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            //                   createCriteria(classe);
            //acessar a tabela do banco de dados Aluno, Turma ou Imc
            @SuppressWarnings("deprecation")
            Criteria c = session.createCriteria(classe);
            //adicionar uma restrição. Sql é o "where"
            c.add(Restrictions.idEq(id));
            @SuppressWarnings("unchecked")
            Entity resultado = (Entity) c.uniqueResult();
            return resultado;
        } catch (RuntimeException ex) {
            throw ex;
        } finally {
            session.close();
        }

    }

}
