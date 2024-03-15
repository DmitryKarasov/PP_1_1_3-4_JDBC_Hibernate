package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        UserService service = new UserServiceImpl();

        service.createUsersTable();
        service.saveUser("Ivan", "Ivanov", (byte) 15);
        service.saveUser("Petr", "Petrov", (byte) 20);
        service.saveUser("Vasya", "Vasechkin", (byte) 25);
        service.saveUser("Roman", "Romanov", (byte) 30);

        List<User> users = service.getAllUsers();

        for (User user : users) {
            System.out.println(user);
        }

        service.cleanUsersTable();
        service.dropUsersTable();

    }
}
