package cl.set.markito.tests.MarkitoREST.tests.functional;
import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import cl.set.markito.MarkitoREST;
import cl.set.markito.tests.MarkitoREST.framework.*;
import cl.set.markito.tests.MarkitoREST.framework.types.*;
import io.restassured.response.Response;

public class MarsPhotosAPITests extends MarsApiTestBase {
	@Test( groups = { "SMOKE", "FUNCTIONAL" }, description = "TC-01-Retrieve 50 images of Curiosity on sol=1000 and download.")
	public void TC_01_RetrieveAndDownloadCuriositySOL1000()
	{
		// Arrange
		MarkitoREST marsPhotos = new MarkitoREST("https://mars-photos.herokuapp.com/", "api/v1/rovers/curiosity/photos");
		// Act
		Response response = marsPhotos.Get("sol=1000&page=1");
		// Assert
		Assert.assertEquals(response.getStatusCode(), 200);
		Page page = new Page();
		page = page.GetPageFromResponse(response); 
		List<String> files = page.DownloadPagePhotos(marsPhotos, page);
		for (String file : files) {
			println(file);
		}
	}

	@Test( groups = { "FUNCTIONAL" }, 
		   description = "TC-02-Retrieve 50 images of Curiosity on earth_date=2015-6-3.")
	public void TC_02_RetrieveAndDownloadCuriosityOnEarthTime()
	{
		// Arrange
		MarkitoREST marsPhotos = new MarkitoREST("https://mars-photos.herokuapp.com/", "api/v1/rovers/curiosity/photos");

		// Act
		Response response = marsPhotos.Get("earth_date=2015-6-3&page=1");
		// Assert
		Assert.assertEquals(response.getStatusCode(), 200);
		Page page = new Page();
		page = page.GetPageFromResponse(response); 
	}

	@Test( dataProvider = "CuriosityCameras", dataProviderClass = DataProviders.class, groups = { "FUNCTIONAL" }, 
	       description = "TC-03-For each camera Retrieve 50 images of Curiosity on sol=100.")
	public void TC_03_RetrieveImagesOfDifferentCuriosityCameras(String cameraName){
		// Arrange
		MarkitoREST marsPhotos = new MarkitoREST("https://mars-photos.herokuapp.com/", "api/v1/rovers/curiosity/photos");

		// Act
		Response response = marsPhotos.Get("sol=1000&camera="+cameraName+"&page=1");
		// Assert
		Assert.assertEquals(response.getStatusCode(), 200);
		Page page = new Page();
		page = page.GetPageFromResponse(response); 
	}

	@Test( groups = { "FUNCTIONAL" }, 
	       description = "TC-04-For each camera Retrieve all images on sol=100 and count.")
	public void TC_04_RetrieveAndCountAllImagesInADayOfDifferentCuriosityCameras(){
		// Arrange
		HashMap<String, Integer> imagesPerCamera = new HashMap<>();
		int sol=1000;
		String rover = "Curiosity";
		String curiosityCameras[] = new String[] {"FHAZ", "RHAZ", "MAST", "CHEMCAM", "MAHLI", "MARDI", "NAVCAM" };
		// Act
		SetDebugModeOFF();
		imagesPerCamera = CountImagesPerCameraBySol( sol, rover, curiosityCameras);
		SetDebugModeON();
		// Assert
		printf("Rover=%s Sol day=%s Images per camera=%s\n", rover, sol, imagesPerCamera);
	}
}
