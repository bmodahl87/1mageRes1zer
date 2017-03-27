package entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by bmodahl on 3/21/17.
 */

@XmlRootElement(name = "processed_image")
public class ProcessedImage {


    Boolean success;
    List<byte[]> images;

    public ProcessedImage() {};


    public Boolean getSucess() { return success; }

    public void setSuccess(Boolean success) { this.success = success; }


    public byte[] getImage() { return images; }

    public void setImage(byte[] images) { this.images = images; }




}
