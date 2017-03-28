package resizer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;

import java.util.Map;

import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;
import entity.ProcessedImage;
import net.coobird.thumbnailator.*;

import static com.sun.jersey.multipart.MultiPartMediaTypes.MULTIPART_MIXED_TYPE;
import static java.awt.SystemColor.info;


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
    public Response resizeJpegImage(@QueryParam("url") URL url,
                                    @QueryParam("width") Integer width,
                                    @QueryParam("height") Integer height) throws IOException {


        URLConnection connection = url.openConnection();

        String contentType = connection.getHeaderField("Content-Type");

        boolean isImage = contentType.startsWith("image/");

        ProcessedImage processedImage = new ProcessedImage();

        if (isImage) {

            BufferedImage image = ImageIO.read(url);

            if (image != null) {

                BufferedImage thumbNail = Thumbnails.of(image)
                        .size(width, height)
                        .asBufferedImage();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                ImageIO.write(thumbNail, "jpg", baos);

                byte[] imageData = baos.toByteArray();

                return Response.ok(
                        new ByteArrayInputStream(imageData),
                        new MediaType("image", "jpg"))
                        .build();

            } else {
                processedImage.setSuccess(false);
                processedImage.setMessage("Invalid Image!");
                return Response.ok(processedImage, MediaType.APPLICATION_JSON).build();
            }

            
        } else {

            processedImage.setSuccess(false);
            processedImage.setMessage("Invalid URL!");
            return Response.ok(processedImage, MediaType.APPLICATION_JSON).build();

        }
    }


}
