import com.madgag.gif.fmsware.AnimatedGifEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by katana on 3/27/17.
 */
public class GifGenerator {

    private AnimatedGifEncoder encoder;

    public GifGenerator() {

        encoder = new AnimatedGifEncoder();

    }

    public void tryGifEncoder() throws IOException {
        Image image1 = ImageIO.read(new File("~/test_images/image1.jpg"));
        Image image2 = ImageIO.read(new File("~/test_images/image2.jpg"));
        BufferedImage buff1 = (BufferedImage) image1;
        BufferedImage buff2 = (BufferedImage) image2;

        encoder.start("~/test_images/test.gif");
        encoder.setDelay(1000);
        encoder.addFrame(buff1);
        encoder.addFrame(buff2);
        encoder.finish();

    }

}
