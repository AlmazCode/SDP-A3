package lab.instructions;

import java.util.Objects;

public record InstructionMessage(String stationCode, String body, Priority priority) {
    public InstructionMessage {
        Objects.requireNonNull(stationCode);
        Objects.requireNonNull(body);
        Objects.requireNonNull(priority);
    }
}
