package lab.instructions;

public class DeliveryException extends Exception {
    public enum Reason { INVALID_STATION, DEVICE_UNAVAILABLE, REJECTED, UNKNOWN }

    private final Reason reason;

    public DeliveryException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    public Reason reason() { return reason; }
}
