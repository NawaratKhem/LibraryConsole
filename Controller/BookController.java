package Controller;

import java.util.ArrayList;

import Ab_interface.Observer;
import Models.Book;
import Provider.Instance;

public class BookController implements Instance {

    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Observer> observers = new ArrayList<>();

    public BookController(){
        books.add(new Book(1, "The Great Gatsby", "F. Scott Fitzgerald", "1925", false));
        books.add(new Book(2, "1984", "George Orwell", "1949", true));
        books.add(new Book(3, "To Kill a Mockingbird", "Harper Lee", "1960", false));
        books.add(new Book(4, "The Catcher in the Rye", "J.D. Salinger", "1951", true));
        books.add(new Book(5, "Pride and Prejudice", "Jane Austen", "1813", false));
        books.add(new Book(6, "The Hobbit", "J.R.R. Tolkien", "1937", true));
        books.add(new Book(7, "Harry Potter and the Philosopher's Stone", "J.K. Rowling", "1997", false));
        books.add(new Book(8, "The Da Vinci Code", "Dan Brown", "2003", true));
        books.add(new Book(9, "The Hunger Games", "Suzanne Collins", "2008", false));
        books.add(new Book(10, "The Girl with the Dragon Tattoo", "Stieg Larsson", "2005", true));
    }

    public ArrayList<Book> getBooks(){
        return books;
    }

    public Book getBookById(int bookId){
        for (Book book : books) {
            if(book.getId() == bookId){
                return book;
            }
        }
        return null;
    }

    public void AddBook(Book newBook){
        books.add(newBook);
        notifyObservers();
    }

    public void RemoveBook(int book_id){
        for (Book book : books) {
            if(book.getId() == book_id){
                books.remove(book);
                break;
            }
        }
        notifyObservers();
    }

    public void UpdateBook(Book updatedBook){
        for (Book book : books) {
            if(book.getId() == updatedBook.getId()){
                book = updatedBook;
                break;
            }
        }
        notifyObservers();
    }

    public boolean BorrowBook(int bookId, int numberOfDays){
        Book selectedBook = getBookById(bookId);
        if(selectedBook == null){
            return false;
        }

        if(selectedBook.getBookStatus()){
            return false;
        }
        notifyObservers();
        return true;
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
