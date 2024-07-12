package vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// 메모리 설계도
public class User {

    private static int seqNo;

    private int no;
    private String name;
    private String tel;
    private List<Book> loanedBooks = new ArrayList<>();

    public User() {
    }

    public User(int no) {
        this.no = no;
    }

    public User(String name, String tel) {
        this.name = name;
        this.tel = tel;
    }

    public static int getNextSeqNo() {
        return ++seqNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(tel, user.tel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tel);
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public List<Book> getLoanedBooks() {
        return loanedBooks;
    }

    public void loanBook(Book book) {
        loanedBooks.add(book);
    }

    public void returnBook(Book book) {
        loanedBooks.remove(book);
    }
}
