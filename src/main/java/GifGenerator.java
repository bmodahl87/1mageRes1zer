import com.madgag.gif.fmsware.AnimatedGifEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by katana on 3/27/17.
 */
public class GifGenerator {

    private AnimatedGifEncoder encoder;

    public GifGenerator() {

        encoder = new AnimatedGifEncoder();

    }

    public void tryGifEncoder() throws IOException {
        Image image1 = ImageIO.read(new URL("http://proprofs-cdn.s3.amazonaws.com/images/games/user_images/misc/1141652403.png"));
        Image image2 = ImageIO.read(new URL("https://www.iconexperience.com/_img/v_collection_png/256x256/shadow/flower_red.png"));
        BufferedImage buff1 = (BufferedImage) image1;
        BufferedImage buff2 = (BufferedImage) image2;

        encoder.start("src/main/resources/test.gif");
        encoder.setDelay(1000);
        encoder.setRepeat(0);
        encoder.addFrame(buff1);
        encoder.addFrame(buff2);

        encoder.finish();

    }

}
