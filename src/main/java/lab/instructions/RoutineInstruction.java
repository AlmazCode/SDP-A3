package lab.instructions;

public final class RoutineInstruction extends Instruction {
    public RoutineInstruction(InstructionOutput output) { super(output); }

    @Override public void issue(String stationCode, String text) throws DeliveryException {
        publish(new InstructionMessage(stationCode, "ROUTINE: " + text, Priority.NORMAL));
    }
}
