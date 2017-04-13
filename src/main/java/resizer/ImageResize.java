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

    /**
     *
     * @param urls
     * @param width
     * @param height
     * @param delay
     * @return
     * @throws IOException
     */
    @Path("/")
    @GET
    public Response filterRequest(@QueryParam("urls") List<URL> urls,
                                  @QueryParam("width") int width,
                                  @QueryParam("height") int height,
                                  @QueryParam("delay") double delay) throws IOException {

        Response response = null;

        if (urls.isEmpty()) {

            log.info("no url sent, proceed with error msg response");
            processedImage.setSuccess(false);
            processedImage.setMessage("No URL received.");
            response = Response.ok(processedImage, MediaType.APPLICATION_JSON).build();

        } else if (width != 0 && height != 0 && delay == 0){

            log.info("User sent params: Height, Width.");
            processedImage.setSuccess(true);
            response = processRequest(urls, height, width, 0);

        } else if (width != 0 && height != 0 && delay != 0) {

            log.info("User sent params: Height, Width + Delay");
            processedImage.setSuccess(true);
            response = processRequest(urls, height, width, delay);

        } else if (width == 0 && height != 0 && delay == 0) {

            log.info("User sent params: Height");
            processedImage.setSuccess(true);
            response = processRequest(urls, height, 0, 0);

        } else if (width == 0 && height != 0 && delay != 0) {

            log.info("User sent params: Height + Delay");
            processedImage.setSuccess(true);
            response = processRequest(urls, height, 0, delay);

        } else if (width != 0 && height == 0 && delay == 0) {

            log.info("User sent params: Width");
            processedImage.setSuccess(true);
            response = processRequest(urls, 0, width, 0);

        } else if (width != 0 && height == 0 && delay != 0) {

            log.info("User sent params: Width + Delay");
            processedImage.setSuccess(true);
            response = processRequest(urls, 0, width, delay);

        } else if (width == 0 && height == 0 && delay == 0) {

            log.info("user only sent urls");
            processedImage.setSuccess(true);
            response = sendBackOriginalImage(urls, 0);

        } else if (width == 0 && height == 0 && delay != 0) {

            log.info("user only sent urls + Delay");
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

    /**
     *
     * Method for proccessing an image with no change in width or height
     *
     * @param urls
     * @param delay
     * @return
     * @throws IOException
     */
    public Response sendBackOriginalImage(List<URL> urls, double delay) throws IOException {

        log.info("begin the sendBackOriginalImage method");

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

    /**
     *
     * Method calls for image validation, image resizing and general image processing methods
     *
     * @param urls
     * @param height
     * @param width
     * @param delay
     * @return
     * @throws IOException
     */
    public Response processRequest(List<URL> urls, int height, int width, double delay) throws IOException {

        log.info("begin the processRequest method");

        List<BufferedImage> resizedImages;

        if (validateInput(urls)) {
            setImageType(urls);
            resizedImages = resizeImages(urls, width, height);
            byte[] imageData = createProcessedImage(resizedImages, delay).toByteArray();

            return Response.ok(new ByteArrayInputStream(imageData), new MediaType("image", processedImage.getSubType())).build();

        } else {

            return Response.ok(processedImage, MediaType.APPLICATION_JSON).build();
        }
    }

    /**
     *
     * Method calls all specific validation methods
     *
     * @param urls
     * @return
     * @throws IOException
     */
    public boolean validateInput(List<URL> urls) throws IOException {

        log.info("Validating Input");
        if (checkURLS(urls)){
            int counter = 0;
            if(checkImages(urls)){
                log.info("Images: " + counter + " passed validation");
                counter ++;
                return true;
            }
        }
        log.error("Input failed validation");
        return false;
    }

    /**
     *
     * Method validates the urls that the user sent
     *
     * @param urls
     * @return
     * @throws IOException
     */
    public boolean checkURLS(List<URL> urls) throws IOException {

        log.info("Begin Validate Urls");

        UrlValidator urlValidator = new UrlValidator();

        for (URL url : urls) {

            String urlString = url.toExternalForm();

            log.info("Begin Checking http://");
            if (urlValidator.isValid(urlString)) {
                log.info("Passed URL Check #1");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                String contentType = connection.getHeaderField("Content-Type");

                boolean isImage = contentType.startsWith("image/");

                if (isImage == false) {

                    log.error("Failed Url Check #2");
                    processedImage.setSuccess(false);
                    processedImage.setMessage("Invalid URL!");
                    return false;
                }


            } else {

                log.error("Failed URLs Validation Check #1");
                processedImage.setSuccess(false);
                processedImage.setMessage("Broken URL!");
                return false;
            }
        }
        log.info("URLs Passed Validation Check");
        return true;
    }


    /**
     *
     * Method insures that the urls contain images
     *
     * @param urls
     * @return
     * @throws IOException
     */
    public boolean checkImages(List<URL> urls) throws IOException {

        log.info("Begin Validation If Images Exist");
        for(URL url:urls) {
            BufferedImage image = ImageIO.read(url);
            if(image == null){

                log.error("Failed Image Validation: Image is null");
                processedImage.setSuccess(false);
                processedImage.setMessage("Invalid Image!");
                return false;
            }
        }
        log.info("Passed Image Validation Check");
        return true;
    }

    /**
     *
     * method is used to set the return type of the images ("jpg" vs "png" vs "gif")
     *
     * @param urls
     * @throws IOException
     */
    public void setImageType(List<URL> urls) throws IOException {

        log.info("Retreiving Image Subtype");
        if (urls.size() > 1){
            processedImage.setSubType("gif");
            log.info("Image Subtype = " + processedImage.getSubType());
        } else {
            URLConnection connection = urls.get(0).openConnection();
            String contentType = connection.getHeaderField("Content-Type");
            processedImage.setSubType(contentType.substring(6));
            log.info("Image Subtype = " + processedImage.getSubType());
        }
    }


    /**
     *
     * Method determines if their are user wants to create a gif, then processes the users image(s) for return
     *
     * @param resizedImages
     * @param delay
     * @return
     * @throws IOException
     */
    public ByteArrayOutputStream createProcessedImage(List<BufferedImage> resizedImages, double delay) throws IOException {

        log.info("Begin createProcessedImage method");
        ByteArrayOutputStream imageData;

        //If gif
        if (resizedImages.size() > 1) {
            log.info("Multiple Images: Make Call To GifGenerator");

            GIFGenerator gen = new GIFGenerator();

            imageData = gen.generate(delay, resizedImages);

            //If img
        } else {

            log.info("Single Image: Write image data");
            BufferedImage image = resizedImages.get(0);
            ImageIO.write(image, processedImage.getSubType(), imageData = new ByteArrayOutputStream());
        }
        return imageData;
    }


    /**
     *
     * Method handles the actyual resizing of the image(s)
     *
     * @param urls
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    public List<BufferedImage> resizeImages(List<URL> urls, int width, int height) throws IOException {

        log.info("Begin Resizing Image(s)");

        List<BufferedImage> images = processImages(urls);
        List<BufferedImage> resizedImages = new ArrayList<BufferedImage>();


        /**
         *
         * TODO: BUG!!! If trying to resize larger than original image with only width or only height, image does not resize.
         *
         */
        if (width == 0) {
            for (BufferedImage image : images) {
                BufferedImage thumbnail = Thumbnails.of(image)
                        .size(image.getWidth(), height)
                        .keepAspectRatio(true)
                        .outputFormat(processedImage.getSubType())
                        .asBufferedImage();
                resizedImages.add(thumbnail);

            }

        } else if (height == 0){
            for (BufferedImage image : images) {
                BufferedImage thumbnail = Thumbnails.of(image)
                        .size(width, image.getHeight())
                        .keepAspectRatio(true)
                        .outputFormat(processedImage.getSubType())
                        .asBufferedImage();
                resizedImages.add(thumbnail);

            }

        } else {

            for (BufferedImage image : images) {
                BufferedImage thumbnail = Thumbnails.of(image)
                        .size(width, height)
                        .keepAspectRatio(false)
                        .outputFormat(processedImage.getSubType())
                        .asBufferedImage();
                resizedImages.add(thumbnail);

            }

        }

        processedImage.setResizedImages(resizedImages);

        return resizedImages;
    }


    /**
     *
     * method process urls into images
     *
     * @param urls
     * @return
     * @throws IOException
     */
    public List<BufferedImage> processImages(List<URL> urls) throws IOException {

        log.info("Begin Writing URL(s) To Buffered Images");

        List<BufferedImage> images = new ArrayList<BufferedImage>();

        for (URL url : urls) {
            images.add(ImageIO.read(url));
        }
        processedImage.setImagesReceived(images);

        return images;
    }


    /**
     *
     * method returns processedImage
     *
     * @return
     */
    public ProcessedImage getProcessedImage() {

        return processedImage;

    }
}



