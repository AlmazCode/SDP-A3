package lab.instructions.providers;

import lab.instructions.*;

public final class RoutineProvider implements InstructionProvider {
    @Override public String kind() { return "routine"; }
    @Override public Instruction create(InstructionOutput output) { return new RoutineInstruction(output); }
}
