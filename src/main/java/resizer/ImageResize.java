package resizer;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import entity.ProcessedImage;
import net.coobird.thumbnailator.*;



/**
 * Created by bmodahl on 3/7/17.
 */

@Path("/resizeImage")
public class ImageResize {

    @Context
    HttpHeaders header;
    @Context
    HttpServletResponse response;
    ProcessedImage processedImage = new ProcessedImage();

    @Path("/resizeImageJpeg")
    @GET
    @Produces("application/json")
    //TODO need to make url a array
    public Response resizeJpegImage(@QueryParam("urls") List<URL> urls,
                                    @QueryParam("width") Integer width,
                                    @QueryParam("height") Integer height) throws IOException {

        List<BufferedImage> resizedImages = new ArrayList<BufferedImage>();


        if (ValidateInput(urls)) {
            for (URL url : urls) {
                BufferedImage image = ImageIO.read(url);

                BufferedImage thumbNail = Thumbnails.of(image)
                        .size(width, height)
                        .asBufferedImage();

                resizedImages.add(thumbNail);
            }
            //TODO returns either gif or image object
            BufferedImage finalImage = CheckIfGif(resizedImages);
            byte[] imageData = CreateProcessedImage(finalImage);

            return Response.ok(new ByteArrayInputStream(imageData), new MediaType("image", "jpg")).build();

        } else {
            return Response.ok(processedImage, MediaType.APPLICATION_JSON).build();
        }

    }

    @Path("/resizeImageJpeg")
    @GET
    @Produces("application/json")
    //TODO need to make url a array
    public Response resizeJpegImage(@QueryParam("urls") List<URL> urls,
                                    @QueryParam("height") Integer height) throws IOException {

        List<BufferedImage> resizedImages = new ArrayList<BufferedImage>();


        if (ValidateInput(urls)) {
            for (URL url : urls) {
                BufferedImage image = ImageIO.read(url);

                //TODO find a better code bit
                BufferedImage thumbNail = Thumbnails.of(image)
                        .size(width, height)
                        .asBufferedImage();

                resizedImages.add(thumbNail);
            }
            //TODO returns either gif or image object
            BufferedImage finalImage = CheckIfGif(resizedImages);
            byte[] imageData = CreateProcessedImage(finalImage);

            return Response.ok(new ByteArrayInputStream(imageData), new MediaType("image", "jpg")).build();

        } else {
            return Response.ok(processedImage, MediaType.APPLICATION_JSON).build();
        }

    }

    @Path("/resizeImageJpeg")
    @GET
    @Produces("application/json")
    //TODO need to make url a array
    public Response resizeJpegImage(@QueryParam("urls") List<URL> urls,
                                    @QueryParam("width") Integer width) throws IOException {

        List<BufferedImage> resizedImages = new ArrayList<BufferedImage>();


        if (ValidateInput(urls)) {
            for (URL url : urls) {
                BufferedImage image = ImageIO.read(url);

                //TODO add unique resize code for each method
                //
                //
                //
                resizedImages.add(image);
            }
            //TODO returns either gif or image object
            BufferedImage finalImage = CheckIfGif(resizedImages);
            byte[] imageData = CreateProcessedImage(finalImage);

            return Response.ok(new ByteArrayInputStream(imageData), new MediaType("image", "jpg")).build();

        } else {
            return Response.ok(processedImage, MediaType.APPLICATION_JSON).build();
        }

    }

    public boolean ValidateInput(List<URL> urls) throws IOException {
        if (CheckURLS(urls)){
            if(CheckImage(urls)){
                return true;
            }
        }
        return false;
    }

    public boolean CheckURLS(List<URL> urls) throws IOException {
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

    public boolean CheckImage(List<URL> urls) throws IOException {
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

    public byte[] CreateProcessedImage(BufferedImage thumbNail) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ImageIO.write(thumbNail, "jpg", baos);

        byte[] imageData = baos.toByteArray();

        return imageData;
    }


}
