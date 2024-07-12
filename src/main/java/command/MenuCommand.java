package command;

import com.sun.tools.javac.Main;
import util.Prompt;
import vo.User;

import java.util.ArrayList;
import java.util.List;

public class MenuCommand {

    String[] menus = {"로그인", "회원 가입", "회원 탈퇴"};
    List<User> userList = new ArrayList<>();
    UserCommand userCommand = new UserCommand(userList);
    LoginCommand loginCommand = new LoginCommand(userList);

    public void execute() {
        printMenus();

        while (true) {
            String command = Prompt.input("%s>", "메인");
            if (command.equals("menu")) {
                printMenus();
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
                boolean hasUser = loginCommand.execute();
                if(hasUser) {
                    System.out.println("대출");
                }
                break;
            case "회원 등록":
                userCommand.addUser();
                break;
            case "회원 탈퇴":
                userCommand.deleteUser();
                break;
            /*case "회원 조회":
                userCommand.listUser();
                break;*/
        }
    }

    private void printMenus() {
        System.out.printf("[%s]\n", "메인");
        for (int i = 0; i < menus.length; i++) {
            System.out.printf("%d. %s\n", (i + 1), menus[i]);
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
