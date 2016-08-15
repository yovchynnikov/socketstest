package sockets;

public enum OperationType {
    PLUS("PLUS"), MINUS("MINUS"), TIMES("TIMES"), DIVIDE("DIVIDE"), OK("OK");

    private final String name;

    OperationType(String name) {
        this.name = name;
    }

    public static OperationType operationTypeByName(String name) {
        for (OperationType operationType : values()) {
            if (operationType.name.equals(name)) {
                return operationType;
            }
        }
        throw new IllegalArgumentException("Cannot identify operation " + name);
    }

}
