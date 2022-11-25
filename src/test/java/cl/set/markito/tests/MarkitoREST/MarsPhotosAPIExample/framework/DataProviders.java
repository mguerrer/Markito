package cl.set.markito.tests.MarkitoREST.MarsPhotosAPIExample.framework;
import org.testng.annotations.DataProvider;

public class DataProviders {
    @DataProvider(name = "MarsPhotosTestData" )
    public Object[][] MarsPhotosTestData() {
        return new Object[][] { {"sol=1000&page=1"}};
    }
    @DataProvider(name = "CuriosityCameras" )
    public Object[][] CuriosityCameras() {
        return new Object[][] { {"FHAZ"}, {"RHAZ"},  {"MAST"}, {"CHEMCAM"}, {"MAHLI"}, {"MARDI"}, {"NAVCAM"} };
    }
}