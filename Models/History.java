package Models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class History {
    private int id;
    private Book book;
    private String borrowerName;
    private LocalDate borrowedOn;
    private LocalDate returnedOn;
    private double totalCost;

    // Constructor
    public History(int id, Book book, String borrowerName, LocalDate borrowedOn, LocalDate returnedOn) {
        this.id = id;
        this.book = book;
        this.borrowerName = borrowerName;
        this.borrowedOn = borrowedOn;
        this.returnedOn = returnedOn;
        this.totalCost = calculateTotalCost(borrowedOn, returnedOn, book);
    }

    // Calculate the total cost using the book's cost methods
    private double calculateTotalCost(LocalDate borrowedOn, LocalDate returnedOn, Book book) {
        long daysBetween = ChronoUnit.DAYS.between(borrowedOn, returnedOn);
        return book.TotalCost((int) daysBetween);  // Use the book's cost calculation method
    }

    // Getters
    public int getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public LocalDate getBorrowedOn() {
        return borrowedOn;
    }

    public LocalDate getReturnedOn() {
        return returnedOn;
    }

    public double getTotalCost() {
        return totalCost;
    }
}
