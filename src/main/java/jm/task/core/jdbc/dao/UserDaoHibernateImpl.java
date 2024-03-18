package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final String TABLE_NAME = "users";
    private final SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
        sessionFactory = Util.getSessionFactory();
    }

    @Override
    public void createUsersTable() {

        String querySql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (id INTEGER not NULL AUTO_INCREMENT," +
                " name VARCHAR (45) not NULL," +
                " last_name VARCHAR (45) not NULL," +
                " age INT (100) not NULL," +
                " PRIMARY KEY (id))";

        executeQuery(querySql);
        System.out.println("Таблица " + TABLE_NAME + " успешно создана.");
    }

    @Override
    public void dropUsersTable() {
        String querySql = "DROP TABLE IF EXISTS " + TABLE_NAME;

        executeQuery(querySql);

        System.out.println("Таблица " + TABLE_NAME + " успешно удалена.");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.persist(
                    new User(
                            name,
                            lastName,
                            age
                    )
            );

            session.getTransaction().commit();

            System.out.printf("Пользователь с именем %s, фамилией %s и возрастом %d добавлен.\n",
                    name,
                    lastName,
                    age);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.remove(session.get(User.class, id));
            session.getTransaction().commit();

            System.out.printf("Пользователь с id %d удален.\n", id);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;

        try (Session session = sessionFactory.openSession()) {
            users = session.createNativeQuery("SELECT * FROM " + TABLE_NAME, User.class).getResultList();
        }

        return users;
    }

    @Override
    public void cleanUsersTable() {
        String querySql = "DELETE FROM " + TABLE_NAME;

        executeQuery(querySql);

        System.out.println("Таблица очищена, все данные удалены.");
    }

    private void executeQuery(String querySql) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.createNativeQuery(querySql, User.class).executeUpdate();
            session.getTransaction().commit();
        }
    }
}
