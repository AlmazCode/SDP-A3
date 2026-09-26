package lab.instructions;

import java.util.Objects;

public abstract class Instruction {
    private final InstructionOutput output;

    protected Instruction(InstructionOutput output) {
        this.output = Objects.requireNonNull(output);
    }

    protected final void publish(InstructionMessage message) throws DeliveryException {
        output.publish(message);
    }

    public abstract void issue(String stationCode, String text) throws DeliveryException;
}
