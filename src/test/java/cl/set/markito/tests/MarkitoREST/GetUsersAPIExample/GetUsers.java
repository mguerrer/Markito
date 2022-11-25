package cl.set.markito.tests.MarkitoREST.GetUsersAPIExample;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.testng.annotations.Test;

import cl.set.markito.MarkitoREST;
import io.restassured.response.Response;

public class GetUsers  {
    @Test( description = "Get users and assses specific name=Tobias.")
    public void GetUsersList(){
    // Arrange
    MarkitoREST users = new MarkitoREST("https://reqres.in/","api/users?page=2");

        Response response = users.GET("https://reqres.in/api/users?page=2");
        assertEquals(200, response.getStatusCode());
        var body = response.getBody();
        response.prettyPrint();
		UsersPage page = new UsersPage();
        page = body.as(UsersPage.class);
        System.out.println(page.toString());
        Boolean found = false;
        for (int i=0; i<6; i++) {
            if ( page.data.get(i).first_name.equals("Tobias") )
                found = true;
        }
        assertTrue( found );

    }
}