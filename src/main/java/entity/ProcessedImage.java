package entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.image.BufferedImage;

/**
 * Created by bmodahl on 3/21/17.
 */

@XmlRootElement(name = "processed_image")
public class ProcessedImage {


    Boolean success;
    byte[] image;

    public ProcessedImage() {};


    public Boolean getSuccess() { return success; }

    public void setSuccess(Boolean success) { this.success = success; }


    public byte[] getImage() { return image; }

    public void setImage(byte[] image) { this.image = image; }




}
