package resizer;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.awt.image.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.List;

import entity.ProcessedImage;
import net.coobird.thumbnailator.*;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.log4j.Logger;


/**
 * Created by bmodahl & also Keith on 3/7/17.
 */

@Path("/image")
public class ImageResize {
    private Logger log = Logger.getLogger(this.getClass());

    ProcessedImage processedImage = new ProcessedImage();

    @Path("/")
    @GET
    public Response filterRequest(@QueryParam("urls") List<URL> urls,
                                  @QueryParam("width") int width,
                                  @QueryParam("height") int height,
                                  @QueryParam("delay") double delay) throws IOException {

        Response response = null;

        if (urls.isEmpty()) {

            processedImage.setSuccess(false);
            processedImage.setMessage("No URL received.");
            response = Response.ok(processedImage, MediaType.APPLICATION_JSON).build();

        } else if (width != 0 && height != 0 && delay == 0){

            processedImage.setSuccess(true);
            response = processRequest(urls, height, width, 0);

        } else if (width != 0 && height != 0 && delay != 0) {

            processedImage.setSuccess(true);
            response = processRequest(urls, height, width, delay);

        } else if (width == 0 && height != 0 && delay == 0) {

            processedImage.setSuccess(true);
            response = processRequest(urls, height, 0, 0);

        } else if (width == 0 && height != 0 && delay != 0) {

            processedImage.setSuccess(true);
            response = processRequest(urls, height, 0, delay);

        } else if (width != 0 && height == 0 && delay == 0) {

            processedImage.setSuccess(true);
            response = processRequest(urls, 0, width, 0);

        } else if (width != 0 && height == 0 && delay != 0) {

            processedImage.setSuccess(true);
            response = processRequest(urls, 0, width, delay);

        } else if (width == 0 && height == 0 && delay == 0) {

            processedImage.setSuccess(true);
            response = sendBackOriginalImage(urls, 0);

        } else if (width == 0 && height == 0 && delay != 0) {

            processedImage.setSuccess(true);
            response = sendBackOriginalImage(urls, delay);

        } else {

            processedImage.setSuccess(false);
            processedImage.setMessage("Unable to process request, reason unknown.");
            response = Response.ok(processedImage, MediaType.APPLICATION_JSON).build();
            log.debug("Unable to filter request within filterRequest()");

        }

        return response;

    }

    public Response sendBackOriginalImage(List<URL> urls, double delay) throws IOException {

        int width = 0;
        int height = 0;

        if (validateInput(urls)) {


            List<BufferedImage> images = processImages(urls);

            BufferedImage firstImg = images.get(0);

            width = firstImg.getWidth();
            height = firstImg.getHeight();

            processedImage.setSuccess(true);

            return processRequest(urls, height, width, delay);

        } else {
            processedImage.setSuccess(false);
            return Response.ok(processedImage, MediaType.APPLICATION_JSON).build();
        }

    }

    public Response processRequest(List<URL> urls, int height, int width, double delay) throws IOException {

        List<BufferedImage> resizedImages;

        if (validateInput(urls)) {
            setImageType(urls);
            resizedImages = resizeImages(urls, width, height);
            byte[] imageData = createProcessedImage(resizedImages, delay).toByteArray();

            log.info(processedImage.getSubType());
            return Response.ok(new ByteArrayInputStream(imageData), new MediaType("image", processedImage.getSubType())).build();

        } else {

            return Response.ok(processedImage, MediaType.APPLICATION_JSON).build();

        }

    }

    public boolean validateInput(List<URL> urls) throws IOException {

        if (checkURLS(urls)){
            if(checkImages(urls)){
                return true;
            }
        }
        return false;
    }

    public boolean checkURLS(List<URL> urls) throws IOException {

        UrlValidator urlValidator = new UrlValidator();

        for (URL url : urls) {

            String urlString = url.toExternalForm();

            if (urlValidator.isValid(urlString)) {

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                String contentType = connection.getHeaderField("Content-Type");

                boolean isImage = contentType.startsWith("image/");

                if (isImage == false) {

                    processedImage.setSuccess(false);
                    processedImage.setMessage("Invalid URL!");
                    return false;
                }


            } else {

                processedImage.setSuccess(false);
                processedImage.setMessage("Broken URL!");
                return false;

            }

        }

        return true;
    }


    public boolean checkImages(List<URL> urls) throws IOException {
        for(URL url:urls) {
            BufferedImage image = ImageIO.read(url);
            if(image == null){

                processedImage.setSuccess(false);
                processedImage.setMessage("Invalid Image!");
                return false;
            }
        }
        return true;
    }

    public void setImageType(List<URL> urls) throws IOException {
        if (urls.size() > 1){
            processedImage.setSubType("gif");
        } else {
            URLConnection connection = urls.get(0).openConnection();
            String contentType = connection.getHeaderField("Content-Type");
            processedImage.setSubType(contentType.substring(6));
        }
    }

    public ByteArrayOutputStream createProcessedImage(List<BufferedImage> resizedImages, double delay) throws IOException {

        ByteArrayOutputStream imageData;

        //If gif
        if (resizedImages.size() > 1) {

            GIFGenerator gen = new GIFGenerator();

            imageData = gen.generate(delay, resizedImages);

        //If img
        } else {

            BufferedImage image = resizedImages.get(0);
            ImageIO.write(image, processedImage.getSubType(), imageData = new ByteArrayOutputStream());

        }

        return imageData;

    }

    public List<BufferedImage> resizeImages(List<URL> urls, int width, int height) throws IOException {

        List<BufferedImage> images = processImages(urls);
        List<BufferedImage> resizedImages = new ArrayList<BufferedImage>();

        if (width == 0) {
            for (BufferedImage image : images) {
                BufferedImage thumbnail = Thumbnails.of(image)
                        .size(image.getWidth(), height)
                        .keepAspectRatio(true)
                        .outputFormat(processedImage.getSubType())
                        .asBufferedImage();
                resizedImages.add(thumbnail);
                log.info(image.getHeight());

            }


        } else if (height == 0){
            for (BufferedImage image : images) {
                BufferedImage thumbnail = Thumbnails.of(image)
                        .size(width, image.getHeight())
                        .keepAspectRatio(true)
                        .outputFormat(processedImage.getSubType())
                        .asBufferedImage();
                resizedImages.add(thumbnail);
                log.info(image.getWidth());

            }


        } else {

            for (BufferedImage image : images) {
                BufferedImage thumbnail = Thumbnails.of(image)
                        .size(width, height)
                        .keepAspectRatio(false)
                        .outputFormat(processedImage.getSubType())
                        .asBufferedImage();
                resizedImages.add(thumbnail);
                log.info(image.getWidth());
                log.info(image.getHeight());

            }


        }

        processedImage.setResizedImages(resizedImages);

        return resizedImages;

    }

    public List<BufferedImage> processImages(List<URL> urls) throws IOException {

        List<BufferedImage> images = new ArrayList<BufferedImage>();

        for (URL url : urls) {
            images.add(ImageIO.read(url));
        }

        processedImage.setImagesReceived(images);

        return images;
    }


    public ProcessedImage getProcessedImage() {

        return processedImage;

    }
}