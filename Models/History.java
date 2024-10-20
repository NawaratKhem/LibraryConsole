package Models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class History {
    private int id;
    private String name;
    private LocalDate borrowedOn;
    private LocalDate returnOn;
    private double cost;

    private static final double COST_PER_DAY = 10.0; // 10 baht per day

    // Constructor
    public History(int id, String name, LocalDate borrowedOn, LocalDate returnOn) {
        this.id = id;
        this.name = name;
        this.borrowedOn = borrowedOn;
        this.returnOn = returnOn;
        this.cost = calculateCost(borrowedOn, returnOn);
    }

    // Calculate the cost based on the number of days between borrowedOn and returnOn
    private double calculateCost(LocalDate borrowedOn, LocalDate returnOn) {
        long daysBetween = ChronoUnit.DAYS.between(borrowedOn, returnOn);
        return daysBetween * COST_PER_DAY;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBorrowedOn() {
        return borrowedOn;
    }

    public LocalDate getReturnOn() {
        return returnOn;
    }

    public double getCost() {
        return cost;
    }
}

