package lab.instructions.providers;

import lab.instructions.*;

public final class EmergencyProvider implements InstructionProvider {
    @Override public String kind() { return "emergency"; }
    @Override public Instruction create(InstructionOutput output) { return new EmergencyInstruction(output); }
}
