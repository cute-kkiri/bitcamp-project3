package command;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import vo.Book;
import vo.NaverBookItem;
import vo.NaverBookResponse;

import java.io.IOException;

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

    // 네이버 API에서 검색한 책 정보를 vo.Book 객체로 변환하여 저장
    public int saveBooksFromResponse(String responseJson, BookLoanService bookLoanService) {
        Gson gson = new Gson();
        NaverBookResponse naverResponse = gson.fromJson(responseJson, NaverBookResponse.class);

        if (naverResponse != null && naverResponse.getItems() != null) {
//            System.out.println("Total Results: " + naverResponse.getTotal());


            if (naverResponse.getTotal() == 0) {
                System.out.println(); // 개행
                System.out.println("=========================================================================");
                System.out.println(); // 개행
                System.out.println("검색 결과가 없습니다.");
                System.out.println(); // 개행
                System.out.println("=========================================================================");
                System.out.println(); // 개행
                return 0;
            }

            System.out.println(); // 개행
            System.out.println("=========================================================================");
            for (NaverBookItem item : naverResponse.getItems()) {
                int index = naverResponse.getItems().indexOf(item);
                Book book = new Book(item.getTitle(), item.getAuthor(), item.getIsbn(), null, null);
                // BookLoanService에 책 추가
                bookLoanService.addBookFromSearchResult(book);
                // 책 정보 출력
                System.out.printf("%d. ", index + 1);
                System.out.println("책 제목: " + book.getTitle());
                System.out.println("저자: " + book.getAuthor());
                System.out.println("대출 가능 여부: " + book.getLoanAvailabilityStatus()); // 변경된 부분
                System.out.println("반납일자: " + book.getReturnDate()); // 변경된 부분
                System.out.println("ISBN: " + book.getIsbn());
                System.out.println(); // 개행
            }


            System.out.println("=========================================================================");
            System.out.println(); // 개행
        }
        return 1;
    }
}
