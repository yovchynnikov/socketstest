package sockets;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.study.core.sockets.OperationType;
import org.study.core.sockets.Operations;

import static org.junit.Assert.*;

public class OperationsTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testExecuteOperationPlus() throws Exception {
        OperationType request = OperationType.PLUS;
        int[] operators = {1, 2, 123, -10, 0, 1000};
        Integer expectedResult = 1+2+123-10+0+1000;
        Integer result = Operations.executeOperation(request, operators);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testExecuteOperationPlusZeroArgs() throws Exception {
        OperationType request = OperationType.PLUS;
        int[] operators = {};
        Integer expectedResult = 0;
        Integer result = Operations.executeOperation(request, operators);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testExecuteOperationMinus() throws Exception {
        OperationType request = OperationType.MINUS;
        int[] operators = {1, 2, 123, -10, 0, 1000};
        Integer expectedResult = 1-2-123+10-0-1000;
        Integer result = Operations.executeOperation(request, operators);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testExecuteOperationMinusZeroArgs() throws Exception {
        OperationType request = OperationType.MINUS;
        int[] operators = {};
        Integer expectedResult = 0;
        Integer result = Operations.executeOperation(request, operators);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testExecuteOperationTimes() throws Exception {
        OperationType request = OperationType.TIMES;
        int[] operators = {1000, 1, 10, -5};
        Integer expectedResult = 1000*1*10*-5;
        Integer result = Operations.executeOperation(request, operators);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testExecuteOperationTimesZeroArgs() throws Exception {
        OperationType request = OperationType.TIMES;
        int[] operators = {};
        Integer expectedResult = 0;
        Integer result = Operations.executeOperation(request, operators);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testExecuteOperationDivide() throws Exception {
        OperationType request = OperationType.DIVIDE;
        int[] operators = {1000, 1, 10, -5};
        Integer expectedResult = 1000/1/10/-5;
        Integer result = Operations.executeOperation(request, operators);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testExecuteOperationDivideZeroArgs() throws Exception {
        OperationType request = OperationType.DIVIDE;
        int[] operators = {};
        Integer expectedResult = 0;
        Integer result = Operations.executeOperation(request, operators);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testExecuteOperationDivideByZero() throws Exception {
        OperationType request = OperationType.DIVIDE;
        int[] operators = {1000, 1, 0, -5};
        expectedException.expect(Operations.OperationException.class);
        Operations.executeOperation(request, operators);
    }

    @Test
    public void checkParamsAndExecuteOperationPositive() throws Exception {
        String arg = "PLUS 1 2 3 4";
        int expectedResult = 10;
        int result = Operations.checkParamsAndExecuteOperation(arg);
        assertEquals(expectedResult, result);

        arg = "MINUS 10 5 4";
        expectedResult = 1;
        result = Operations.checkParamsAndExecuteOperation(arg);
        assertEquals(expectedResult, result);

        arg = "TIMES 1 2 3 4";
        expectedResult = 24;
        result = Operations.checkParamsAndExecuteOperation(arg);
        assertEquals(expectedResult, result);

        arg = "DIVIDE 10 5 2";
        expectedResult = 1;
        result = Operations.checkParamsAndExecuteOperation(arg);
        assertEquals(expectedResult, result);
    }

    @Test
    public void checkParamsAndExecuteOperationTearDown() throws Exception {
        String arg = "OK CLOSE";
        expectedException.expect(Operations.OperationTeardownException.class);
        Operations.checkParamsAndExecuteOperation(arg);
    }

    @Test
    public void checkParamsAndExecuteOperationStop() throws Exception {
        String arg = "OK STOP";
        expectedException.expect(Operations.OperationStopException.class);
        Operations.checkParamsAndExecuteOperation(arg);
    }

    @Test
    public void checkParamsAndExecuteOperationTearDownNegative() throws Exception {
        String arg = "OK 11";
        expectedException.expect(Operations.OperationException.class);
        Operations.checkParamsAndExecuteOperation(arg);
    }

    @Test
    public void checkParamsAndExecuteOperationBadOperation() throws Exception {
        String arg = "SOMEWRONGTYPE";
        expectedException.expect(Operations.OperationException.class);
        Operations.checkParamsAndExecuteOperation(arg);
    }

    @Test
    public void checkParamsAndExecuteOperationUpperLimit() throws Exception {
        String arg = "PLUS " + Operations.BOUND_MAX_VALUE + " " + Operations.BOUND_MAX_VALUE;
        expectedException.expect(Operations.OperationException.class);
        Operations.checkParamsAndExecuteOperation(arg);
    }

    @Test
    public void checkParamsAndExecuteOperationLowerLimit() throws Exception {
        String arg = "MINUS " + Operations.BOUND_MIN_VALUE + " 1";
        expectedException.expect(Operations.OperationException.class);
        Operations.checkParamsAndExecuteOperation(arg);
    }

    @Test
    public void checkParamsAndExecuteOperationBadSArgument() throws Exception {
        String arg = "MINUS 1 2.1";
        expectedException.expect(Operations.OperationException.class);
        Operations.checkParamsAndExecuteOperation(arg);
    }

}