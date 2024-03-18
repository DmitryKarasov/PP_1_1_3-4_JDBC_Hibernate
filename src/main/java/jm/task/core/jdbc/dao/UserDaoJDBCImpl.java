package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private static final String TABLE_NAME = "users";
    private final Connection connection;

    public UserDaoJDBCImpl() {
        connection = Util.getConnection();
    }

    public void createUsersTable() {

        try (Statement statement = connection.createStatement()) {

            String querySql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (id INTEGER not NULL AUTO_INCREMENT," +
                    " name VARCHAR (45) not NULL," +
                    " last_name VARCHAR (45) not NULL," +
                    " age INT (100) not NULL," +
                    " PRIMARY KEY (id))";

            statement.executeUpdate(querySql);
            System.out.println("Таблица " + TABLE_NAME + " успешно создана.");

        } catch (SQLException ignored) {
            System.out.println("Не удалось создать таблицу.");
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {

            String querySql = "DROP TABLE IF EXISTS " + TABLE_NAME;

            statement.executeUpdate(querySql);
            System.out.println("Таблица " + TABLE_NAME + " успешно удалена.");

        } catch (SQLException ignored) {
            System.out.println("Не получилось удалить таблицу.");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Statement statement = connection.createStatement()) {

            String querySql = "INSERT INTO " + TABLE_NAME + "(name, last_name, age)" +
                    " VALUES ('" + name + "', '" + lastName + "', " + age + ")";

            statement.executeUpdate(querySql);
            System.out.printf("Пользователь с именем %s, фамилией %s и возрастом %d добавлен.\n",
                    name,
                    lastName,
                    age);

        } catch (SQLException ignored) {
            System.out.println("Не получилось добавить пользователя.");
        }
    }

    public void removeUserById(long id) {

        try (Statement statement = connection.createStatement()) {

            String querySql = "DELETE FROM " + TABLE_NAME + " WHERE id = " + id;

            if (statement.executeUpdate(querySql) == 0) {
                System.out.println("Пользователя с таким id не существует.");
            } else {
                System.out.printf("Пользователь с id %d удален.\n", id);
            }

        } catch (SQLException ignored) {
            System.out.println("Не получилось удалить пользователя.");
        }
    }

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            String querySql = "SELECT * FROM " + TABLE_NAME;

            ResultSet resultSet = statement.executeQuery(querySql);
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getString("name"),
                        resultSet.getString("last_name"),
                        resultSet.getByte("age")
                ));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return users;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            String querySql = "DELETE FROM " + TABLE_NAME;
            statement.executeUpdate(querySql);
            System.out.println("Таблица очищена, все данные удалены.");
        } catch (SQLException ignored) {
            System.out.println("Не получилось очистить таблицу.");
        }
    }
}
