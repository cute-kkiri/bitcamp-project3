package command;

import vo.Book;
import vo.User;

import static util.Ansi.*;

public class BookListViewCommand {

    public void listUserBooks(User user) {
        System.out.println();
        System.out.printf("['%s'님의 대출 목록]\n", BOLD + CYAN + user.getName() + RESET);
        System.out.println("=========================================================================");
        System.out.println();
        int i = 1;
        for (Book book : user.getLoanedBooks()) {
            System.out.printf("%d. %s\n", i++, book);
            System.out.println();
        }
        System.out.println("=========================================================================");
    }
}
