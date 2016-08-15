package sockets;

import java.util.Arrays;

public class Operations {


    public static final int MAX_COUNT_OF_OPERATIONS = 256;
    public static final int BOUND_MAX_VALUE = 65535;
    public static final int BOUND_MIN_VALUE = 0;
    public static final String TEARDOWN_ARGUMENT = "CLOSE";
    private static final String STOP_ARGUMENT = "STOP";

    public static Operation getOperation(OperationType operationType) throws OperationException {
        switch (operationType) {
            case PLUS:
                return (args) -> Arrays.stream(args).sum();
            case MINUS:
                return (args) -> Arrays.stream(args).reduce((i, j) -> i - j).orElse(0);
            case TIMES:
                return (args) -> Arrays.stream(args).reduce((i, j) -> i * j).orElse(0);
            case DIVIDE:
                return (args) -> Arrays.stream(args).reduce((i, j) -> i / j).orElse(0);
            default:
                throw new OperationException("Unsupported operation: " + operationType);
        }
    }

    public static int executeOperation(OperationType operationType, int[] args) throws OperationException {
        Operation operation = getOperation(operationType);
        try {
            return operation.compute(args);
        } catch (Exception e) {
            throw new OperationException(e);
        }
    }

    public static int checkParamsAndExecuteOperation(String arg) throws OperationException {
        String[] args = arg.split("\\s");
        if (args.length > MAX_COUNT_OF_OPERATIONS) {
            throw new OperationException("Maximum operations count is reached");
        }
        if (args.length == 0) {
            throw new OperationException("No args are passed");
        }
        try {
            OperationType operationType = OperationType.operationTypeByName(args[0].toUpperCase());
            if (OperationType.OK.equals(operationType)) {
                if (args.length > 1 && TEARDOWN_ARGUMENT.equals(args[1])) {
                    throw new OperationTeardownException();
                } else if (args.length > 1 && STOP_ARGUMENT.equals(args[1])) {
                    throw new OperationStopException();
                } else {
                    throw new OperationException("Unknown operation: " + arg);
                }
            } else {
                int[] argInts = Arrays.stream(Arrays.copyOfRange(args, 1, args.length))
                        .mapToInt(Integer::valueOf)
                        .toArray();
                int result = executeOperation(operationType, argInts);
                if (result < BOUND_MIN_VALUE || result > BOUND_MAX_VALUE) {
                    throw new OperationException("Result does not fit to the allowed result: " + result);
                }
                return result;
            }
        } catch (NumberFormatException ex) {
            throw new OperationException("Params convert error", ex);
        } catch (IllegalArgumentException ex) {
            throw new OperationException("Cannot identify operation type", ex);
        }
    }

    public static class OperationException extends Exception {
        public OperationException() {
            super();
        }

        public OperationException(String s) {
            super(s);
        }

        public OperationException(Exception e) {
            super(e);
        }

        public OperationException(String s, Exception ex) {
            super(s, ex);
        }
    }

    public static class OperationTeardownException extends OperationException {
        public OperationTeardownException() {
            super();
        }
    }

    public static class OperationStopException extends OperationException {
        public OperationStopException() {
            super();
        }
    }
}
