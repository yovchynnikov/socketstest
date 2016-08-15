package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static final int DEFAULT_PORT_NUMBER = 4444;
    private final int portNumber;

    Server(int portNumber) {
        this.portNumber = portNumber;
    }

    public void run() {
        boolean isStop = false;
        while (!isStop) {
            System.out.println("Waiting for income connection on port " + portNumber);
            try (
                    ServerSocket serverSocket = new ServerSocket(portNumber);
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out =
                            new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()))
            ) {
                System.out.println("Connection started");

                out.println("Operations: PLUS|MINUS|TIMES|DIVIDE. Arguments: round numbers from 0 to 65535. Tear down: OK CLOSE. Stop server: OK STOP");


                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                    try {
                        out.println(execute(inputLine));
                    } catch (Operations.OperationStopException e) {
                        out.println(OperationResult.CLOSED);
                        isStop = true;
                        break;
                    } catch (Operations.OperationTeardownException e) {
                        out.println(OperationResult.CLOSED);
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Server error");
                System.out.println(e.getMessage());
            } finally {
                System.out.println("Connection closed");
            }
        }
        System.out.println("Server stopped");
    }

    String execute(String arg) throws Operations.OperationTeardownException, Operations.OperationStopException {
        try {
            int result = Operations.checkParamsAndExecuteOperation(arg);
            return OperationResult.RESULT.toString() + " " + result;
        } catch (Operations.OperationTeardownException | Operations.OperationStopException ex) {
            throw ex;
        } catch (Operations.OperationException ex) {
            return OperationResult.ERROR.toString();
        }
    }


    public static void main(String[] args) {
        int portNumber = DEFAULT_PORT_NUMBER;
        if (args.length > 0) {
            portNumber = Integer.parseInt(args[0]);
        }
        Server server = new Server(portNumber);
        server.run();
    }
}
