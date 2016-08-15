package sockets;

@FunctionalInterface
public interface Operation {
    Integer compute(int[] args);
}
