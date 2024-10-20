package Controller;

import java.time.LocalDate;
import java.util.ArrayList;

import Ab_interface.Observer;
import Models.Admin;
import Models.Book;
import Models.History;
import Models.User;
import Provider.Instance;

public class HistoryController implements Instance {
    ArrayList<History> historyRecords = new ArrayList<>(50);
    private ArrayList<Observer> observers = new ArrayList<>();

    public HistoryController(){
        
        // Generate 20 History records for the provided users
        historyRecords.add(new History(101, "John", LocalDate.of(2023, 9, 15), LocalDate.of(2023, 9, 18)));
        historyRecords.add(new History(102, "Emily", LocalDate.of(2023, 8, 10), LocalDate.of(2023, 8, 14)));
        historyRecords.add(new History(103, "David", LocalDate.of(2023, 7, 20), LocalDate.of(2023, 7, 23)));
        historyRecords.add(new History(201, "Alice", LocalDate.of(2023, 9, 1), LocalDate.of(2023, 9, 5)));
        historyRecords.add(new History(202, "Bob", LocalDate.of(2023, 8, 12), LocalDate.of(2023, 8, 16)));
        historyRecords.add(new History(203, "Charlie", LocalDate.of(2023, 7, 25), LocalDate.of(2023, 7, 29)));
        historyRecords.add(new History(204, "Diana", LocalDate.of(2023, 10, 5), LocalDate.of(2023, 10, 8)));
        historyRecords.add(new History(205, "Eve", LocalDate.of(2023, 9, 10), LocalDate.of(2023, 9, 12)));
        historyRecords.add(new History(201, "Alice", LocalDate.of(2023, 6, 15), LocalDate.of(2023, 6, 18)));
        historyRecords.add(new History(102, "Emily", LocalDate.of(2023, 9, 25), LocalDate.of(2023, 9, 29)));
        historyRecords.add(new History(202, "Bob", LocalDate.of(2023, 8, 1), LocalDate.of(2023, 8, 3)));
        historyRecords.add(new History(203, "Charlie", LocalDate.of(2023, 7, 1), LocalDate.of(2023, 7, 5)));
        historyRecords.add(new History(204, "Diana", LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 4)));
        historyRecords.add(new History(205, "Eve", LocalDate.of(2023, 9, 20), LocalDate.of(2023, 9, 22)));
        historyRecords.add(new History(103, "David", LocalDate.of(2023, 8, 5), LocalDate.of(2023, 8, 9)));
        historyRecords.add(new History(101, "John", LocalDate.of(2023, 7, 1), LocalDate.of(2023, 7, 4)));
        historyRecords.add(new History(102, "Emily", LocalDate.of(2023, 9, 18), LocalDate.of(2023, 9, 21)));
        historyRecords.add(new History(202, "Bob", LocalDate.of(2023, 7, 10), LocalDate.of(2023, 7, 12)));
        historyRecords.add(new History(203, "Charlie", LocalDate.of(2023, 6, 15), LocalDate.of(2023, 6, 18)));
        historyRecords.add(new History(201, "Alice", LocalDate.of(2023, 8, 28), LocalDate.of(2023, 9, 1)));
        
    }

    public ArrayList<History> getHistory(User user){
        if(user instanceof Admin){
            return historyRecords;
        }

        ArrayList<History> userSpecific = new ArrayList<>(20);
        for (History history : historyRecords) {
            if(history.getName().equals(user.name)){
                userSpecific.add(history);
            }
        }
        return userSpecific;
    }

    public void AddHistory(Book book, int numberOfDays){

        LocalDate date = LocalDate.now();
        LocalDate returnDate = date.plusDays(numberOfDays);
        History history = new History(book.getId(), book.getTitle(), date, returnDate);

        notifyObservers();

        historyRecords.add(history);
    }
    

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.onChanged();
        }
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }
}
