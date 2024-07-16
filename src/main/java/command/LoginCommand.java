package command;

import util.Prompt;
import vo.User;

import java.util.List;

import static util.Ansi.*;

public class LoginCommand {

    private List<User> userList;

    public LoginCommand(List<User> userList) {
        this.userList = userList;
    }

    public User execute() {
        String userName = Prompt.input("이름?");
        String userTel = Prompt.input("핸드폰 뒷 번호?");
        String fullTel = getUserTel(userTel);

        User user = new User(userName, fullTel);
        int index = userList.indexOf(user);
        if (index == -1) {
            System.out.println("일치하는 회원 정보가 없습니다.");
            return null;
        }
        User currentUser = userList.get(index);
        System.out.printf("'%s'님 환영합니다.\n", CYAN + currentUser.getName() + RESET);
        return currentUser;
    }

    private String getUserTel(String userTel) {
        for (User user : userList) {
//            if (userTel.length() == 4 && user.getTel().endsWith(userTel)) {
            if (user.getTel().endsWith(userTel)) {
                return user.getTel();
            }
        }
        return userTel;
    }


}
