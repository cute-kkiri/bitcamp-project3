package command;

import util.Prompt;
import vo.Book;
import vo.User;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class BookCommand {
    List<Book> bookList;
    BookSearchService bookSearchService = new BookSearchService();
    BookLoanService bookLoanService = new BookLoanService();
    Scanner scanner = new Scanner(System.in);

    public BookCommand(List<Book> bookList) {
        this.bookList = bookList;
    }

    public void execute(User user) {
        while (true) {
            System.out.println("============무인 도서 대출/반납 서비스============");
            System.out.println("1. 도서 대출");
            System.out.println("2. 도서 반납");
            System.out.println("3. 대출 연장");
            System.out.println("4. 회원 도서 조회");
            System.out.println("5. 종료");
            System.out.print("메뉴를 선택하세요: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 처리

            switch (choice) {

                case 1:
                    try {
                        String response = bookSearchService.searchBooks(Prompt.input("%s> ", "검색어를 입력하세요:"));
                        // 네이버 API에서 가져온 데이터를 vo.Book 객체로 변환 및 저장
                        bookSearchService.saveBooksFromResponse(response, bookLoanService);
                        System.out.println("책 검색 완료");
                    } catch (IOException e) {
                        System.err.println("책 검색 중 오류 발생: " + e.getMessage());
                    }

                    // 대출 기능 구현 부분
                    Book loanedBook = bookLoanService.loanBook(user, Prompt.input("%s> ", "대출할 책의 ISBN을 입력하세요:"));
                    if (loanedBook != null) {
                        System.out.println(loanedBook.getTitle() + " 책이 대출되었습니다.");
                        System.out.println(user.getName());
                    } else {
                        System.out.println("책 대출 실패: 해당 책을 찾을 수 없거나 이미 대출 중입니다.");
                    }
                    break;
                case 2:
                    // 반납 기능 구현 부분
                    Book returnedBook = bookLoanService.returnBook(Prompt.input("%s> ", "반납할 책의 ISBN을 입력하세요:"));
                    if (returnedBook != null) {
                        System.out.println(returnedBook.getTitle() + " 책이 반납되었습니다.");
                    } else {
                        System.out.println("책 반납 실패: 해당 책의 대출 기록이 없습니다.");
                    }
                    break;
                case 3:
                    // 대출 중인 책 목록 출력
                    System.out.println("대출 중인 책 목록:");
                    for (Book book : bookLoanService.getLoanedBooks().values()) {
                        System.out.println(book);
                    }
                    break;
                case 4:
                    // 보유 중인 책 목록 출력
                    System.out.println("보유 중인 책 목록:");
                    for (Book book : bookLoanService.getAvailableBooks().values()) {
                        System.out.println(book);
                    }
                    break;
                case 5:
                    // 프로그램 종료
                    System.out.println("프로그램을 종료합니다.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("잘못된 선택입니다. 다시 시도하세요.");
                    break;
            }
            System.out.println(); // 개행
        }
    }

}
