package command;

import util.Prompt;
import vo.Book;
import vo.User;

import java.util.ArrayList;
import java.util.List;

public class MenuCommand {

    String[] menus = {"로그인", "회원 가입"};
    List<User> userList = new ArrayList<>();
    List<Book> bookList = new ArrayList<>();
    UserCommand userCommand = new UserCommand(userList);
    LoginCommand loginCommand = new LoginCommand(userList);
    BookCommand bookCommand = new BookCommand(bookList);

    public void execute() {
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

                processMenu(menuName);

            } catch (NumberFormatException ex) {
                System.out.println("숫자로 메뉴 번호를 입력하세요.");
            }
        }
    }

    protected void processMenu(String menuName) {
        switch (menuName) {
            case "로그인":
                User hasUser = loginCommand.execute();
                if (hasUser != null) {
                    bookCommand.execute(hasUser);
                }
                break;
            case "회원 가입":
                userCommand.addUser();
                break;
        }
    }

    private void printMenus() {
        System.out.println();
        System.out.println("============무인 도서 대출/반납 서비스============");
        System.out.println();
        for (int i = 0; i < menus.length; i++) {
            System.out.printf("%d. %s\t", (i + 1), menus[i]);
        }
        System.out.println("0. 종료");
    }

    private String getMenuTitle(int menuNo) {
        return isValidateMenu(menuNo) ? menus[menuNo - 1] : null;
    }

    private boolean isValidateMenu(int menuNo) {
        return menuNo >= 1 && menuNo <= menus.length;
    }
}
