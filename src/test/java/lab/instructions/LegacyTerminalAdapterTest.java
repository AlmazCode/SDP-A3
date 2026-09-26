package lab.instructions;

import static org.junit.jupiter.api.Assertions.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import lab.instructions.legacy.LegacyTerminal;
import org.junit.jupiter.api.Test;

class LegacyTerminalAdapterTest {
    private static final class StubTerminal extends LegacyTerminal {
        int status;
        int station;
        byte[] payload;
        char urgency;

        StubTerminal(int status) { super(new PrintStream(OutputStream.nullOutputStream())); this.status = status; }

        @Override public int sendCommand(int station, byte[] payload, char urgency) {
            this.station = station;
            this.payload = payload;
            this.urgency = urgency;
            return status;
        }
    }

    @Test void convertsTheMessageToNativeArguments() throws DeliveryException {
        StubTerminal terminal = new StubTerminal(LegacyTerminal.OK);
        new LegacyTerminalAdapter(terminal).publish(new InstructionMessage("14", "Evacuate", Priority.URGENT));
        assertEquals(14, terminal.station);
        assertEquals("Evacuate", new String(terminal.payload, StandardCharsets.UTF_8));
        assertEquals('U', terminal.urgency);
    }

    @Test void mapsInvalidStationStatus() {
        assertReason(LegacyTerminal.BAD_STATION, DeliveryException.Reason.INVALID_STATION);
    }

    @Test void mapsFullQueueStatus() {
        assertReason(LegacyTerminal.QUEUE_FULL, DeliveryException.Reason.REJECTED);
    }

    @Test void mapsOfflineStatus() {
        assertReason(LegacyTerminal.OFFLINE, DeliveryException.Reason.DEVICE_UNAVAILABLE);
    }

    @Test void mapsUnexpectedNativeStatus() {
        assertReason(999, DeliveryException.Reason.UNKNOWN);
    }

    @Test void mapsMalformedStationWithoutLeakingNumberFormatException() {
        StubTerminal terminal = new StubTerminal(LegacyTerminal.OK);
        DeliveryException error = assertThrows(DeliveryException.class,
                () -> new LegacyTerminalAdapter(terminal).publish(new InstructionMessage("east-wing", "Check", Priority.NORMAL)));
        assertEquals(DeliveryException.Reason.INVALID_STATION, error.reason());
        assertNull(error.getCause());
    }

    private static void assertReason(int status, DeliveryException.Reason expected) {
        DeliveryException error = assertThrows(DeliveryException.class,
                () -> new LegacyTerminalAdapter(new StubTerminal(status))
                        .publish(new InstructionMessage("14", "Check", Priority.NORMAL)));
        assertEquals(expected, error.reason());
        assertNull(error.getCause());
    }
}
