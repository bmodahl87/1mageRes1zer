import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by katana on 3/27/17.
 */
public class GifGeneratorTest {
    @Test
    public void tryGifEncoder() throws Exception {

        GifGenerator generator = new GifGenerator();

        generator.tryGifEncoder();

    }

}