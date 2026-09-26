package lab.instructions;

import java.io.PrintStream;
import java.util.Objects;

public final class PanelOutput implements InstructionOutput {
    private final PrintStream stream;

    public PanelOutput(PrintStream stream) { this.stream = Objects.requireNonNull(stream); }

    @Override public void publish(InstructionMessage message) throws DeliveryException {
        stream.println("[PANEL] station=" + message.stationCode() + " priority=" + message.priority() + ": " + message.body());
        if (stream.checkError()) throw new DeliveryException(DeliveryException.Reason.DEVICE_UNAVAILABLE, "Panel output failed");
    }
}
