package vo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static util.Ansi.*;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private boolean loanAvailable;
    private String returnDate;

    public Book(String title, String author, String isbn, String dummy1, String dummy2) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.loanAvailable = generateRandomLoanAvailability();
        this.returnDate = this.loanAvailable ? "-" : generateRandomReturnDate(); // 수정
    }

    // 대출 가능 여부를 랜덤하게 설정하는 메서드
    private boolean generateRandomLoanAvailability() {
        Random random = new Random();
        double probabilityOfTrue = 0.3;
        return random.nextDouble() < probabilityOfTrue;
    }

    // 랜덤한 반납일자를 생성하는 메서드
    private String generateRandomReturnDate() {
        LocalDate today = LocalDate.now();
        LocalDate twoWeeksLater = today.plusWeeks(2);
        long minDay = today.toEpochDay();
        long maxDay = twoWeeksLater.toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay + 1);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        return randomDate.format(formatter);
    }

    // 대출 가능 여부를 "대출 가능" 또는 "대출 불가"로 반환
    public String getLoanAvailabilityStatus() {
        return loanAvailable ? GREEN + "대출 가능" + RESET : RED + "대출 불가" + RESET; // 수정
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isLoanAvailable() {
        return loanAvailable;
    }

    public void setLoanAvailable(boolean loanAvailable) {
        this.loanAvailable = loanAvailable;
        this.returnDate = loanAvailable ? "-" : generateRandomReturnDate(); // 수정
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "제목: " + title + '\n' +
                "저자: " + author + '\n' +
                "고유번호: " + isbn + '\n' +
                "반납일자: " + getReturnDate();
//                "대출 가능여부: " + getLoanAvailabilityStatus() + '\n' +
    }
}
