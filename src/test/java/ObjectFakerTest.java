import com.mbi.Faker;
import com.mbi.ObjectFaker;
import com.mbi.parameters.Parameter;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ObjectFakerTest {

    private final Faker faker = new ObjectFaker();

    @Test
    public void testSeveralParameters() {
        var s = faker.fakeData("asd {$uid} asd {$caller} ddd");

        assertFalse(s.contains("{$"));
        assertTrue(s.contains(" asd ObjectFakerTest.testSeveralParameters ddd"));
    }

    @Test
    public void testNoParameters() {
        var s = faker.fakeData("asd ddd");

        assertEquals(s, "asd ddd");
    }

    @Test
    public void testFakeNotString() {
        int i = faker.fakeData(1);

        assertEquals(i, 1);
    }

    @Test
    public void testNoParameterEnding() {
        var ex = expectThrows(IllegalArgumentException.class, () -> faker.fakeData("asd} {$uid"));
        assertEquals(ex.getMessage(), "Incorrect parameter: asd} {$uid");
    }

    @Test
    public void testEmptyParameterName() {
        var ex = expectThrows(IllegalArgumentException.class, () -> new Parameter(""));
        assertEquals(ex.getMessage(), "Parameter is empty or null");
    }

    @Test
    public void testEmptyFullParameterName() {
        var ex = expectThrows(IllegalArgumentException.class, () -> new Parameter(";2;s"));
        assertEquals(ex.getMessage(), "Parameter name is missing: ;2;s");
    }

    @Test
    public void testEmptyParameterNull() {
        var ex = expectThrows(IllegalArgumentException.class, () -> new Parameter(null));
        assertEquals(ex.getMessage(), "Parameter is empty or null");
    }

    @Test
    public void testNotClosedParameter() {
        var ex = expectThrows(IllegalArgumentException.class, () -> faker.fakeData("abc {$uid abc {$caller}"));
        assertEquals(ex.getMessage(), "Incorrect parameter: abc {$uid abc {$caller}");
    }
}
