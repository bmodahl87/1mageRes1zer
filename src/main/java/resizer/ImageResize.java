package resizer;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.awt.image.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

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
    @Produces("images/jpg")
    public Response resizeJpegImage(@QueryParam("url") URL url,
                                    @QueryParam("width") Integer width,
                                    @QueryParam("height") Integer height) throws IOException {

        BufferedImage image = ImageIO.read(url);

        if (image != null) {

            BufferedImage thumbNail = Thumbnails.of(image)
                    .size(width, height)
                    .asBufferedImage();


            OutputStream out = response.getOutputStream();
            ImageIO.write(thumbNail, "jpg", out);
            out.close();

            return Response.ok().entity(thumbNail).build();
        } else {

            return Response.noContent().build();

        }
    }
}
