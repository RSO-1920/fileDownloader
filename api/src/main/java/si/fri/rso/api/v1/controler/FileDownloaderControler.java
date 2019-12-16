package si.fri.rso.api.v1.controler;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import si.fri.rso.services.FileDownloaderBean;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@Path("/download")
public class FileDownloaderControler {

    @Inject
    private FileDownloaderBean fileDownloaderBean;

    @Inject
    @DiscoverService(value = "rso1920-fileStorage")
    private Optional<String> url;

    private Client httpClient;
    @PostConstruct
    private void init(){
        this.httpClient = ClientBuilder.newClient();
    }

    @Inject
    HttpServletRequest requestheader;

    @GET
    @Path("test")
    public Response TestMe(){
        return Response.status(777).entity("Test succesfull").build();
    }


    @GET
    @Path("file")
    public Response getFile(@QueryParam("channelId") String channelId, @QueryParam("fileId") String fileId){
        if (fileId == null || channelId == null ){
            return Response.status(444, "File id od channel id was not given! ").build();
        }
        //File file = this.fileDownloaderBean.downloadFile(fileId);

        // TODO: Klic na pencata, da pove če je file_id veljaven
        System.out.println(url);
        String Davidov_url;
        if (url.isPresent()) {
            Davidov_url = url.get();
        }
        else{
            return Response.status(444, "File id od channel id was not given! ").build();
        }
        String requestHeader = requestheader.getHeader("uniqueRequestId");
        System.out.println("getting file: "+ fileId);
        requestHeader = requestHeader != null ? requestHeader : UUID.randomUUID().toString();

        String request = Davidov_url+"/v1/fileTransfer/"+channelId+"/"+fileId;
        System.out.println(request);
        String requestUniqueID = requestHeader;
        try{
            Response success = httpClient
                    .target(request)
                    .request(MediaType.MULTIPART_FORM_DATA_TYPE)
                    .header("uniqueRequestId", requestUniqueID)
                    .get();
            if (success.getStatus() == 200){
                Response.ResponseBuilder response = Response.ok((Object) success.getEntity());
                response.header("Content-Disposition", "attachment; filename="+fileId);
                return response.build();
                // return success;
            }
            else{
                return Response.status(success.getStatus(),
                        "Status of FileStorage is: "+ success.getStatus()).build();
            }


        }catch (WebApplicationException | ProcessingException e) {
            // e.printStackTrace();
            System.out.println("api not reachable: " + request);
            return Response.status(444, "File id od channel id was not given! ").build();
        }

        // iz multiparta dobi file
        //File file  = new File(fileId);
        //Response.ResponseBuilder response = Response.ok((Object) file);
        //response.header("Content-Disposition",
        //        "attachment; filename="+fileId);
        //return response.build();

    }
    /*
    TODO:
        download file

         1. dobiš path parameter
         2. kličeš penca.getfile
         3. if response = 200 and file is not null
         4. download file ((header parameter, attachment file)) -- google it
         5. response return file, status 200


     */
}
