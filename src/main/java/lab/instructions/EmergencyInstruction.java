package lab.instructions;

public final class EmergencyInstruction extends Instruction {
    public EmergencyInstruction(InstructionOutput output) { super(output); }

    @Override public void issue(String stationCode, String text) throws DeliveryException {
        publish(new InstructionMessage(stationCode, "EMERGENCY: " + text, Priority.URGENT));
    }
}
