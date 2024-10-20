package Controller;

import java.util.ArrayList;

import Ab_interface.Observer;
import Models.Book;
import Provider.Instance;

public class BookController implements Instance {

    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Observer> observers = new ArrayList<>();

    public BookController() {
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
    
        books.add(new Book(11, "Moby Dick", "Herman Melville", "1851", false));
        books.add(new Book(12, "War and Peace", "Leo Tolstoy", "1869", true));
        books.add(new Book(13, "Ulysses", "James Joyce", "1922", false));
        books.add(new Book(14, "The Divine Comedy", "Dante Alighieri", "1320", true));
        books.add(new Book(15, "Brave New World", "Aldous Huxley", "1932", false));
        books.add(new Book(16, "Crime and Punishment", "Fyodor Dostoevsky", "1866", true));
        books.add(new Book(17, "The Lord of the Rings", "J.R.R. Tolkien", "1954", false));
        books.add(new Book(18, "One Hundred Years of Solitude", "Gabriel Garcia Marquez", "1967", true));
        books.add(new Book(19, "The Brothers Karamazov", "Fyodor Dostoevsky", "1880", false));
        books.add(new Book(20, "The Alchemist", "Paulo Coelho", "1988", true));
    
        books.add(new Book(21, "Les Misérables", "Victor Hugo", "1862", false));
        books.add(new Book(22, "The Picture of Dorian Gray", "Oscar Wilde", "1890", true));
        books.add(new Book(23, "Anna Karenina", "Leo Tolstoy", "1878", false));
        books.add(new Book(24, "Don Quixote", "Miguel de Cervantes", "1615", true));
        books.add(new Book(25, "Wuthering Heights", "Emily Brontë", "1847", false));
        books.add(new Book(26, "Jane Eyre", "Charlotte Brontë", "1847", true));
        books.add(new Book(27, "Fahrenheit 451", "Ray Bradbury", "1953", false));
        books.add(new Book(28, "The Kite Runner", "Khaled Hosseini", "2003", true));
        books.add(new Book(29, "Dracula", "Bram Stoker", "1897", false));
        books.add(new Book(30, "The Odyssey", "Homer", "8th century BC", true));
    
        books.add(new Book(31, "The Iliad", "Homer", "8th century BC", false));
        books.add(new Book(32, "Lolita", "Vladimir Nabokov", "1955", true));
        books.add(new Book(33, "Catch-22", "Joseph Heller", "1961", false));
        books.add(new Book(34, "The Catcher in the Rye", "J.D. Salinger", "1951", true));
        books.add(new Book(35, "The Metamorphosis", "Franz Kafka", "1915", false));
        books.add(new Book(36, "The Shining", "Stephen King", "1977", true));
        books.add(new Book(37, "Frankenstein", "Mary Shelley", "1818", false));
        books.add(new Book(38, "The Secret Garden", "Frances Hodgson Burnett", "1911", true));
        books.add(new Book(39, "The Giver", "Lois Lowry", "1993", false));
        books.add(new Book(40, "Beloved", "Toni Morrison", "1987", true));
    
        books.add(new Book(41, "Slaughterhouse-Five", "Kurt Vonnegut", "1969", false));
        books.add(new Book(42, "The Road", "Cormac McCarthy", "2006", true));
        books.add(new Book(43, "The Grapes of Wrath", "John Steinbeck", "1939", false));
        books.add(new Book(44, "Gone with the Wind", "Margaret Mitchell", "1936", true));
        books.add(new Book(45, "The Sound and the Fury", "William Faulkner", "1929", false));
        books.add(new Book(46, "Heart of Darkness", "Joseph Conrad", "1899", true));
        books.add(new Book(47, "A Tale of Two Cities", "Charles Dickens", "1859", false));
        books.add(new Book(48, "The Handmaid's Tale", "Margaret Atwood", "1985", true));
        books.add(new Book(49, "The Sun Also Rises", "Ernest Hemingway", "1926", false));
        books.add(new Book(50, "Life of Pi", "Yann Martel", "2001", true));
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
