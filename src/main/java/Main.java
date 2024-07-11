import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BookSearchService bookSearchService = new BookSearchService();
        BookLoanService bookLoanService = new BookLoanService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("============무인 도서 대출/반납 서비스============");
            System.out.println("1. 책 검색");
            System.out.println("2. 책 대출");
            System.out.println("3. 책 반납");
            System.out.println("4. 대출 중인 책 목록");
            System.out.println("5. 보유 중인 책 목록");
            System.out.println("6. 종료");
            System.out.print("메뉴를 선택하세요: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 처리

            switch (choice) {
                case 1:
                    System.out.print("검색어를 입력하세요: ");
                    String query = scanner.nextLine();
                    try {
                        String response = bookSearchService.searchBooks(query);
                        // 네이버 API에서 가져온 데이터를 Book 객체로 변환 및 저장
                        bookSearchService.saveBooksFromResponse(response, bookLoanService);
                        System.out.println("책 검색 완료");
                    } catch (IOException e) {
                        System.err.println("책 검색 중 오류 발생: " + e.getMessage());
                    }
                    break;
                case 2:
                    // 대출 기능 구현 부분
                    System.out.print("대출할 책의 ISBN을 입력하세요: ");
                    String loanIsbn = scanner.nextLine();
                    Book loanedBook = bookLoanService.loanBook(loanIsbn);
                    if (loanedBook != null) {
                        System.out.println(loanedBook.getTitle() + " 책이 대출되었습니다.");
                    } else {
                        System.out.println("책 대출 실패: 해당 책을 찾을 수 없거나 이미 대출 중입니다.");
                    }
                    break;
                case 3:
                    // 반납 기능 구현 부분
                    System.out.print("반납할 책의 ISBN을 입력하세요: ");
                    String returnIsbn = scanner.nextLine();
                    Book returnedBook = bookLoanService.returnBook(returnIsbn);
                    if (returnedBook != null) {
                        System.out.println(returnedBook.getTitle() + " 책이 반납되었습니다.");
                    } else {
                        System.out.println("책 반납 실패: 해당 책의 대출 기록이 없습니다.");
                    }
                    break;
                case 4:
                    // 대출 중인 책 목록 출력
                    System.out.println("대출 중인 책 목록:");
                    for (Book book : bookLoanService.getLoanedBooks().values()) {
                        System.out.println(book);
                    }
                    break;
                case 5:
                    // 보유 중인 책 목록 출력
                    System.out.println("보유 중인 책 목록:");
                    for (Book book : bookLoanService.getAvailableBooks().values()) {
                        System.out.println(book);
                    }
                    break;
                case 6:
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
