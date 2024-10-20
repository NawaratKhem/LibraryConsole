package Controller;

import java.util.ArrayList;

import Models.Teacher;
import Provider.Instance;

public class TeacherController implements Instance {

    private ArrayList<Teacher> teachers = new ArrayList<>();

    public TeacherController(){
        teachers.add(new Teacher(1, 2001, "Prof. John Smith", "University A"));
        teachers.add(new Teacher(2, 2002, "Prof. Jane Doe", "University B"));
        teachers.add(new Teacher(3, 2003, "Dr. Alex Brown", "University C"));
        teachers.add(new Teacher(4, 2004, "Dr. Emma Green", "University D"));
        teachers.add(new Teacher(5, 2005, "Prof. Michael White", "University E"));
    }
    
    public ArrayList<Teacher> getTeachers(){
        return teachers;
    }
}
