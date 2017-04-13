package resizer;

import entity.ProcessedImage;
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
    public void filterRequestEmptyURLS() throws Exception {

        response = resizer.filterRequest(new ArrayList<URL>(), 0, 0, 0);
        assertTrue("Should be false", resizer.processedImage.getSuccess() == false);

        String entityClass = response.getEntity().getClass().toString();
        assertEquals("Should be equal", "class entity.ProcessedImage", entityClass);


    }

    @Test
    public void filterRequestWidthHeightNoDelay() throws Exception {

        response = resizer.filterRequest(urls, 500, 500, 0);
        assertTrue("Should be true", resizer.processedImage.getSuccess() == true);

        String entityClass = response.getEntity().getClass().toString();
        assertEquals("Should be equal", "class java.io.ByteArrayInputStream", entityClass);


    }

    @Test
    public void filterRequestWidthHeightDelay() throws Exception {

        response = resizer.filterRequest(urls, 500, 500, 1);
        assertTrue("Should be true", resizer.processedImage.getSuccess() == true);

        String entityClass = response.getEntity().getClass().toString();
        assertEquals("Should be equal", "class java.io.ByteArrayInputStream", entityClass);


    }

    @Test
    public void filterRequestNoWidthHeightNoDelay() throws Exception {

        response = resizer.filterRequest(urls, 0, 500, 0);
        assertTrue("Should be true", resizer.processedImage.getSuccess() == true);

        String entityClass = response.getEntity().getClass().toString();
        assertEquals("Should be equal", "class java.io.ByteArrayInputStream", entityClass);


    }

    @Test
    public void filterRequestNoWidthHeightDelay() throws Exception {

        response = resizer.filterRequest(urls, 0, 500, 1);
        assertTrue("Should be true", resizer.processedImage.getSuccess() == true);

        String entityClass = response.getEntity().getClass().toString();
        assertEquals("Should be equal", "class java.io.ByteArrayInputStream", entityClass);


    }

    @Test
    public void filterRequestWidthNoHeightNoDelay() throws Exception {

        response = resizer.filterRequest(urls, 500, 0, 0);
        assertTrue("Should be true", resizer.processedImage.getSuccess() == true);

        String entityClass = response.getEntity().getClass().toString();
        assertEquals("Should be equal", "class java.io.ByteArrayInputStream", entityClass);


    }

    @Test
    public void filterRequestWidthNoHeightDelay() throws Exception {

        response = resizer.filterRequest(urls, 500, 0, 1);
        assertTrue("Should be true", resizer.processedImage.getSuccess() == true);

        String entityClass = response.getEntity().getClass().toString();
        assertEquals("Should be equal", "class java.io.ByteArrayInputStream", entityClass);


    }

    @Test
    public void filterRequestNoWidthNoHeightNoDelay() throws Exception {

        response = resizer.filterRequest(urls, 0, 0, 0);
        assertTrue("Should be true", resizer.processedImage.getSuccess() == true);

        String entityClass = response.getEntity().getClass().toString();
        assertEquals("Should be equal", "class java.io.ByteArrayInputStream", entityClass);


    }

    @Test
    public void filterRequestNoWidthNoHeightDelay() throws Exception {

        response = resizer.filterRequest(urls, 0, 0, 1);
        assertTrue("Should be true", resizer.processedImage.getSuccess() == true);

        String entityClass = response.getEntity().getClass().toString();
        assertEquals("Should be equal", "class java.io.ByteArrayInputStream", entityClass);


    }

    @Test
    public void sendBackOriginalImageValidInput() throws Exception {

        response = resizer.sendBackOriginalImage(urls, 0);

        assertTrue("Should be true", resizer.processedImage.getSuccess() == true);

    }

    @Test
    public void sendBackOriginalImageInvalidInput() throws Exception {

        urls.add(new URL("https://stackoverflow.com/"));

        response = resizer.sendBackOriginalImage(urls, 0);
        assertTrue("Should be false", resizer.processedImage.getSuccess() == false);


    }

    @Test
    public void validateInputValid() throws Exception {

        boolean inputOkay = resizer.validateInput(urls);

        assertTrue("Should be true", inputOkay == true);

    }

    @Test
    public void validateInputInvalid() throws Exception {

        urls.add(new URL("https://stackoverflow.com/"));
        boolean inputOkay = resizer.validateInput(urls);

        assertTrue("Should be false", inputOkay ==  false);

    }

    @Test
    public void checkURLSValid() throws Exception {

        boolean urlsOkay = resizer.checkURLS(urls);

        assertTrue("Should be true", urlsOkay == true);

    }

    @Test
    public void checkURLSInvalid() throws Exception {

        urls.add(new URL("https://stackoverflow.com/"));
        boolean urlsOkay = resizer.checkURLS(urls);

        assertTrue("Should be false", urlsOkay == false);
    }

    @Test
    public void checkImagesValid() throws Exception {

        boolean imagesOkay = resizer.checkImages(urls);

        assertTrue("Should be true", imagesOkay == true);

    }

    @Test
    public void checkImagesInvalid() throws Exception {

        urls.add(new URL("http://che.org.il/wp-content/uploads/2016/12/pdf-sample.pdf"));
        boolean imagesOkay = resizer.checkImages(urls);

        assertTrue("Should be false", imagesOkay == false);

    }

}