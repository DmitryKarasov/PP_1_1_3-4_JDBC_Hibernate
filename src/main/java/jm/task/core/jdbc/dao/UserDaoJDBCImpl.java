package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
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
            if (tableExists()) {
                System.out.println("Таблица 'users' уже существует.");
            } else {
                String querySql = "CREATE TABLE " + TABLE_NAME +
                        " (id INTEGER not NULL AUTO_INCREMENT," +
                        " name VARCHAR (45) not NULL," +
                        " last_name VARCHAR (45) not NULL," +
                        " age INT (100) not NULL," +
                        " PRIMARY KEY (id))";

                statement.executeUpdate(querySql);
                System.out.println("Таблица 'users' успешно создана.");

            }
        } catch (SQLException ignored) {
            System.out.println("Не удалось создать таблицу.");
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            if (tableExists()) {
                String querySql = "DROP TABLE " + TABLE_NAME;

                statement.executeUpdate(querySql);
                System.out.println("Таблица 'users' успешно удалена.");

            } else {
                System.out.println("Таблица 'users' не существует.");
            }
        } catch (SQLException ignored) {
            System.out.println("Не получилось удалить таблицу.");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Statement statement = connection.createStatement()) {
            if (tableExists()) {
                String querySql = "INSERT INTO " + TABLE_NAME + "(name, last_name, age)" +
                        " VALUES ('" + name + "', '" + lastName + "', " + age + ")";

                statement.executeUpdate(querySql);
                System.out.printf("Пользователь с именем %s, фамилией %s и возрастом %d добавлен.\n",
                        name,
                        lastName,
                        age);

            } else {
                System.out.println("Невозможно добавить пользователя, " +
                        "так как таблица 'users' не существует.");
            }
        } catch (SQLException ignored) {
            System.out.println("Не получилось добавить пользователя.");
        }
    }

    public void removeUserById(long id) {
        if (id < 1) {
            System.out.println("Некорректный id.");
            return;
        }

        try (Statement statement = connection.createStatement()) {
            if (tableExists()) {
                String querySql = "DELETE FROM " + TABLE_NAME + " WHERE id = " + id;

                if (statement.executeUpdate(querySql) == 0) {
                    System.out.println("Пользователя с таким id не существует.");
                } else {
                    System.out.printf("Пользователь с id %d удален.\n", id);
                }

            } else {
                System.out.println("Невозможно удалить пользователя, " +
                        "так как таблица 'users' не существует.");
            }
        } catch (SQLException ignored) {
            System.out.println("Не получилось удалить пользователя.");
        }
    }

    public List<User> getAllUsers() {
        try (Statement statement = connection.createStatement()) {
            if (tableExists()) {
                String querySql = "SELECT * FROM " + TABLE_NAME;

                List<User> users = new ArrayList<>();

                ResultSet resultSet = statement.executeQuery(querySql);
                while (resultSet.next()) {
                    users.add(new User(
                            resultSet.getString("name"),
                            resultSet.getString("last_name"),
                            resultSet.getByte("age")
                    ));
                }

                return users;

            } else {
                System.out.println("Невозможно очистить таблицу, " +
                        "так как таблица 'users' не существует.");
            }
        } catch (SQLException ignored) {
            System.out.println("Не получилось очистить таблицу.");
        }

        return null;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            if (tableExists()) {
                String querySql = "DELETE FROM " + TABLE_NAME;

                statement.executeUpdate(querySql);
                System.out.println("Таблица очищена, все данные удалены.");

            } else {
                System.out.println("Невозможно очистить таблицу, " +
                        "так как таблица 'users' не существует.");
            }
        } catch (SQLException ignored) {
            System.out.println("Не получилось очистить таблицу.");
        }
    }

    private boolean tableExists() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(
                null,
                null,
                TABLE_NAME,
                new String[]{"TABLE"}
        );

        return resultSet.next();
    }
}
