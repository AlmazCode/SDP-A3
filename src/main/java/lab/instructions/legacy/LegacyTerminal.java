package lab.instructions.legacy;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/** Simulated vendor API. It deliberately knows nothing about InstructionOutput. */
public class LegacyTerminal {
    public static final int OK = 0;
    public static final int BAD_STATION = 10;
    public static final int QUEUE_FULL = 20;
    public static final int OFFLINE = 30;

    private final PrintStream stream;

    public LegacyTerminal(PrintStream stream) { this.stream = Objects.requireNonNull(stream); }

    public int sendCommand(int station, byte[] payload, char urgency) {
        if (station <= 0) return BAD_STATION;
        if (payload.length > 120) return QUEUE_FULL;
        stream.println("[TERMINAL " + station + "] " + urgency + " | " + new String(payload, StandardCharsets.UTF_8));
        return stream.checkError() ? OFFLINE : OK;
    }
}
