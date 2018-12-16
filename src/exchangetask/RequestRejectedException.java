package exchangetask;

public class RequestRejectedException extends Exception {
    public RequestRejectedException(){}
    public RequestRejectedException(String message){
        super(message);
    }
}
