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
    String  message;
    String  subType;

    public ProcessedImage() {};


    public Boolean getSuccess() { return success; }

    public void setSuccess(Boolean success) { this.success = success; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }


}
