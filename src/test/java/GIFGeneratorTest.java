import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by katana on 3/27/17.
 */
public class GIFGeneratorTest {

    private GIFGenerator generator;

    @Before
    public void setUp() {

        generator = new GIFGenerator();

    }

    @Test
    public void tryGifEncoder() throws Exception {

        generator.tryGifEncoder();

    }

    @Test
    public void generate() throws IOException {

        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();

        URL pic1Url = new URL("http://www.clipartbest.com/cliparts/KTn/XXK/KTnXXK8Ec.png");
        URL pic2Url = new URL("http://www.iconsdb.com/icons/preview/orange/fish-xxl.png");

        URLConnection connection = pic1Url.openConnection();

        BufferedImage pic1Img = ImageIO.read(pic1Url);
        BufferedImage pic2Img = ImageIO.read(pic2Url);

        images.add(pic1Img);
        images.add(pic2Img);

        connection.getContentType();

        ByteArrayOutputStream gifStream = generator.generate(3000, images);

        byte[] imageData = gifStream.toByteArray();

        OutputStream out = null;

        try {

            out = new BufferedOutputStream(new FileOutputStream("/home/katana/EnterpriseRepos/1mageRes1zer/src/test/resources/test2.gif"));
            out.write(imageData);

        } finally {

            if (out != null) {

                out.close();

            }

        }

    }
}