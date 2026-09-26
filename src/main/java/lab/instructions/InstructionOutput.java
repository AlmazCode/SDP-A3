package lab.instructions;

public interface InstructionOutput {
    void publish(InstructionMessage message) throws DeliveryException;
}
