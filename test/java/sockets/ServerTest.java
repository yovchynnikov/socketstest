package sockets;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class ServerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void runPositive() throws Exception {
        String args = "PLUS 1 2 3";
        String expectedResult = "RESULT 6";
        Server obj = new Server(Server.DEFAULT_PORT_NUMBER);
        String result = obj.execute(args);
        assertEquals(expectedResult, result);
        // other positive tests may be put there
    }

    @Test
    public void runNegative() throws Exception {
        String args = "SOMEWRONGCOMMAND";
        Server obj = new Server(Server.DEFAULT_PORT_NUMBER);
        String expectedResult = "ERROR";
        String result = obj.execute(args);
        assertEquals(expectedResult, result);
        // other negative tests may be put there
    }

    @Test
    public void runTearDown() throws Exception {
        String args = "OK CLOSE";
        Server obj = new Server(Server.DEFAULT_PORT_NUMBER);
        expectedException.expect(Operations.OperationTeardownException.class);
        obj.execute(args);
    }

    @Test
    public void runStop() throws Exception {
        String args = "OK STOP";
        Server obj = new Server(Server.DEFAULT_PORT_NUMBER);
        expectedException.expect(Operations.OperationStopException.class);
        obj.execute(args);
    }

}