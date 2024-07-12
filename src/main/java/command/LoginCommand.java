package command;

import util.Prompt;
import vo.User;

import java.util.List;

public class LoginCommand {

    private List<User> userList;

    public LoginCommand(List<User> userList) {
        this.userList = userList;
    }

    public boolean execute() {
        String userName = Prompt.input("이름?");
        String userTel = Prompt.input("핸드폰 뒷 번호?");
        String fullTel = getUserTel(userTel);

        User user = new User(userName, fullTel);
        int index = userList.indexOf(user);
        if (index == -1) {
            System.out.println("일치하는 회원 정보가 없습니다.");
            return false;
        }
        User currentUser = userList.get(index);
        System.out.printf("'%s'.\n", currentUser.getName());
        return true;
    }

    private String getUserTel(String userTel) {
        for (User user : userList) {
            if (userTel.length() == 4 && user.getTel().endsWith(userTel)) {
                return user.getTel();
            }
        }
        return userTel;
    }


}
