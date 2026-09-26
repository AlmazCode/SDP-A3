package lab.instructions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class InstructionDispatcher {
    private final Map<String, InstructionProvider> instructions;
    private final Map<String, OutputProvider> outputs;

    public InstructionDispatcher(Collection<InstructionProvider> instructions, Collection<OutputProvider> outputs) {
        Map<String, InstructionProvider> instructionMap = new HashMap<>();
        for (InstructionProvider provider : instructions) {
            if (instructionMap.putIfAbsent(provider.kind(), provider) != null)
                throw new IllegalArgumentException("Duplicate instruction kind: " + provider.kind());
        }
        Map<String, OutputProvider> outputMap = new HashMap<>();
        for (OutputProvider provider : outputs) {
            if (outputMap.putIfAbsent(provider.destination(), provider) != null)
                throw new IllegalArgumentException("Duplicate output destination: " + provider.destination());
        }
        this.instructions = Map.copyOf(instructionMap);
        this.outputs = Map.copyOf(outputMap);
    }

    public void dispatch(InstructionRequest request) throws DeliveryException {
        InstructionProvider instruction = instructions.get(request.kind());
        if (instruction == null) throw new IllegalArgumentException("Unknown instruction kind: " + request.kind());
        OutputProvider output = outputs.get(request.destination());
        if (output == null) throw new IllegalArgumentException("Unknown output destination: " + request.destination());
        instruction.create(output.create()).issue(request.stationCode(), request.text());
    }
}
