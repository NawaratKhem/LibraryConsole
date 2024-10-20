package Models;

public class Book {
    private int id;
    private String title;
    private String author;
    private String publishDate;
    private boolean isAvailable;

    public double upfrontCost = 30; //Default value
    public double dailyCost = 15;

    public Book(int id, String title, String author, String publishDate, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publishDate = publishDate;
        this.isAvailable = isAvailable;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublishDate() { return publishDate; }
    public boolean getBookStatus() { return isAvailable; }
    public String IsAvailable() { return isAvailable ? "Borrowed" : "Available"; }

    public double TotalCost(int numberOfDays){
        return upfrontCost + (numberOfDays * dailyCost);
    }
}
