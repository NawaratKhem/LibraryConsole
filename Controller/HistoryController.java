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

    public HistoryController(ArrayList<Book> books) {
        historyRecords.add(new History(1, books.get(0), "Alice", LocalDate.of(2023, 9, 5), LocalDate.of(2023, 9, 10)));  // The Great Gatsby
        historyRecords.add(new History(2, books.get(1), "Bob", LocalDate.of(2023, 9, 8), LocalDate.of(2023, 9, 15)));    // 1984
        historyRecords.add(new History(3, books.get(2), "Charlie", LocalDate.of(2023, 8, 18), LocalDate.of(2023, 8, 21)));  // To Kill a Mockingbird
        historyRecords.add(new History(4, books.get(3), "Diana", LocalDate.of(2023, 9, 1), LocalDate.of(2023, 9, 5)));   // The Catcher in the Rye
        historyRecords.add(new History(5, books.get(4), "Eve", LocalDate.of(2023, 8, 12), LocalDate.of(2023, 8, 18)));   // Pride and Prejudice
        historyRecords.add(new History(6, books.get(5), "Alice", LocalDate.of(2023, 7, 22), LocalDate.of(2023, 7, 26)));  // The Hobbit
        historyRecords.add(new History(7, books.get(6), "Bob", LocalDate.of(2023, 7, 5), LocalDate.of(2023, 7, 9)));     // Harry Potter and the Philosopher's Stone
        historyRecords.add(new History(8, books.get(7), "Charlie", LocalDate.of(2023, 6, 20), LocalDate.of(2023, 6, 23))); // The Da Vinci Code
        historyRecords.add(new History(9, books.get(8), "Diana", LocalDate.of(2023, 6, 10), LocalDate.of(2023, 6, 14)));  // The Hunger Games
        historyRecords.add(new History(10, books.get(9), "Eve", LocalDate.of(2023, 6, 8), LocalDate.of(2023, 6, 12)));   // The Girl with the Dragon Tattoo
    
        historyRecords.add(new History(11, books.get(10), "Alice", LocalDate.of(2023, 5, 30), LocalDate.of(2023, 6, 4)));  // Moby Dick
        historyRecords.add(new History(12, books.get(11), "Bob", LocalDate.of(2023, 5, 20), LocalDate.of(2023, 5, 26)));   // War and Peace
        historyRecords.add(new History(13, books.get(12), "Charlie", LocalDate.of(2023, 5, 5), LocalDate.of(2023, 5, 12)));  // Ulysses
        historyRecords.add(new History(14, books.get(13), "Diana", LocalDate.of(2023, 4, 25), LocalDate.of(2023, 4, 30)));  // The Divine Comedy
        historyRecords.add(new History(15, books.get(14), "Eve", LocalDate.of(2023, 4, 18), LocalDate.of(2023, 4, 22)));   // Brave New World
        historyRecords.add(new History(16, books.get(15), "Alice", LocalDate.of(2023, 4, 12), LocalDate.of(2023, 4, 16)));  // Crime and Punishment
        historyRecords.add(new History(17, books.get(16), "Bob", LocalDate.of(2023, 4, 5), LocalDate.of(2023, 4, 9)));    // The Lord of the Rings
        historyRecords.add(new History(18, books.get(17), "Charlie", LocalDate.of(2023, 3, 28), LocalDate.of(2023, 4, 3))); // One Hundred Years of Solitude
        historyRecords.add(new History(19, books.get(18), "Diana", LocalDate.of(2023, 3, 15), LocalDate.of(2023, 3, 20))); // The Brothers Karamazov
        historyRecords.add(new History(20, books.get(19), "Eve", LocalDate.of(2023, 3, 10), LocalDate.of(2023, 3, 15)));  // The Alchemist
    
        historyRecords.add(new History(21, books.get(20), "Alice", LocalDate.of(2023, 2, 28), LocalDate.of(2023, 3, 5)));  // Les Misérables
        historyRecords.add(new History(22, books.get(21), "Bob", LocalDate.of(2023, 2, 18), LocalDate.of(2023, 2, 24)));   // The Picture of Dorian Gray
        historyRecords.add(new History(23, books.get(22), "Charlie", LocalDate.of(2023, 2, 10), LocalDate.of(2023, 2, 15)));  // Anna Karenina
        historyRecords.add(new History(24, books.get(23), "Diana", LocalDate.of(2023, 2, 1), LocalDate.of(2023, 2, 7)));   // Don Quixote
        historyRecords.add(new History(25, books.get(24), "Eve", LocalDate.of(2023, 1, 20), LocalDate.of(2023, 1, 27)));   // Wuthering Heights
        historyRecords.add(new History(26, books.get(25), "Alice", LocalDate.of(2023, 1, 12), LocalDate.of(2023, 1, 18)));  // Jane Eyre
        historyRecords.add(new History(27, books.get(26), "Bob", LocalDate.of(2023, 1, 5), LocalDate.of(2023, 1, 9)));    // Fahrenheit 451
        historyRecords.add(new History(28, books.get(27), "Charlie", LocalDate.of(2022, 12, 25), LocalDate.of(2022, 12, 30))); // The Kite Runner
        historyRecords.add(new History(29, books.get(28), "Diana", LocalDate.of(2022, 12, 15), LocalDate.of(2022, 12, 19)));  // Dracula
        historyRecords.add(new History(30, books.get(29), "Eve", LocalDate.of(2022, 12, 5), LocalDate.of(2022, 12, 10)));  // The Odyssey
    }

    public ArrayList<History> getHistory(User user){
        if(user instanceof Admin){
            return historyRecords;
        }

        ArrayList<History> userSpecific = new ArrayList<>(20);
        for (History history : historyRecords) {
            if(history.getBorrowerName().equals(user.name)){
                userSpecific.add(history);
            }
        }
        return userSpecific;
    }

    public void AddHistory(Book book, int numberOfDays, String borrower){

        LocalDate date = LocalDate.now();
        LocalDate returnDate = date.plusDays(numberOfDays);
        History history = new History(book.getId(), book, borrower, date, returnDate);

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
