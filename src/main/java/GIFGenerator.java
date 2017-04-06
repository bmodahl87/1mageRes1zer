import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import resizer.ImageResize;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by katana on 3/27/17.
 */
public class GIFGenerator {

    private AnimatedGifEncoder encoder;
    private ImageResize resizer;

    public GIFGenerator() {

        encoder = new AnimatedGifEncoder();
        resizer = new ImageResize(); /*TODO: Image resizer must be refactored to be used without sending a response
                                       so that this class can resize the images before generating the gif! */

    }

    //Temporarily void until I know how to return something other than a generated File...
    //width/height for later use
    public ByteArrayOutputStream generate(int delayInSeconds, int width, int height, ArrayList<BufferedImage> images) {

        //Create a ByteArrayOutputStream to write the image to
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        //Takes in OutputStream
        encoder.start(outputStream);

        //Set delay (converted to milliseconds)
        encoder.setDelay(1000 * delayInSeconds);

        //Set the gif to repeat (0 = infinite, -1 = 1 cycle, >= 1 indicates number of cycles)
        encoder.setRepeat(0);

        //Add each image to the gif
        for (Image frame : images) {

            encoder.addFrame((BufferedImage) frame);

        }

        //Finalizes gif
        encoder.finish();

        return outputStream;

    }

}
