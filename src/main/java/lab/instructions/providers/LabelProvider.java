package lab.instructions.providers;

import lab.instructions.*;

public final class LabelProvider implements OutputProvider {
    @Override public String destination() { return "label"; }
    @Override public InstructionOutput create() { return new LabelOutput(System.out); }
}
