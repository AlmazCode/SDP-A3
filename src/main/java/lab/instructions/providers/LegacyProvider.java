package lab.instructions.providers;

import lab.instructions.*;
import lab.instructions.legacy.LegacyTerminal;

public final class LegacyProvider implements OutputProvider {
    @Override public String destination() { return "legacy"; }
    @Override public InstructionOutput create() { return new LegacyTerminalAdapter(new LegacyTerminal(System.out)); }
}
