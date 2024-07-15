package command;

import vo.Book;
import vo.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookLoanService {
    private Map<String, Book> availableBooks;
    private Map<String, Book> loanedBooks;

    public BookLoanService() {
        availableBooks = new HashMap<>();
        loanedBooks = new HashMap<>();
    }

    public void addBookFromSearchResult(Book book) {
        availableBooks.put(book.getIsbn(), book);
    }

    public Book loanBook(User user, String isbn) {
        Book book = availableBooks.get(isbn);
        if (book != null && book.isLoanAvailable()) {
            book.setLoanAvailable(false);
            book.setReturnDate(generateReturnDate());
            loanedBooks.put(isbn, book);
            user.loanBook(book);
            return book;
        }
        return null;
    }

    public Book returnBook(String isbn) {
        Book book = loanedBooks.get(isbn);
        if (book != null) {
            book.setLoanAvailable(true);
            book.setReturnDate("-");
            loanedBooks.remove(isbn);
            availableBooks.put(isbn, book);
            return book;
        }
        return null;
    }

    public Map<String, Book> getAvailableBooks() {
        return availableBooks;
    }

    public Map<String, Book> getLoanedBooks() {
        return loanedBooks;
    }

    private String generateReturnDate() {
        LocalDate returnDate = LocalDate.now().plusWeeks(2);
        return returnDate.toString();
    }

    public void extendReturnDate(Book book) {
        LocalDate newReturnDate = LocalDate.parse(book.getReturnDate()).plusDays(7);
        book.setReturnDate(newReturnDate.toString());
    }
}
