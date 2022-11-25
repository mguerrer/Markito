package cl.set.markito.tests.MarkitoREST.MarsPhotosAPIExample.framework;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.testng.*;
import org.testng.annotations.*;

import cl.set.markito.MarkitoBaseUtils;
import cl.set.markito.MarkitoREST;
import io.restassured.response.Response;;

/**
 * This class implements the actions to be performed after and before tests
 * execution.
 */
public class MarsApiTestBase extends MarkitoBaseUtils {
  @BeforeSuite(alwaysRun = true)
  public void beforeSuite(ITestContext context) {
    println("Executing suite " + context.getSuite().getName());
  }

  @BeforeClass(alwaysRun = true)
  public void beforeClass(ITestContext context) {
    println("Executing scenario " + context.getClass().getName());
  }

  @BeforeMethod(alwaysRun = true)
  public void beforeMethod(Method method) {
    println("\n\nExecuting test " + getClass().getSimpleName() + "-" + method.getName());
  }

  @AfterClass(alwaysRun = true)
  public void afterClass(ITestContext context) {
    println("Done. " + context.getClass().getName());
  }

  /**
   * Counts the number of images captured by a ROVER in a SOL day.
   * @param sol: Number of sol day. 
   * @param rover: Name of the rover.
   * @param curiosityCameras: An array with names of the cameras.
   * @return A hashmap with numbers of images for each camera name.
   */
	public HashMap<String, Integer> CountImagesPerCameraBySol(int sol, String rover, String[] curiosityCameras) {
		Response response;
		MarkitoREST marsPhotos = new MarkitoREST("https://mars-photos.herokuapp.com/", "api/v1/rovers/"+rover+"/photos");
    HashMap<String, Integer> imagesPerCamera = new HashMap<>();

		// Act
		for ( String camera:curiosityCameras) {
			int images;
			response = marsPhotos.Get("sol="+sol+"&camera="+camera);
			images = countMatches(response.getBody().asString(), rover);
			imagesPerCamera.put(camera, images);
		}
    return imagesPerCamera;
	}
  /* Checks if a string is empty ("") or null. */
  public static boolean isEmpty(String s) {
    return s == null || s.length() == 0;
  }

  /* Counts how many times the substring appears in the larger string. */
  public static int countMatches(String text, String str) {
    if (isEmpty(text) || isEmpty(str)) {
      return 0;
    }

    int index = 0, count = 0;
    while (true) {
      index = text.indexOf(str, index);
      if (index != -1) {
        count++;
        index += str.length();
      } else {
        break;
      }
    }

    return count;
  }
}
