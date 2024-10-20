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
    
        // Add Student objects inline, using the same university name for students with the same teacher
        users.add(new Student(201, 1001, 101, "Alice", "University of Bangkok"));
        users.add(new Student(202, 1002, 101, "Bob", "University of Bangkok"));
        users.add(new Student(203, 1003, 102, "Charlie", "Chiang Mai University"));
        users.add(new Student(204, 1004, 102, "Diana", "Chiang Mai University"));
        users.add(new Student(205, 1005, 103, "Eve", "Mahidol University"));
    
        // Adding more students and using the same university name based on the teacher's id
        users.add(new Student(206, 1006, 101, "Frank", "University of Bangkok"));
        users.add(new Student(207, 1007, 101, "Grace", "University of Bangkok"));
        users.add(new Student(208, 1008, 102, "Hank", "Chiang Mai University"));
        users.add(new Student(209, 1009, 102, "Ivy", "Chiang Mai University"));
        users.add(new Student(210, 1010, 103, "Jack", "Mahidol University"));
        users.add(new Student(211, 1011, 103, "Karen", "Mahidol University"));
    }
    

    public User Authenticate(String userName, String password){
        for (User user : users) {
            if(user.name.equals(userName) && user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }

    public ArrayList<Teacher> getTeachers(){
        ArrayList<Teacher> tobeReturnedTeachers = new ArrayList<>();
        for (User user : users) {
            if(user instanceof Teacher){
                tobeReturnedTeachers.add((Teacher)user);
            }
        }
        return tobeReturnedTeachers;
    }
    
    public ArrayList<Student> getStudents(){
        ArrayList<Student> tobeReturnedStudent = new ArrayList<>();
        for (User user : users) {
            if(user instanceof Student){
                tobeReturnedStudent.add((Student)user);
            }
        }
        return tobeReturnedStudent;
    }
}

