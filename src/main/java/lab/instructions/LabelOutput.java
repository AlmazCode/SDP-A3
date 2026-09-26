package lab.instructions;

import java.io.PrintStream;
import java.util.Objects;

public final class LabelOutput implements InstructionOutput {
    private final PrintStream stream;

    public LabelOutput(PrintStream stream) { this.stream = Objects.requireNonNull(stream); }

    @Override public void publish(InstructionMessage message) throws DeliveryException {
        stream.println("[LABEL " + message.stationCode() + "] " + message.priority() + " | " + message.body());
        if (stream.checkError()) throw new DeliveryException(DeliveryException.Reason.DEVICE_UNAVAILABLE, "Label output failed");
    }
}
