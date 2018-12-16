package exchangetask;

// This class is used by the Exchange class to create the order instances
class Order {
    private long orderId;
    private int price;
    private int size;

    Order(long orderId, int price, int size) {
        this.orderId = orderId;
        this.price = price;
        this.size = size;
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof Order)) return false;
        return this.getOrderId() == ((Order)o).getOrderId();
    }

    @Override
    public int hashCode(){
        return (int) this.getOrderId();
    }

    public long getOrderId() {
        return orderId;
    }

    public int getPrice() {
        return price;
    }

    public int getSize() {
        return size;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setSize(int size) {
        this.size = size;
    }
}