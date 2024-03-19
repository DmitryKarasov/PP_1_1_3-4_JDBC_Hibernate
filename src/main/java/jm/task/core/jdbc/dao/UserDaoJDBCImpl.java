package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {
    }

    @Override
    public void createUsersTable() {

        try (Statement statement = Util.getConnection().createStatement()) {

            String querySql = "CREATE TABLE IF NOT EXISTS users" +
                    " (id INTEGER not NULL AUTO_INCREMENT," +
                    " name VARCHAR (45) not NULL," +
                    " last_name VARCHAR (45) not NULL," +
                    " age INT (100) not NULL," +
                    " PRIMARY KEY (id))";

            statement.executeUpdate(querySql);
            System.out.println("Таблица 'users' успешно создана.");

        } catch (SQLException ignored) {
            System.out.println("Не удалось создать таблицу.");
        }
    }

    @Override
    public void dropUsersTable() {

        try (Statement statement = Util.getConnection().createStatement()) {

            statement.executeUpdate("DROP TABLE IF EXISTS users");
            System.out.println("Таблица 'users' успешно удалена.");

        } catch (SQLException ignored) {
            System.out.println("Не получилось удалить таблицу.");
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        try (PreparedStatement statement = Util.getConnection()
                .prepareStatement("INSERT INTO users(name, last_name, age) VALUES (?, ?, ?)")) {

            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            statement.executeUpdate();

            System.out.printf("Пользователь с именем %s, фамилией %s и возрастом %d добавлен.\n",
                    name,
                    lastName,
                    age);

        } catch (SQLException ignored) {
            System.out.println("Не получилось добавить пользователя.");
        }
    }

    @Override
    public void removeUserById(long id) {

        try (PreparedStatement statement = Util.getConnection()
                .prepareStatement("DELETE FROM users WHERE id = ?")) {

            statement.setLong(1, id);

            if (statement.executeUpdate() == 0) {
                System.out.println("Пользователя с таким id не существует.");
            } else {
                System.out.printf("Пользователь с id %d удален.\n", id);
            }

        } catch (SQLException ignored) {
            System.out.println("Не получилось удалить пользователя.");
        }
    }

    @Override
    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        try (Statement statement = Util.getConnection().createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
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

    @Override
    public void cleanUsersTable() {

        try (Statement statement = Util.getConnection().createStatement()) {

            statement.executeUpdate("DELETE FROM users");
            System.out.println("Таблица очищена, все данные удалены.");

        } catch (SQLException ignored) {
            System.out.println("Не получилось очистить таблицу.");
        }
    }
}
