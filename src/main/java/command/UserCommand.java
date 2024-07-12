package command;

import util.Prompt;
import vo.User;

import java.util.List;

public class UserCommand {

    private List<User> userList;

    public UserCommand(List<User> list) {
        this.userList = list;
         initializeDummyData();
    }

    public void addUser() {
        User user = new User();
        user.setName(Prompt.input("이름?"));
        user.setTel(Prompt.input("핸드폰 번호?"));
        user.setNo(User.getNextSeqNo());
        userList.add(user);
    }

    public void deleteUser() {
        String userName = Prompt.input("이름?");
        String userTel = Prompt.input("핸드폰 뒷 번호?");
        String fullTel = getUserTel(userTel);

        User user = new User(userName, fullTel);
        int index = userList.indexOf(user);
        if (index == -1) {
            System.out.println("없는 회원입니다.");
            return;
        }

        String str = Prompt.input("(%s) 삭제하시겠습니까(y)?", user.getName());
        if (str.equalsIgnoreCase("y")) {
            User deletedUser = userList.remove(index);
            System.out.printf("'%s' 회원을 삭제 했습니다.\n", deletedUser.getName());
        } else {
            System.out.printf("'%s' 회원을 유지합니다.\n", user.getName());
        }
    }

    private String getUserTel(String userTel) {
        for (User user : userList) {
            if (userTel.length() == 4 && user.getTel().endsWith(userTel)) {
                return user.getTel();
            }
        }
        return userTel;
    }

    public void listUser() {
        System.out.println("번호 이름 이메일");
        for (User user : userList) {
            System.out.printf("%s %s\n", user.getName(), user.getTel());
        }
    }
    
    // DummyData
    private void initializeDummyData() {
        userList.add(createUser("홍길동", "010-1234-5678"));
        userList.add(createUser("김철수", "010-9876-5432"));
        userList.add(createUser("이영희", "010-5555-5555"));
    }

    private User createUser(String name, String tel) {
        User user = new User();
        user.setName(name);
        user.setTel(tel);
        user.setNo(User.getNextSeqNo());
        return user;
    }
}
