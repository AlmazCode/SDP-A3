package lab.instructions;

import java.util.Objects;

public record InstructionRequest(String kind, String destination, String stationCode, String text) {
    public InstructionRequest {
        Objects.requireNonNull(kind);
        Objects.requireNonNull(destination);
        Objects.requireNonNull(stationCode);
        Objects.requireNonNull(text);
    }
}
