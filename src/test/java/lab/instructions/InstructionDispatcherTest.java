package lab.instructions;

import static org.junit.jupiter.api.Assertions.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.ServiceLoader;
import lab.instructions.legacy.LegacyTerminal;
import org.junit.jupiter.api.Test;

class InstructionDispatcherTest {
    @Test void choosesOutputFromTheRequestRatherThanTheClient() throws DeliveryException {
        RecordingOutput panel = new RecordingOutput();
        RecordingOutput label = new RecordingOutput();
        InstructionDispatcher dispatcher = new InstructionDispatcher(
                List.of(provider("routine", RoutineInstruction::new)),
                List.of(outputProvider("panel", panel), outputProvider("label", label)));

        dispatcher.dispatch(new InstructionRequest("routine", "label", "12", "Calibrate pipette"));
        assertNull(panel.message);
        assertEquals("ROUTINE: Calibrate pipette", label.message.body());
    }

    @Test void legacyRouteUsesTheAdapter() throws DeliveryException {
        class TerminalSpy extends LegacyTerminal {
            int station;
            TerminalSpy() { super(new PrintStream(OutputStream.nullOutputStream())); }
            @Override public int sendCommand(int station, byte[] payload, char urgency) {
                this.station = station;
                return OK;
            }
        }
        TerminalSpy terminal = new TerminalSpy();
        InstructionDispatcher dispatcher = new InstructionDispatcher(
                List.of(provider("emergency", EmergencyInstruction::new)),
                List.of(outputProvider("legacy", new LegacyTerminalAdapter(terminal))));

        dispatcher.dispatch(new InstructionRequest("emergency", "legacy", "7", "Close fume hood"));
        assertEquals(7, terminal.station);
    }

    @Test void rejectsUnknownDestination() {
        InstructionDispatcher dispatcher = new InstructionDispatcher(
                List.of(provider("routine", RoutineInstruction::new)), List.of());
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> dispatcher.dispatch(new InstructionRequest("routine", "telegraph", "12", "Check")));
        assertTrue(error.getMessage().contains("telegraph"));
    }

    @Test void rejectsUnknownKind() {
        InstructionDispatcher dispatcher = new InstructionDispatcher(List.of(), List.of());
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> dispatcher.dispatch(new InstructionRequest("weekly", "panel", "12", "Check")));
        assertTrue(error.getMessage().contains("weekly"));
    }

    @Test void discoversBothInstructionKindsAndAllThreeOutputs() {
        assertEquals(List.of("emergency", "routine"), ServiceLoader.load(InstructionProvider.class).stream()
                .map(p -> p.get().kind()).sorted().toList());
        assertEquals(List.of("label", "legacy", "panel"), ServiceLoader.load(OutputProvider.class).stream()
                .map(p -> p.get().destination()).sorted().toList());
    }

    private static InstructionProvider provider(String kind, java.util.function.Function<InstructionOutput, Instruction> creator) {
        return new InstructionProvider() {
            @Override public String kind() { return kind; }
            @Override public Instruction create(InstructionOutput output) { return creator.apply(output); }
        };
    }

    private static OutputProvider outputProvider(String destination, InstructionOutput output) {
        return new OutputProvider() {
            @Override public String destination() { return destination; }
            @Override public InstructionOutput create() { return output; }
        };
    }

    private static final class RecordingOutput implements InstructionOutput {
        InstructionMessage message;
        @Override public void publish(InstructionMessage message) { this.message = message; }
    }
}
