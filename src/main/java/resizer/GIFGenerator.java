package resizer;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by katana on 3/27/17.
 */
public class GIFGenerator {

    private AnimatedGifEncoder encoder;

    public GIFGenerator() {

        encoder = new AnimatedGifEncoder();

    }

    //width/height for later use
    public ByteArrayOutputStream generate(int delayInSeconds, List<BufferedImage> images) {

        //Create a ByteArrayOutputStream to write the image to
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        //Takes in OutputStream
        encoder.start(outputStream);

        //Set delay (converted to milliseconds)
        encoder.setDelay(1000 * delayInSeconds);

        //Set the gif to repeat (0 = infinite, -1 = 1 cycle, >= 1 indicates number of cycles)
        encoder.setRepeat(0);

        //Add each image to the gif
        for (BufferedImage frame : images) {

            encoder.addFrame(frame);

        }

        //Finalizes gif
        encoder.finish();

        return outputStream;

    }



}