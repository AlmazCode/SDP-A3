package lab.instructions.providers;

import lab.instructions.*;

public final class PanelProvider implements OutputProvider {
    @Override public String destination() { return "panel"; }
    @Override public InstructionOutput create() { return new PanelOutput(System.out); }
}
