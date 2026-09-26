package lab.instructions;

public interface InstructionProvider {
    String kind();
    Instruction create(InstructionOutput output);
}
