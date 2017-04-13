
package resizer;
import com.madgag.gif.fmsware.AnimatedGifEncoder;
import org.apache.log4j.Logger;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;


/**
 * GIFGenerator
 *
 * Class used to generate
 * gifs as a ByteArrayOutputStream
 *
 */

public class GIFGenerator {

    private Logger log = Logger.getLogger(this.getClass());
    private AnimatedGifEncoder encoder;

    /**
     * Class constructor
     * Initializes an AnimatedGifEncoder
     */
    public GIFGenerator() {

        log.info("New gif generator created");
        encoder = new AnimatedGifEncoder();

    }


    /**
     * @param delayInSeconds - Number of seconds to delay in between images, default is 0
     * @param images         - List of buffered images to be generated into a gif
     * @return
     */
    public ByteArrayOutputStream generate(double delayInSeconds, List<BufferedImage> images) {

        log.info("Start gif generation");
        //Create a ByteArrayOutputStream to write the image to
        ByteArrayOutputStream gifStream = new ByteArrayOutputStream();

        //Takes in OutputStream
        encoder.start(gifStream);

        //Set delay (converted to milliseconds)
        encoder.setDelay((int) Math.round(1000 * delayInSeconds));

        log.debug("Delay within generator: " + delayInSeconds);

        //Set the gif to repeat (0 = infinite, -1 = 1 cycle, >= 1 indicates number of cycles)
        encoder.setRepeat(0);

        //Add each image to the gif
        for (BufferedImage frame : images) {

            log.debug("Adding frame to gif");
            encoder.addFrame(frame);

        }

        //Finalizes gif
        encoder.finish();

        log.info("Finished gif generation");

        return gifStream;

    }

}