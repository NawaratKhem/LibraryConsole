package Models;

public abstract class User {
    public int id;
    public String name;
    public boolean isBanned;

    public int numberOfBorrow;
    public double totalSpent;

    private String password = "101010";

    public User(int id, String name){
        this.id = id;
        this.name = name;
    }

    public void banUser(){
        isBanned = true;
    }

    public void unbanUser(){
        isBanned = false;
    }

    public void setPassword(String pass){
        this.password = pass;
    }

    public String getPassword(){
        return password;
    }
}