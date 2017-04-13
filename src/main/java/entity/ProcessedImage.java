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
    String  message;
    String  subType;
    List<BufferedImage> imagesReceived;
    List<BufferedImage> resizedImages;

    public ProcessedImage() {};


    public Boolean getSuccess() { return success; }

    public void setSuccess(Boolean success) { this.success = success; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getSubType() { return subType; }

    public void setSubType(String subType) { this.subType = subType; }

    public List<BufferedImage> getImagesReceived() {
        return imagesReceived;
    }

    public void setImagesReceived(List<BufferedImage> imagesReceived) {
        this.imagesReceived = imagesReceived;
    }

    public List<BufferedImage> getResizedImages() {
        return resizedImages;
    }

    public void setResizedImages(List<BufferedImage> resizedImages) {
        this.resizedImages = resizedImages;
    }

}
