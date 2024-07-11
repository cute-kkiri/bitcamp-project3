import java.util.HashMap;
import java.util.Map;

public class BookLoanService {
    private Map<String, Book> availableBooks;
    private Map<String, Book> loanedBooks;

    public BookLoanService() {
        availableBooks = new HashMap<>();
        loanedBooks = new HashMap<>();
    }

    // 네이버 API에서 검색한 책 정보를 저장하는 메서드
    public void addBookFromSearchResult(Book book) {
        availableBooks.put(book.getIsbn(), book);
    }

    public Book loanBook(String isbn) {
        Book book = availableBooks.get(isbn);
        if (book != null) {
            availableBooks.remove(isbn);
            loanedBooks.put(isbn, book);
            return book;
        } else {
            return null; // 대출 실패: 책을 찾을 수 없거나 이미 대출 중
        }
    }

    public Book returnBook(String isbn) {
        Book book = loanedBooks.get(isbn);
        if (book != null) {
            loanedBooks.remove(isbn);
            availableBooks.put(isbn, book);
            return book;
        } else {
            return null; // 반납 실패: 대출 기록이 없음
        }
    }

    public Map<String, Book> getAvailableBooks() {
        return availableBooks;
    }

    public Map<String, Book> getLoanedBooks() {
        return loanedBooks;
    }
}
