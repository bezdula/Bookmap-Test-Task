package exchangetask;

public interface AdvancedExchangeInterface extends ExchangeInterface {
    public void modify(long oid, int newPrice, int newSize) throws RequestRejectedException;
}
