package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import static java.lang.System.exit;

public class Client {

    private static final int EXIT_BAD_PARAMS = 1;

    public void run(String hostName, int portNumber, boolean demoMode) {
        try (
                Socket kkSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(kkSocket.getInputStream()))
        ) {
            if (!demoMode) {
                runUser(in, out);
            } else {
                runDemo(in, out);
            }
        } catch (UnknownHostException e) {
            System.out.println("Error: Unknown host");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error: cannot communicate with server");
            e.printStackTrace();
        }
    }

    private void runUser(BufferedReader in, PrintWriter out) throws IOException {
        String fromUser, fromServer;
        BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));

        while ((fromServer = in.readLine()) != null) {
            System.out.println("Server: " + fromServer);
            if (fromServer.equals("CLOSED"))
                break;

            fromUser = stdIn.readLine();
            if (fromUser != null) {
                System.out.println("Client: " + fromUser);
                out.println(fromUser);
            }
        }
    }

    private void runDemo(BufferedReader in, PrintWriter out) throws IOException {
        System.out.println("Staring demo mode");
        String fromUser, fromServer;
        fromUser = "PLUS 1 2 3 4";
        System.out.println("send " + fromUser);
        out.println(fromUser);
        fromServer = in.readLine();
        System.out.println("received " + fromServer);

        fromUser = "MINUS 128 23 0 18";
        System.out.println("send " + fromUser);
        out.println(fromUser);
        fromServer = in.readLine();
        System.out.println("received " + fromServer);

        fromUser = "TIMES 12 10 2";
        System.out.println("send " + fromUser);
        out.println(fromUser);
        fromServer = in.readLine();
        System.out.println("received " + fromServer);

        fromUser = "DIVIDE 100 25 1";
        System.out.println("send " + fromUser);
        out.println(fromUser);
        fromServer = in.readLine();
        System.out.println("received " + fromServer);

        fromUser = "DIVIDE 100 0";
        System.out.println("send " + fromUser);
        out.println(fromUser);
        fromServer = in.readLine();
        System.out.println("received " + fromServer);

        fromUser = "MINUS 1 2";
        System.out.println("send " + fromUser);
        out.println(fromUser);
        fromServer = in.readLine();
        System.out.println("received " + fromServer);

        fromUser = "OK CLOSE";
        System.out.println("send " + fromUser);
        out.println(fromUser);
        fromServer = in.readLine();
        System.out.println("received " + fromServer);

    }

    public static void main(String[] args) {
        String hostName = "";
        int portNumber = 0;
        boolean demoMode = args.length == 3;
        try {
            hostName = args[0];
            portNumber = Integer.parseInt(args[1]);
            demoMode = demoMode && args[2].equalsIgnoreCase("demo");

        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("Wrong number or type of arguments");
            System.out.println("Usage: <app> <hostName> <portNumber> [demo] ..");
            System.out.println("\t hostName - server host name (e.g. localhost)");
            System.out.println("\t portNumber - server port number");
            System.out.println("\t demo - start demo mode");
            exit(EXIT_BAD_PARAMS);
        }
        new Client().run(hostName, portNumber, demoMode);
    }
}
