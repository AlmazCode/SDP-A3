package lab.instructions;

import java.util.Arrays;
import java.util.ServiceLoader;

public final class Main {
    private Main() {}

    public static void main(String[] args) throws DeliveryException {
        if (args.length < 4) {
            System.out.println("Usage: <routine|emergency> <panel|label|legacy> <station-number> <instruction text>");
            return;
        }
        InstructionDispatcher dispatcher = new InstructionDispatcher(
                ServiceLoader.load(InstructionProvider.class).stream().map(p -> p.get()).toList(),
                ServiceLoader.load(OutputProvider.class).stream().map(p -> p.get()).toList());
        dispatcher.dispatch(new InstructionRequest(args[0], args[1], args[2], String.join(" ", Arrays.copyOfRange(args, 3, args.length))));
    }
}
