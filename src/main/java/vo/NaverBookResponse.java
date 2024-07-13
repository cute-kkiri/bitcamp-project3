package vo;

import java.util.List;

public class NaverBookResponse {
    private int total;
    private int start;
    private int display;
    private List<NaverBookItem> items;

    public int getTotal() {
        return total;
    }

    public int getStart() {
        return start;
    }

    public int getDisplay() {
        return display;
    }

    public List<NaverBookItem> getItems() {
        return items;
    }

    public void setItems(List<NaverBookItem> items) {
        this.items = items;
    }
}
