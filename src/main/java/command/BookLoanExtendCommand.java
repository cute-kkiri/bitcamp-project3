package command;

import util.Prompt;
import vo.Book;
import vo.User;

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

        String isbn = Prompt.input("%s> ", "연장할 책의 ISBN을 입력하세요");
        Book book = bookLoanService.getLoanedBooks().get(isbn);
        if (book != null) {
            bookLoanService.extendReturnDate(book);
            System.out.println(book.getTitle() + " 책의 반납일자가 연장되었습니다.");
            System.out.println("새 반납일자: " + book.getReturnDate());
        } else {
            System.out.println("연장 실패: 해당 책의 대출 기록이 없습니다.");
        }
    }
}
