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

    /**
     *
     */
    public ProcessedImage() {}


    /**
     *
     * @return
     */
    public Boolean getSuccess() { return success; }

    /**
     *
     * @param success
     */
    public void setSuccess(Boolean success) { this.success = success; }

    /**
     *
     * @return
     */
    public String getMessage() { return message; }


    /**
     *
     * @param message
     */
    public void setMessage(String message) { this.message = message; }


    /**
     *
     * @return
     */
    public String getSubType() { return subType; }


    /**
     *
     * @param subType
     */
    public void setSubType(String subType) { this.subType = subType; }


    /**
     *
     * @return
     */
    public List<BufferedImage> getImagesReceived() {
        return imagesReceived;
    }


    /**
     *
     * @param imagesReceived
     */
    public void setImagesReceived(List<BufferedImage> imagesReceived) {
        this.imagesReceived = imagesReceived;
    }


    /**
     *
     * @return
     */
    public List<BufferedImage> getResizedImages() {
        return resizedImages;
    }


    /**
     * 
     * @param resizedImages
     */
    public void setResizedImages(List<BufferedImage> resizedImages) {
        this.resizedImages = resizedImages;
    }

}
