package lab.instructions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InstructionTest {
    private static final class RecordingOutput implements InstructionOutput {
        InstructionMessage received;
        DeliveryException failure;

        @Override public void publish(InstructionMessage message) throws DeliveryException {
            if (failure != null) throw failure;
            received = message;
        }
    }

    @Test void routineInstructionDelegatesNormalMessage() throws DeliveryException {
        RecordingOutput output = new RecordingOutput();
        new RoutineInstruction(output).issue("12", "Calibrate pipette");
        assertEquals("12", output.received.stationCode());
        assertEquals("ROUTINE: Calibrate pipette", output.received.body());
        assertEquals(Priority.NORMAL, output.received.priority());
    }

    @Test void emergencyInstructionDelegatesUrgentMessage() throws DeliveryException {
        RecordingOutput output = new RecordingOutput();
        new EmergencyInstruction(output).issue("7", "Close fume hood");
        assertEquals("7", output.received.stationCode());
        assertEquals("EMERGENCY: Close fume hood", output.received.body());
        assertEquals(Priority.URGENT, output.received.priority());
    }

    @Test void outputFailurePropagatesThroughAbstraction() {
        RecordingOutput output = new RecordingOutput();
        DeliveryException failure = new DeliveryException(DeliveryException.Reason.DEVICE_UNAVAILABLE, "Panel offline");
        output.failure = failure;
        assertSame(failure, assertThrows(DeliveryException.class,
                () -> new RoutineInstruction(output).issue("12", "Calibrate pipette")));
    }
}
