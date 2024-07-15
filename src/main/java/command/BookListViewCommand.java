package command;

import vo.Book;
import vo.User;

public class BookListViewCommand {

    public void listUserBooks(User user) {
        System.out.println("회원님의 대출 목록:");
        for (Book book : user.getLoanedBooks()) {
            System.out.println(book);
        }
    }
}
