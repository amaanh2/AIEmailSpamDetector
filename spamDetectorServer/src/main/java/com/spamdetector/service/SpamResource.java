package com.spamdetector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spamdetector.domain.TestFile;
import com.spamdetector.util.SpamDetector;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import jakarta.ws.rs.core.Response;

@Path("/spam")
public class SpamResource {
    ObjectMapper objectMapper = new ObjectMapper();
    Response response = null;


    //    your SpamDetector Class responsible for all the SpamDetecting logic
    SpamDetector detector = new SpamDetector();


    SpamResource(){
//        TODO: load resources, train and test to improve performance on the endpoint calls\

        System.out.print("Training and testing the model, please wait");

//      TODO: call  this.trainAndTest();

        SpamDetector detector = new SpamDetector();
        File mainDirectory = new File("path/to/your/data"); // Make sure to provide the correct path
        detector.trainAndTest(mainDirectory);
        detector.generateJsonFile("path/to/your/output/testFiles.json"); // Specify the output path for testFiles.json
    }
    @GET
    @Produces("application/json")
    public Response getSpamResults() {
//       TODO: return the test results list of TestFile, return in a Response object

        try {
            response = Response.status(200)
                    .header("Content-Type", "application/json")
                    .entity(objectMapper.writeValueAsString(this.trainAndTest()))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    @GET
    @Path("/accuracy")
    @Produces("application/json")
    public Response getAccuracy() {
//      TODO: return the accuracy of the detector, return in a Response object

        return null;
    }

    @GET
    @Path("/precision")
    @Produces("application/json")
    public Response getPrecision() {
       //      TODO: return the precision of the detector, return in a Response object

        return null;
    }

    private List<TestFile> trainAndTest()  {
        if (this.detector==null){
            this.detector = new SpamDetector();
        }
        URL url = this.getClass().getClassLoader().getResource("data");
        File mainDirectory = null;
        try {
            mainDirectory = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
//        TODO: load the main directory "data" here from the Resources folder
        return this.detector.trainAndTest(mainDirectory);
    }
    public static void main(String[] args) {
        SpamDetector detector = new SpamDetector();
        File mainDirectory = new File("path/to/your/data"); // Make sure to provide the correct path
        detector.trainAndTest(mainDirectory);
        detector.generateJsonFile("path/to/your/output/testFiles.json"); // Specify the output path for testFiles.json
    }
}
