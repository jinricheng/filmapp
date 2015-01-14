package cat.udl.eps.softarch.hello.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GreetingTest {

    private Film greeting;

    @Before
    public void setUp() throws Exception {
        greeting = new Film();
    }

    @Test
    public void testSetContent() throws Exception {
        greeting.setTitle("test");
        assertEquals("test", greeting.getTitle());
    }
}