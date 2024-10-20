package Controller;

import Models.*;
import Provider.Instance;

import java.util.ArrayList;

public class UserController implements Instance {
    // Create an ArrayList of Users (Teachers, Students, and Admin)
    private ArrayList<User> users = new ArrayList<>();

    public UserController() {
        // Add Admin
        users.add(new Admin(301, "Admin"));
        users.get(0).setPassword("1234");

        // Add Teacher objects inline
        users.add(new Teacher(101, 1, "John", "University of Bangkok"));
        users.add(new Teacher(102, 2, "Emily", "Chiang Mai University"));
        users.add(new Teacher(103, 3, "David", "Mahidol University"));

        // Add Student objects inline
        users.add(new Student(201, 1001, 101,"Alice", "Computer Science University"));
        users.add(new Student(202, 1002, 101,"Bob", "Engineering University"));
        users.add(new Student(203, 1003, 102,"Charlie", "Medical College"));
        users.add(new Student(204, 1004, 102,"Diana", "Law School"));
        users.add(new Student(205, 1005, 103,"Eve", "Business School"));
    }

    public User Authenticate(String userName, String password){
        for (User user : users) {
            if(user.name.equals(userName) && user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }
}
