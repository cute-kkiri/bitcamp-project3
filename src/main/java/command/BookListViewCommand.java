package command;

import vo.Book;
import vo.User;

public class BookListViewCommand {

    public void listUserBooks(User user) {
        System.out.println();
        System.out.printf("============================'%s'님의 대출 목록==========================", user.getName());
        System.out.println();
        int i = 1;
        for (Book book : user.getLoanedBooks()) {
            System.out.printf("%d. %s\n", i++, book);
            System.out.println();
        }
        System.out.println("=========================================================================");
    }
}
