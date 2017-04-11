package resizer;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.List;

import entity.ProcessedImage;
import net.coobird.thumbnailator.*;
import net.coobird.thumbnailator.geometry.Positions;


/**
 * Created by bmodahl & also Keith on 3/7/17.
 */

//TODO: make api endpoint the userdocs page (index)
@Path("/resizeImage")
public class ImageResize {

    ProcessedImage processedImage = new ProcessedImage();

    @Path("/resizeImageJpeg")
    @GET
    public Response resizeImage(@QueryParam("urls") List<URL> urls,
                                @QueryParam("width") int width,
                                @QueryParam("height") int height) throws IOException {

        return processRequest(urls, width, height, 1);

    }

    @Path("/resizeImageJpeg")
    @GET
    public Response resizeImage(@QueryParam("urls") List<URL> urls,
                                @QueryParam("width") int width,
                                @QueryParam("height") int height,
                                @QueryParam("delay") double delay) throws IOException {

        return processRequest(urls, width, height, delay);

    }

    @Path("/resizeImageJpeg")
    @GET
    public Response resizeImageWidthOnly(@QueryParam("urls") List<URL> urls,
                                         @QueryParam("width") int width) throws IOException {
        return processRequest(urls, 0, width, 1);

    }
    @Path("/resizeImageJpeg")
    @GET
    public Response resizeImageWidthOnly(@QueryParam("urls") List<URL> urls,
                                         @QueryParam("width") int width,
                                         @QueryParam("delay") double delay) throws IOException {
        return processRequest(urls, 0, width, delay);

    }

    @Path("/resizeImageJpeg")
    @GET
    public Response resizeImageHeightOnly(@QueryParam("urls") List<URL> urls,
                                          @QueryParam("height") int height) throws IOException {

        return processRequest(urls, height, 0, 1);

    }
    @Path("/resizeImageJpeg")
    @GET
    public Response resizeImageHeightOnly(@QueryParam("urls") List<URL> urls,
                                          @QueryParam("height") int height,
                                          @QueryParam("delay") double delay) throws IOException {

        return processRequest(urls, height, 0, delay);

    }


    public Response processRequest(List<URL> urls, int height, int width, double delay) throws IOException {

        List<BufferedImage> resizedImages;

        if (validateInput(urls)) {
            resizedImages = resizeImages(urls, height, width);
            byte[] imageData = createProcessedImage(resizedImages, delay).toByteArray();

            return Response.ok(new ByteArrayInputStream(imageData), new MediaType("image", "jpg")).build();

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
        for(URL url:urls) {
            URLConnection connection = url.openConnection();

            String contentType = connection.getHeaderField("Content-Type");

            boolean isImage = contentType.startsWith("image/");
            if(isImage == false){

                processedImage.setSuccess(false);
                processedImage.setMessage("Invalid URL!");
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

    public ByteArrayOutputStream createProcessedImage(List<BufferedImage> resizedImages, double delay) throws IOException {

        ByteArrayOutputStream imageData;

        //If gif
        if (resizedImages.size() > 1) {

            GIFGenerator gen = new GIFGenerator();

            imageData = gen.generate(delay, resizedImages);

        //If img
        } else {

            BufferedImage image = resizedImages.get(0);
            ImageIO.write(image, "jpg", imageData = new ByteArrayOutputStream());

        }

        return imageData;

    }

    public List<BufferedImage> resizeImages(List<URL> urls, int width, int height) throws IOException {

        List<BufferedImage> images = processImages(urls);


        if (width == 0) {
            for (BufferedImage image : images) {
                Thumbnails.of(image)
                        .size(1, height)
                        .asBufferedImage();
            }

        } else if (height == 0){
            for (BufferedImage image : images) {
                Thumbnails.of(image)
                        .size(width, 1)
                        .asBufferedImage();
            }

        } else {

            for (BufferedImage image : images) {
                Thumbnails.of(image)
                        .sourceRegion(Positions.CENTER, width, height)
                        .size(width, height)
                        .asBufferedImage();
            }


        }

        return images;
    }

    public List<BufferedImage> processImages(List<URL> urls) throws IOException {

        List<BufferedImage> images = new ArrayList<BufferedImage>();

        for (URL url : urls) {
            images.add(ImageIO.read(url));
        }

        return images;
    }

}