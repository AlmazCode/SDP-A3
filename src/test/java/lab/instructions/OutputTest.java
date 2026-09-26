package lab.instructions;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class OutputTest {
    @Test void panelRendersAnUrgentInstruction() throws DeliveryException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        new PanelOutput(new PrintStream(bytes, true, StandardCharsets.UTF_8))
                .publish(new InstructionMessage("7", "Close fume hood", Priority.URGENT));
        assertEquals("[PANEL] station=7 priority=URGENT: Close fume hood\n", bytes.toString(StandardCharsets.UTF_8));
    }

    @Test void labelRendersACompactInstruction() throws DeliveryException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        new LabelOutput(new PrintStream(bytes, true, StandardCharsets.UTF_8))
                .publish(new InstructionMessage("12", "Calibrate pipette", Priority.NORMAL));
        assertEquals("[LABEL 12] NORMAL | Calibrate pipette\n", bytes.toString(StandardCharsets.UTF_8));
    }
}
