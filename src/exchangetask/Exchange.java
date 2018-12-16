package exchangetask;

import java.util.LinkedList;
import java.util.ListIterator;

public class Exchange implements AdvancedExchangeInterface, QueryInterface {

    private LinkedList<Order> buyOrders = new LinkedList<>();
    private LinkedList<Order> sellOrders = new LinkedList<>();

    @Override
    public void send(long orderId, boolean isBuy, int price, int size) {

        if ((size <= 0) || ( price <= 0)){
            return;
        }

        int newSize = size;

        if (isBuy) {
            if (!sellOrders.isEmpty()) {
                while (sellOrders.get(0).getPrice() <= price) {
                    int buyMinusSellSize = newSize - sellOrders.get(0).getSize();
                    if (buyMinusSellSize < 0) {
                        sellOrders.get(0).setSize(-buyMinusSellSize);
                        return;
                    } else if (buyMinusSellSize == 0) {
                        sellOrders.remove(0);
                        return;
                    } else {
                        sellOrders.remove(0);
                        newSize = buyMinusSellSize;
                    }
                    if (sellOrders.isEmpty()) {
                        break;
                    }
                }
            }
            insertBuy(orderId, price, newSize);
        } else {
            if (!buyOrders.isEmpty()) {
                while (buyOrders.get(0).getPrice() >= price) {
                    int sellMinusByuSize = newSize - buyOrders.get(0).getSize();
                    if (sellMinusByuSize < 0) {
                        buyOrders.get(0).setSize(-sellMinusByuSize);
                        return;
                    } else if (sellMinusByuSize == 0) {
                        buyOrders.remove(0);
                        return;
                    } else {
                        buyOrders.remove(0);
                        newSize = sellMinusByuSize;
                    }
                    if (buyOrders.isEmpty()) {
                        break;
                    }
                }
            }
            insertSell(orderId, price, newSize);
        }
    }

    private void insertBuy(long orderId, int price, int size) {
        ListIterator<Order> iterator = buyOrders.listIterator();
        while (iterator.hasNext()) {
            if (price >= iterator.next().getPrice()) {
                iterator.previous();
                break;
            }
        }
        iterator.add(new Order(orderId, price, size));
    }

    private void insertSell(long orderId, int price, int size) {
        ListIterator<Order> iterator = sellOrders.listIterator();
        while (iterator.hasNext()) {
            if (price <= iterator.next().getPrice()) {
                iterator.previous();
                break;
            }
        }
        iterator.add(new Order(orderId, price, size));
    }

    @Override
    public void cancel(long orderId) throws RequestRejectedException {
        int index;
        index = buyOrders.indexOf(new Order(orderId, 0, 0));
        if (index != -1) {
            buyOrders.remove(index);
            return;
        }
        index = sellOrders.indexOf(new Order(orderId, 0, 0));
        if (index != -1) {
            sellOrders.remove(index);
            return;
        }
        throw new RequestRejectedException("No such resting order");
    }

    @Override
    public void modify(long oid, int newPrice, int newSize) throws RequestRejectedException {
        int index;
        index = buyOrders.indexOf(new Order(oid, 0, 0));
        if (index != -1) {
            buyOrders.remove(index);
            send(oid, true, newPrice, newSize);
            return;
        }
        index = sellOrders.indexOf(new Order(oid, 0, 0));
        if (index != -1) {
            sellOrders.remove(index);
            send(oid, false, newPrice, newSize);
            return;
        }
        throw new RequestRejectedException("No such resting order");
    }

    @Override
    public int getTotalSizeAtPrice(int price) {
        int totalSizeAtPrice = 0;
        ListIterator<Order> iterator;
        iterator = buyOrders.listIterator();

        while (iterator.hasNext()) {
            int nextPrice = iterator.next().getPrice();
            if (price == nextPrice) {
                iterator.previous();
                totalSizeAtPrice = totalSizeAtPrice + iterator.next().getSize();
            } else if (price > nextPrice) {
                break;
            }
        }
        iterator = sellOrders.listIterator();
        while (iterator.hasNext()) {
            int nextPrice = iterator.next().getPrice();
            if (price == nextPrice) {
                iterator.previous();
                totalSizeAtPrice = totalSizeAtPrice + iterator.next().getSize();
            }else if (price < nextPrice) {
                break;
            }
        }
        return totalSizeAtPrice;
    }

    @Override
    public int getHighestBuyPrice() {
        if (!buyOrders.isEmpty()){
            return buyOrders.getFirst().getPrice();
        } else {
            return -1;
        }
    }

    @Override
    public int getLowestSellPrice() {
        if (!sellOrders.isEmpty()){
            return sellOrders.getFirst().getPrice();
        } else {
            return -1;
        }
    }
}
