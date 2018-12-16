package exchangetester;

import exchangetask.Exchange;
import exchangetask.RequestRejectedException;

public class ExchangeTaskTester {
    public static void main(String[] args) {

        Exchange exchange = new Exchange();

        for (int j = 0; j < 100; j++) {
            long orderId = (long) j;
            boolean isBuy = (((int) (Math.random() * 10) > 4) ? true : false);
            int price = (int) (Math.random() * 100);
            int size = (int) (Math.random() * 100);

            System.out.println("Send: " + (isBuy ? "Buy, " : "Sell, ") + "Price " + price + ", Size " + size);
            exchange.send(orderId, isBuy, price, size);
            System.out.println("Size at price at 7: " + exchange.getTotalSizeAtPrice(7));
            System.out.println("Lowest sell price:  " + exchange.getLowestSellPrice());
            System.out.println("Highest buy price:  " + exchange.getHighestBuyPrice() + "\n");
        }


        for (int j = 0; j < 10; j++) {
            long orderId = (long) j + 90;
            int price = (int) (Math.random() * 100);
            int size = (int) (Math.random() * 100);

            System.out.println("Modify: ID: " + orderId + ", Price " + price + ", Size " + size);
            try {
                exchange.modify(orderId, price, size);
            } catch (RequestRejectedException e) {
                System.out.println("Error");
            }

            System.out.println("Size at price at 7: " + exchange.getTotalSizeAtPrice(7));
            System.out.println("Lowest sell price:  " + exchange.getLowestSellPrice());
            System.out.println("Highest buy price:  " + exchange.getHighestBuyPrice() + "\n");
        }
    }
}

