package pl.edu.agh.miss;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class SampleTest {

    @Test
    public void sampleTest() {
        // junit & mockito available
        Object object = mock(Object.class);
        assertNotNull(object);
    }

}