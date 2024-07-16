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
        String userName = Prompt.input("이름?");
        String userTel = Prompt.input("핸드폰 번호?");

        if (!userName.isEmpty() && !userTel.isEmpty()) {
            User user = new User(userName, userTel);

            int index = userList.indexOf(user);
            if (index == -1) {
                System.out.println("회원가입이 완료되었습니다.");
                user.setName(userName);
                user.setTel(userTel);
                user.setNo(User.getNextSeqNo());
                userList.add(user);
            } else {
                System.out.println("이미 존재하는 회원입니다.");
            }
            // listUser();
        } else {
            System.out.println("회원 정보를 정확하게 입력해주세요.");
        }
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

        User existingUser = userList.get(index);
        if (!existingUser.getLoanedBooks().isEmpty()) { // 대출 중인 책이 있는 경우
            System.out.println("탈퇴 실패: 대출 중인 책을 반납 후 다시 시도하세요."); // (최종수정)
            return;
        }

        String str = Prompt.input("(%s) 삭제하시겠습니까(y)?", existingUser.getName());
        if (str.equalsIgnoreCase("y")) {
            User deletedUser = userList.remove(index);
            System.out.printf("'%s' 회원을 삭제 했습니다.\n", deletedUser.getName());
        } else {
            System.out.printf("'%s' 회원을 유지합니다.\n", existingUser.getName());
        }
    }

    private String getUserTel(String userTel) {
        for (User user : userList) {
            if (user.getTel().endsWith(userTel)) {
                return user.getTel();
            }
        }
        return userTel;
    }

    public void listUser() {
        System.out.println("번호 이름 이메일");
        for (User user : userList) {
            System.out.printf("%d. %s %s\n", user.getNo(), user.getName(), user.getTel());
        }
    }

    // DummyData
    private void initializeDummyData() {
        userList.add(createUser("홍길동", "010-1234-5678"));
        userList.add(createUser("김철수", "010-9876-5432"));
        userList.add(createUser("이영희", "010-5555-5555"));
        userList.add(createUser("a", "1111"));
    }

    private User createUser(String name, String tel) {
        User user = new User();
        user.setName(name);
        user.setTel(tel);
        user.setNo(User.getNextSeqNo());
        return user;
    }
}
