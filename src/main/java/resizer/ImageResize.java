package resizer;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
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

    @Path("/resizeImageJpeg")
    @GET
    @Produces("application/json")
    //TODO need to make url a array
    public Response resizeJpegImage(@QueryParam("urls") List<URL> urls,
                                    //@QueryParam("tag") List<String> tags,
                                    @QueryParam("width") Integer width,
                                    @QueryParam("height") Integer height) throws IOException {

        //For misc
        ValidateInput();

        List<URL> resizedImages = new ArrayList<URL>();

        for(URL url:urls) {
            //TODO checkifphotos will return a string "allPics" set to true or false and an image
            CheckIfPhotos(url);

            if (allPics == "true"){
                ConvertToImage(url);
                //TODO Resize will return image
                Resize(image, width, height, tags);
                //TODO AddResizedPhotos to resizedArray
                resizedImages.add(image);
            }
        }
        //TODO returns list with either gif or images "finalImages" list
        CheckIfGif(resizedImages, tags);

        //Returns list of byte arrays
        imageList = PrepareForResponce(finalImages);

        ProccessedImage.setImage(imageList);

        CreateProcessedImage();


        BufferedImage image = ImageIO.read(url);

        if (image != null) {

            BufferedImage thumbNail = Thumbnails.of(image)
                    .size(width, height)
                    .asBufferedImage();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(image, "jpg", baos);

            byte[] imageData = baos.toByteArray();

            ProcessedImage processedImage = new ProcessedImage();

            processedImage.setSuccess(true);

            processedImage.setImage(imageData);

            return Response.ok(processedImage).build();
        } else {

            ProcessedImage processedImage = new ProcessedImage();

            processedImage.setSuccess(false);

            processedImage.setImage(null);

            return Response.ok(processedImage).build();

        }
    }


}
