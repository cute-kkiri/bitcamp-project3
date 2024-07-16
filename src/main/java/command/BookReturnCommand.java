package command;

import util.Prompt;
import vo.Book;
import vo.User;

import java.util.List;

import static util.Ansi.*;

public class BookReturnCommand {
    private BookLoanService bookLoanService;
    BookListViewCommand bookListViewCommand = new BookListViewCommand();

    public BookReturnCommand(BookLoanService bookLoanService) {
        this.bookLoanService = bookLoanService;
    }

    public void returnBook(User user) {
        bookListViewCommand.listUserBooks(user);

        List<Book> books = user.getLoanedBooks();
        Book returnedBook = null;
        String input = Prompt.input("%s> ", "반납할 책의 목록 번호 또는 고유번호를 입력하세요(이전: 0)");

        try {
            int index = Integer.parseInt(input) - 1;

            if (index == -1) {
                return;
            }

            if (index >= 0 && index < books.size()) {
                returnedBook = books.get(index);
            }
        } catch (NumberFormatException e) {
            // 입력이 ISBM인 경우
            for (Book book : books) {
                if (book.getIsbn().equals(input)) {
                    returnedBook = book;
                    break;
                }
            }
        }
//        Book returnedBook = bookLoanService.returnBook(input);
            if (returnedBook != null) {
            user.returnBook(returnedBook);
            System.out.println(GREEN + returnedBook.getTitle() + " 책이 반납되었습니다." + RESET);
        } else {
            System.out.println(ORANGE + "책 반납 실패: 해당 책의 대출 기록이 없습니다." + RESET);
        }
    }

}
