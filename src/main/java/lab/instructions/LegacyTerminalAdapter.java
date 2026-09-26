package lab.instructions;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lab.instructions.legacy.LegacyTerminal;

public final class LegacyTerminalAdapter implements InstructionOutput {
    private final LegacyTerminal terminal;

    public LegacyTerminalAdapter(LegacyTerminal terminal) { this.terminal = Objects.requireNonNull(terminal); }

    @Override public void publish(InstructionMessage message) throws DeliveryException {
        final int station;
        try {
            station = Integer.parseInt(message.stationCode());
        } catch (NumberFormatException error) {
            throw new DeliveryException(DeliveryException.Reason.INVALID_STATION, "Station code must be numeric");
        }
        final int status;
        try {
            status = terminal.sendCommand(station, message.body().getBytes(StandardCharsets.UTF_8),
                    message.priority() == Priority.URGENT ? 'U' : 'N');
        } catch (RuntimeException error) {
            throw new DeliveryException(DeliveryException.Reason.UNKNOWN, "Legacy terminal failed");
        }
        switch (status) {
            case LegacyTerminal.OK -> { return; }
            case LegacyTerminal.BAD_STATION -> throw new DeliveryException(DeliveryException.Reason.INVALID_STATION, "Station was rejected");
            case LegacyTerminal.QUEUE_FULL -> throw new DeliveryException(DeliveryException.Reason.REJECTED, "Terminal queue is full");
            case LegacyTerminal.OFFLINE -> throw new DeliveryException(DeliveryException.Reason.DEVICE_UNAVAILABLE, "Terminal is offline");
            default -> throw new DeliveryException(DeliveryException.Reason.UNKNOWN, "Terminal returned an unknown status");
        }
    }
}
