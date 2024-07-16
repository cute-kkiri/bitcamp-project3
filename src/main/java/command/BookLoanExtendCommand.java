package command;

import util.Prompt;
import vo.Book;
import vo.User;

import java.text.NumberFormat;
import java.util.List;

import static util.Ansi.*;

public class BookLoanExtendCommand {

    private BookLoanService bookLoanService;
    BookListViewCommand bookListViewCommand = new BookListViewCommand();

    public BookLoanExtendCommand(BookLoanService bookLoanService) {
        this.bookLoanService = bookLoanService;
    }

    public void extendLoan(User user) {
        /*System.out.println("대출 중인 책 목록:");
        for (Book book : user.getLoanedBooks()) {
            System.out.println(book);
        }*/

        bookListViewCommand.listUserBooks(user);

        List<Book> books = user.getLoanedBooks();
        Book bookToLoan = null;
        String input = Prompt.input("%s> ", "연장할 책의 목록번호 또는 고유번호를 입력하세요(이전: 0)");

        try {
            int index = Integer.parseInt(input) -1 ;

            if (index == -1) {
                return;
            }

            if (index >= 0 && index < books.size()) {
                bookToLoan = books.get(index);
            }
        } catch (NumberFormatException e) {
            // 입력이 ISBN인 경우
            for (Book book : books) {
                if (book.getIsbn().equals(input)) {
                    bookToLoan = book;
                    break;
                }
            }
        }

        if (bookToLoan != null) {
            bookLoanService.extendReturnDate(bookToLoan);
            System.out.println(GREEN + bookToLoan.getTitle() + " 책의 반납일자가 연장되었습니다." + RESET);
            System.out.printf("새 반납일자: %s\n", CYAN + bookToLoan.getReturnDate() + RESET);
        } else {
            System.out.println(ORANGE + "연장 실패: 해당 책의 대출 기록이 없습니다." + RESET);
        }



//        Book book = bookLoanService.getLoanedBooks().get(isbn);

    }
}
