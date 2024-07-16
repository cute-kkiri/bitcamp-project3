package command;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import vo.Book;
import vo.NaverBookItem;
import vo.NaverBookResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static util.Ansi.*;

public class BookSearchService {
    private static final String CLIENT_ID = "ZjQIawPDjrOWGVsJb24P";
    private static final String CLIENT_SECRET = "WFRlX98n7q";

    private OkHttpClient client;

    public BookSearchService() {
        client = new OkHttpClient();
    }

    public String searchBooks(String query) throws IOException {
        String apiUrl = "https://openapi.naver.com/v1/search/book.json?query=" + query;

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("X-Naver-Client-Id", CLIENT_ID)
                .addHeader("X-Naver-Client-Secret", CLIENT_SECRET)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }
    }

    public List<Book> saveBooksFromResponse(String responseJson, BookLoanService bookLoanService) {
        Gson gson = new Gson();
        NaverBookResponse naverResponse = gson.fromJson(responseJson, NaverBookResponse.class);

        if (naverResponse != null && naverResponse.getItems() != null) {
            if (naverResponse.getTotal() == 0) {
                System.out.println(); // 개행
                System.out.println("=========================================================================");
                System.out.println(); // 개행
                System.out.println("검색 결과가 없습니다.");
                System.out.println(); // 개행
                System.out.println("=========================================================================");
                System.out.println(); // 개행
                return null;
            }

            List<Book> books = new ArrayList<>();
            System.out.println(); // 개행
            System.out.println("=========================================================================");
            System.out.println(); // 개행
            for (NaverBookItem item : naverResponse.getItems()) {
                int index = naverResponse.getItems().indexOf(item);
                Book book = new Book(item.getTitle(), item.getAuthor(), item.getIsbn(), null, null);
                bookLoanService.addBookFromSearchResult(book);
                System.out.printf("%d. ", index + 1);
                System.out.println("책 제목: " + book.getTitle());
                System.out.println("저자: " + book.getAuthor());
                System.out.println("대출 가능 여부: " + book.getLoanAvailabilityStatus());
                System.out.println("반납일자: " + book.getReturnDate());
                System.out.println("고유번호: " + book.getIsbn());
                System.out.println(); // 개행

                books.add(book);
            }

            System.out.println("=========================================================================");
            System.out.println(); // 개행

            return books;
        } else {
            System.out.println("검색 결과가 없습니다.");
            return null;
        }

    }

    public Book searchBookByIsbn(String isbn, BookLoanService bookLoanService) { // 수정
        return bookLoanService.getAvailableBooks().get(isbn);
    }
}
