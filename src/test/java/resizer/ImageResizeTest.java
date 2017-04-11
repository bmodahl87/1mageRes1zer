package resizer;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
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
    private Response response;

    @Before
    public void setUp() throws Exception {

        urls = new ArrayList<URL>();

        resizer = new ImageResize();
        pic1Url = new URL("http://www.clipartbest.com/cliparts/KTn/XXK/KTnXXK8Ec.png");
        pic2Url = new URL("http://www.iconsdb.com/icons/preview/orange/fish-xxl.png");

        urls.add(pic1Url);
        urls.add(pic2Url);


    }

    @Test
    public void resizeImage() throws Exception {

        response = resizer.resizeImage(urls, 300, 100);
        log.info("RESPONSE -----\n\n" + response.toString());

    }

    @Test
    public void resizeImageWidthOnly() throws Exception {

    }

    @Test
    public void resizeImageHeightOnly() throws Exception {

    }

    @Test
    public void processRequest() throws Exception {

    }

    @Test
    public void validateInput() throws Exception {

    }

    @Test
    public void checkURLS() throws Exception {

    }

    @Test
    public void checkImage() throws Exception {

    }

    @Test
    public void createProcessedImage() throws Exception {

    }

    @Test
    public void resizeImages() throws Exception {

    }

    @Test
    public void processImages() throws Exception {

    }

}