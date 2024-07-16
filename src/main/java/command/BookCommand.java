package command;

import util.Prompt;
import vo.Book;
import vo.User;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static util.Ansi.*;

public class BookCommand {

    String[] menus = {"도서 대출", "도서 반납", "대출 연장", "대출 목록"}; // 수정
    List<Book> bookList;
    BookSearchService bookSearchService = new BookSearchService();
    BookLoanService bookLoanService = new BookLoanService();
    BookLoanExtendCommand bookLoanExtendCommand = new BookLoanExtendCommand(bookLoanService); // 추가
    BookListViewCommand bookListViewCommand = new BookListViewCommand(); // 추가
    BookReturnCommand bookReturnCommand = new BookReturnCommand(bookLoanService);
    Scanner scanner = new Scanner(System.in);

    public BookCommand(List<Book> bookList) {
        this.bookList = bookList;
    }

    public void execute(User user) {
        System.out.println();

        while (true) {
            printMenus();
            String command = Prompt.input("> ");
            if (command.equals("menu")) {
                continue;
            } else if (command.equals("0")) { // 이전 메뉴 선택
                return;
            }

            try {
                int menuNo = Integer.parseInt(command);
                String menuName = getMenuTitle(menuNo);
                if (menuName == null) {
                    System.out.println("유효한 메뉴 번호가 아닙니다.");
                    continue;
                }

                processMenu(user, menuName);

            } catch (NumberFormatException ex) {
                System.out.println("숫자로 메뉴 번호를 입력하세요.");
            }
        }
    }

    protected void processMenu(User user, String menuName) {
        switch (menuName) {
            case "도서 대출":
                loanBook(user);
                break;
            case "도서 반납":
                bookReturnCommand.returnBook(user);
                break;
            case "대출 연장":
                bookLoanExtendCommand.extendLoan(user); // 수정
                break;
            case "대출 목록": // 수정
                bookListViewCommand.listUserBooks(user); // 수정
                break;
        }
        System.out.println(); // 개행
    }

    private void loanBook(User user) {
        System.out.println("1. 도서 제목 검색");
        System.out.println("2. 고유번호 검색");
        String searchOption = Prompt.input("%s> ", "선택하세요");

        try {
            String response;
            if ("1".equals(searchOption)) {
                response = bookSearchService.searchBooks(Prompt.input("%s> ", "검색어를 입력하세요"));
            } else if ("2".equals(searchOption)) {
                String isbn = Prompt.input("%s > ", "ISBN 번호를 입력하세요");
                Book book = bookSearchService.searchBookByIsbn(isbn, bookLoanService); // 수정
                if (book != null) {
                    System.out.println("책 제목: " + book.getTitle());
                    System.out.println("저자: " + book.getAuthor());
                    System.out.println("대출 가능 여부: " + book.getLoanAvailabilityStatus());
                    System.out.println("반납일자: " + book.getReturnDate());
                    System.out.println("ISBN: " + book.getIsbn());
                    System.out.println(); // 개행

                    String loanOption = Prompt.input("%s> ", "대출하시겠습니까? (y/n)");
                    if ("y".equalsIgnoreCase(loanOption)) {
                        Book loanedBook = bookLoanService.loanBook(user, book.getIsbn());
                        if (loanedBook != null) {
                            System.out.println(GREEN + loanedBook.getTitle() + " 책이 대출되었습니다." + RESET);
                            System.out.println("반납일자: " + loanedBook.getReturnDate()); // 수정
                        } else {
                            System.out.println(ORANGE + "책 대출 실패: 해당 책을 찾을 수 없거나 이미 대출 중입니다." + RESET);
                        }
                    }
                } else {
                    System.out.println("책을 찾을 수 없습니다.");
                }
                return;
            } else {
                System.out.println("유효하지 않은 선택입니다.");
                return;
            }

            // 네이버 API에서 가져온 데이터를 vo.Book 객체로 변환 및 저장
            List<Book> bookSearchList = bookSearchService.saveBooksFromResponse(response, bookLoanService);

            if (bookSearchList == null) {
                return;
            } else {
                // 대출 기능 구현 부분
                String input = Prompt.input("%s> ", "대출할 책의 목록번호 또는 고유번호를 입력(이전: 0)");
                Book bookToLoan = null;

                try {
                    // 입력이 인덱스인 경우
                    int index = Integer.parseInt(input) - 1;
                    if (index == -1) {
                        return;
                    }
                    if (index >= 0 && index < bookSearchList.size()) {
                        bookToLoan = bookSearchList.get(index);

                        if (bookToLoan.isLoanAvailable()) {
                            Book loanedBook = bookLoanService.loanBook(user, bookToLoan.getIsbn());
                            System.out.println(GREEN + loanedBook.getTitle() + " 책이 대출되었습니다." + RESET);
                            System.out.println("반납일자: " + loanedBook.getReturnDate()); // 수정
                        } else {
                            System.out.println(ORANGE + "책 대출 실패: 해당 책을 찾을 수 없거나 이미 대출 중입니다." + RESET);
                        }
                    }
                } catch (NumberFormatException e) {
                    // 입력이 ISBN인 경우
                    for (Book book : bookSearchList) {
                        if (book.getIsbn().equals(input)) {
                            bookToLoan = book;

                            if (bookToLoan.isLoanAvailable()) {
                                Book loanedBook = bookLoanService.loanBook(user, bookToLoan.getIsbn());
                                System.out.println(GREEN + loanedBook.getTitle() + " 책이 대출되었습니다." + RESET);
                                System.out.println("반납일자: " + loanedBook.getReturnDate()); // 수정
                            } else {
                                System.out.println(ORANGE + "책 대출 실패: 해당 책을 찾을 수 없거나 이미 대출 중입니다." + RESET);
                            }
                            break;
                        }
                    }
                }


//                Book loanedBook = bookLoanService.loanBook(user, Prompt.input("%s> ", "대출할 책의 ISBN을 입력하세요"));
                /*if (bookToLoan != null) {
                    Book loanedBook = bookLoanService.loanBook(user, bookToLoan.getIsbn());
                    System.out.println(loanedBook.getTitle() + " 책이 대출되었습니다.");
                    System.out.println("반납일자: " + loanedBook.getReturnDate()); // 수정
                } else {
                    System.out.println(ORANGE + "책 대출 실패: 해당 책을 찾을 수 없거나 이미 대출 중입니다." + RESET);
                }*/
            }
        } catch (IOException e) {
            System.err.println("책 검색 중 오류 발생");
        }
    }

    /*private void returnBook(User user) {
        Book returnedBook = bookLoanService.returnBook(Prompt.input("%s> ", "반납할 책의 ISBN을 입력하세요"));
        if (returnedBook != null) {
            user.returnBook(returnedBook);
            System.out.println(GREEN + returnedBook.getTitle() + " 책이 반납되었습니다." + RESET);
        } else {
            System.out.println(ORANGE + "책 반납 실패: 해당 책의 대출 기록이 없습니다." + RESET);
        }
    }*/

    /*private void printLoanedBooks(User user) { // 수정

        System.out.println("대출 중인 책 목록:");
        bookListViewCommand.listUserBooks(user); // 수정

        int index = 0;
        for (Book book : user.getLoanedBooks()) {
            System.out.printf("%d. %s\n", ++index, book);
        }
    }*/

    private void printMenus() {
        System.out.println();
        System.out.println("============ 무인 도서 대출/반납 서비스 ============");
        System.out.println();
        for (int i = 0; i < menus.length; i++) {
            System.out.printf("%d. %s\t", (i + 1), menus[i]);
        }
        System.out.println("0. 이전");
    }

    private String getMenuTitle(int menuNo) {
        return isValidateMenu(menuNo) ? menus[menuNo - 1] : null;
    }

    private boolean isValidateMenu(int menuNo) {
        return menuNo >= 1 && menuNo <= menus.length;
    }
}
