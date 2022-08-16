/** Markito REST main class.
* Marcos Guerrero
* 04-08-2022
**/
package cl.set.markito;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import io.restassured.RestAssured;
import io.restassured.response.Response;


public class MarkitoREST extends MarkitoBaseUtils {

    public String host = "https://api.nasa.gov/";
    public String endPoint = "";
    public String apiKey = "DEMO_KEY"; // My key = "S65Y560FSh4EeEiFQGopU8htqFgMp7S9mc3IMW4D";
    public String endPointUrl = host + endPoint + "?api_key=" + apiKey;
    
    public MarkitoREST( String host, String endPoint){
        this.host =  host;
        this.endPoint = endPoint;
        this.endPointUrl = host + endPoint + "?api_key=" + apiKey;
    }
    public Response Get(String parameters) {
        Response response = GET(endPointUrl+"&"+parameters);
        return response;
    }
    /**
     * Perform Get from REST microservice.
     * @param url: Endpoint url.
     * @return Response obtained from call.
     */
    public Response GET(String url){
        println("GET URL="+url) ;
		Response response = RestAssured.get(url);
        printf("GET Status Code:%s\n",response.getStatusCode(), response.asString());
        return response;
    }
    public void DownloadFileFromUrl(String filename, String url) throws MalformedURLException {
        try {
            URL urlAux = new URL( url );
            InputStream in;
            in = new BufferedInputStream(urlAux.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1!=(n=in.read(buf)))
            {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();

            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(response);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getEndPoint() {
        return endPoint;
    }
    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
    public String getApiKey() {
        return apiKey;
    }
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    public String getEndPointUrl() {
        return endPointUrl;
    }
    public void setEndPointUrl(String endPointUrl) {
        this.endPointUrl = endPointUrl;
    }
}