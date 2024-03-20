package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        String querySql = "CREATE TABLE IF NOT EXISTS users" +
                " (id INTEGER not NULL AUTO_INCREMENT," +
                " name VARCHAR (45) not NULL," +
                " last_name VARCHAR (45) not NULL," +
                " age INT (100) not NULL," +
                " PRIMARY KEY (id))";
        executeQuery(querySql);
        System.out.println("Таблица users успешно создана.");
    }

    @Override
    public void dropUsersTable() {
        String querySql = "DROP TABLE IF EXISTS users";
        executeQuery(querySql);
        System.out.println("Таблица users успешно удалена.");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;

        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(
                    new User(
                            name,
                            lastName,
                            age
                    )
            );

            transaction.commit();

            System.out.printf("Пользователь с именем %s, фамилией %s и возрастом %d добавлен.\n",
                    name,
                    lastName,
                    age);

        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            exception.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;

        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.remove(session.get(User.class, id));
            session.getTransaction().commit();

            System.out.printf("Пользователь с id %d удален.\n", id);

        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            exception.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;

        try (Session session = Util.getSessionFactory().openSession()) {
            users = session.createNativeQuery("SELECT * FROM users", User.class).getResultList();
        }

        return users;
    }

    @Override
    public void cleanUsersTable() {
        String querySql = "DELETE FROM users";
        executeQuery(querySql);
        System.out.println("Таблица очищена, все данные удалены.");
    }

    private void executeQuery(String querySql) {
        Transaction transaction = null;

        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.createNativeQuery(querySql, User.class).executeUpdate();
            transaction.commit();

        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            exception.printStackTrace();
        }
    }
}
