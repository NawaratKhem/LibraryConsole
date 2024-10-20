package Models;

public class Teacher extends User {

    public int teacherId;
    public String universityName;

    public Teacher(int id, int teacherId, String name, String universityName) {
        super(id, name);
        this.teacherId = teacherId;
        this.universityName = universityName;
    }
}
