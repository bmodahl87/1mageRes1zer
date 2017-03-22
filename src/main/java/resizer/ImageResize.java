package resizer;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;

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
    public Response resizeJpegImage(@QueryParam("url") URL url,
                                    @QueryParam("width") Integer width,
                                    @QueryParam("height") Integer height) throws IOException {

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
