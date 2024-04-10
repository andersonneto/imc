package br.com.imc.util;

import br.com.imc.domain.Aluno;
import br.com.imc.domain.Turma;
import java.sql.Connection;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

    private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "org.mariadb.jdbc.Driver");
                settings.put(Environment.URL, "jdbc:mariadb://localhost:3306/imc");
                settings.put(Environment.USER, "admin"); //usuário do banco
                settings.put(Environment.PASS, ""); //senha do banco
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MariaDB10Dialect");
                settings.put(Environment.SHOW_SQL, "true"); //mostrar codigo sql
                settings.put(Environment.FORMAT_SQL, "false"); //codigo sql formatado
                settings.put(Environment.HBM2DDL_AUTO, "update"); //atualizar o banco de dados automaticamente

                configuration.setProperties(settings);

                //adicionar as classes
                configuration.addAnnotatedClass(Aluno.class);
                configuration.addAnnotatedClass(Turma.class);

                serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
                        .build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                // sessionFactory.withOptions().jdbcTimeZone(TimeZone.getTimeZone("UTC"));
            } catch (HibernateException e) {
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (serviceRegistry != null) {
            StandardServiceRegistryBuilder.destroy(serviceRegistry);
            serviceRegistry.close();
        }
    }

    public static Connection getConnection() {
        Session session = getSessionFactory().openSession();
        Connection connection = session.doReturningWork((Connection c) -> c);
        return connection;
    }
}
