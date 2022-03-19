package common.endpoints;

import java.util.ArrayList;
import java.util.List;

public class Order {
    public static List<String> side = new ArrayList<>();

    public Order(List<String> side) {
        this.side = side;
    }

    public static List<String> getSide() {
        side.add("Buy");
        side.add("Sell");
        return side;
    }

    public static void setSide(List<String> side) {
        Order.side = side;
    }
}
