package cl.set.markito.tests.MarkitoREST.framework.types;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Color;
import javax.imageio.ImageIO;

import org.testng.Assert;
import cl.set.markito.MarkitoREST;
import io.restassured.response.Response;

/**
 * This type represents a page with 25 images.
 */
public class Page {
    Photo photos[] = new Photo[25];

    public Photo[] getPhotos() {
        return photos;
    }

    public void setPhotos(Photo[] photos) {
        this.photos = photos;
    }
    /**
     * Deserialize a success response from API an get the content in a page object.
     * @param response
     * @return
     */
    public Page GetPageFromResponse(Response response) {
		Page page;
		var body = response.getBody();
		page = body.as(Page.class);
		return page;
	}
    /**
     * For a given endpoint and a page already get from API, download all images with filename <id>.jpeg.
     * @param marsPhotos: Endpoint
     * @param page
     * @return List of filenames downloaded in current folder.
     */
	public List<String> DownloadPagePhotos(MarkitoREST marsPhotos, Page page) {
        List<String> filenames = new ArrayList<String>();
		for( Photo photo : page.getPhotos()) {
			try {
                String filename = String.valueOf(photo.getId())+".jpg";;
				marsPhotos.DownloadFileFromUrl(filename, photo.getImg_src());
                filenames.add(filename);
			} catch (MalformedURLException e) {
				Assert.fail(e.getMessage());
			}
		}
        return filenames;
	}
    /**
     * Compares two images stored in files.
     * @param photo1Filename
     * @param photo2Filename
     * @throws Exception
     */
    public double CompareImages(String photo1Filename, String photo2Filename) throws Exception {
        double percentage = 0.0;
        BufferedImage img1 = ImageIO.read(new File(photo1Filename));
        BufferedImage img2 = ImageIO.read(new File(photo2Filename));
        int w1 = img1.getWidth();
        int w2 = img2.getWidth();
        int h1 = img1.getHeight();
        int h2 = img2.getHeight();
        if ((w1!=w2)||(h1!=h2)) {
            System.out.println("Difference: " + percentage);
            return percentage;
        } else {
           long diff = 0;
           for (int j = 0; j < h1; j++) {
              for (int i = 0; i < w1; i++) {
                 //Getting the RGB values of a pixel
                 int pixel1 = img1.getRGB(i, j);
                 Color color1 = new Color(pixel1, true);
                 int r1 = color1.getRed();
                 int g1 = color1.getGreen();
                 int b1 = color1.getBlue();
                 int pixel2 = img2.getRGB(i, j);
                 Color color2 = new Color(pixel2, true);
                 int r2 = color2.getRed();
                 int g2 = color2.getGreen();
                 int b2= color2.getBlue();
                 //sum of differences of RGB values of the two images
                 long data = Math.abs(r1-r2)+Math.abs(g1-g2)+ Math.abs(b1-b2);
                 diff = diff+data;
              }
           }
           double avg = diff/(w1*h1*3);
           percentage = (avg/255)*100;
           System.out.println("Difference: " + percentage);
           return percentage;
        }
    }
}
