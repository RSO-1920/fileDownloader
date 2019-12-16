package si.fri.rso.services;


import com.kumuluz.ee.discovery.annotations.DiscoverService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.Optional;

@RequestScoped
public class FileDownloaderBean {
    @Inject
    @DiscoverService(value = "rso1920-fileStorage")
    private Optional<String> url;

    private Client httpClient;
    @PostConstruct
    private void init(){
        this.httpClient = ClientBuilder.newClient();
    }

    public InputStream getFile(String bucketName, String fileName, String requestheader){
        System.out.println(url);
        String Davidov_url;
        if (url.isPresent()) {
            Davidov_url = url.get();
        }
        else{
            return null;
        }
        String request = Davidov_url+"/v1/fileTransfer/"+bucketName+"/"+fileName;
        System.out.println(request);
        try{
            Response success = httpClient
                    .target(request)
                    .request(MediaType.MULTIPART_FORM_DATA_TYPE)
                    .header("uniqueRequestId", requestheader)
                    .get();
            if (success.getStatus() == 200){
                return success.readEntity(InputStream.class);
            }
            else{
                System.out.println("request status: " + success.getStatus());
                return null;
            }

        }catch (WebApplicationException | ProcessingException e) {
            // e.printStackTrace();
            System.out.println("api not reachable: " + request);
            return null;
        }
    }
}
