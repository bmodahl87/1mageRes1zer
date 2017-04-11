package resizer;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by katana on 4/10/17.
 */
public class ImageResizeTest {

    private Logger log = Logger.getLogger(this.getClass());
    private ImageResize resizer;
    private URL pic1Url;
    private URL pic2Url;
    private List<URL> urls;
    private List<BufferedImage> images;
    private Response response;

    @Before
    public void setUp() throws Exception {

        urls = new ArrayList<URL>();
        images = new ArrayList<BufferedImage>();

        resizer = new ImageResize();
        pic1Url = new URL("http://www.clipartbest.com/cliparts/KTn/XXK/KTnXXK8Ec.png");
        pic2Url = new URL("http://www.iconsdb.com/icons/preview/orange/fish-xxl.png");

        urls.add(pic1Url);
        urls.add(pic2Url);

        images.add(ImageIO.read(pic1Url));
        images.add(ImageIO.read(pic2Url));


    }

    @Test
    public void resizeImage() throws Exception {

        response = resizer.resizeImage(urls, 300, 100);

    }

    @Test
    public void resizeImageWidthOnly() throws Exception {

        response = resizer.resizeImageWidthOnly(urls, 300);

    }

    @Test
    public void resizeImageHeightOnly() throws Exception {

        response = resizer.resizeImageHeightOnly(urls, 300);

    }

    @Test
    public void processRequest() throws Exception {

        response = resizer.processRequest(urls, 300, 100);

    }

    @Test
    public void validateInput() throws Exception {

        boolean inputOkay = resizer.validateInput(urls);

        assertTrue("Input should be valid", inputOkay == true);

    }

    @Test
    public void checkURLS() throws Exception {

        boolean urlsOkay = resizer.checkURLS(urls);

        assertTrue("URLs should be images", urlsOkay == true);

    }

    @Test
    public void checkImage() throws Exception {

        boolean imagesOkay = resizer.checkImages(urls);

        assertTrue("Images must be valid", imagesOkay == true);

    }

    @Test
    public void createProcessedGif() throws Exception {

        ByteArrayOutputStream outputImage = resizer.createProcessedImage(images);

        OutputStream outputStream = new FileOutputStream("src/test/resources/createProcessedGifTest.gif");
        outputImage.writeTo(outputStream);

    }

//    @Test
//    public void createProcessedImage() throws Exception {
//
//        images.remove(0);
//
//        ByteArrayOutputStream outputImage = resizer.createProcessedImage(images);
//
//        OutputStream outputStream = new FileOutputStream("src/test/resources/createProcessedImageTest.jpg");
//        outputImage.writeTo(outputStream);
//
//    }

    @Test
    public void resizeImages() throws Exception {

        List<BufferedImage> resizedImages = resizer.resizeImages(urls, 400,400);
    }

    @Test
    public void processImages() throws Exception {

        List<BufferedImage> processedImages = resizer.processImages(urls);

    }

}