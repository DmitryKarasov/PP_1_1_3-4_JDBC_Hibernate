package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String DB_USERNAME = "myname";
    private static final String DB_PASSWORD = "root";

    public static Connection getConnection() {
        Connection connection = null;

        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);

            connection = DriverManager.getConnection(
                    DB_URL,
                    DB_USERNAME,
                    DB_PASSWORD
            );
            System.out.println("Успешное подключение к базе даных.");
        } catch (SQLException ignored) {
            System.out.println("Не удалось подключиться к базе даных.");
        }
        return connection;
    }

    public static SessionFactory getSessionFactory() {

        Configuration config = new Configuration();
        Properties settings = new Properties();

        settings.put(Environment.JAKARTA_JDBC_DRIVER, "com.mysql.cj.jdbc.Driver");
        settings.put(Environment.JAKARTA_JDBC_URL, DB_URL);
        settings.put(Environment.JAKARTA_JDBC_USER, DB_USERNAME);
        settings.put(Environment.JAKARTA_JDBC_PASSWORD, DB_PASSWORD);
        settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");

        settings.put(Environment.SHOW_SQL, "true");
        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        settings.put(Environment.HBM2DDL_AUTO, "");

        config.setProperties(settings);
        config.addAnnotatedClass(User.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(config.getProperties()).build();

        return config.buildSessionFactory(serviceRegistry);
    }
}
