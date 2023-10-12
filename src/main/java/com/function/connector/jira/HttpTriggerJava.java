package com.function.connector.jira;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL; 


/**
 * Azure Functions with HTTP Trigger.
 */
public class HttpTriggerJava {
    /**
     * This function listens at endpoint "/api/HttpTriggerJava". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpTriggerJava
     * 2. curl {your host}/api/HttpTriggerJava?name=HTTP%20Query
     */
    @FunctionName("HttpTriggerJava")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        //String query = request.getQueryParameters().get("name");
        //String name = request.getBody().orElse(query);

        StringBuffer content = new StringBuffer();;

        try {
            URL url = new URL("https://codefest-poc.atlassian.net/rest/api/2/search?jql=status%20%3D%20Done%20and%20resolutionDate%3EstartOfWeek()");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Basic dGFwaXNoYkBnbWFpbC5jb206QVRBVFQzeEZmR0YwREFOeW5vZ0dDbkNkdTVjakFkRVJHVzQwVUxBaG1HUlN2SE1ULVpJTnF3RXY0LUJ4Z245OVl1cW43X1hmSlVTY3l4cU9COW92VVd2cHBtNkpBUUpQZ19LOThudUxlUTFPdUxjQlBuSW45VGg2RXFSOUdIRWtjLUIya2JWQVQ3RXF1b0N5R2NqRkt3Y0ZkM0dYVTRlQlY2eVZKTjZsLW5aZUpwMWFvUWtnaUNvPTg1QTRBMkNE");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            int status = con.getResponseCode();

            BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);

                con.disconnect();
            }
            in.close();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return request.createResponseBuilder(HttpStatus.OK).body(content).build();

        /*if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            //return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
            return request.createResponseBuilder(HttpStatus.OK).body(content).build();
        }*/
    }
}
