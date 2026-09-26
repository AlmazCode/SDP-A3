package lab.instructions;

public interface OutputProvider {
    String destination();
    InstructionOutput create();
}
