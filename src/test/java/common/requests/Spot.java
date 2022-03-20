package common.requests;

public class Spot {
    public static String QUERY_SYMBOL = "/spot/v1/symbols";
    public static String ORDER_BOOK = "/spot/quote/v1/depth";
    public static String MERGED_ORDER_BOOK = "/spot/quote/v1/depth/merged";
    public static String PUBLIC_TRADE_RECORDS = "/spot/quote/v1/trades";
    public static String LATEST_INFORMATION_FOR_SYMBOL = "/spot/quote/v1/ticker/24hr";
    public static String LATEST_TRADED_PRICE = "/spot/quote/v1/ticker/price";
    public static String BEST_BID_ASK_PRICE = "/spot/quote/v1/ticker/book_ticker";
}
