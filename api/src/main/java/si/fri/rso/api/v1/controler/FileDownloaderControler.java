package si.fri.rso.api.v1.controler;
import si.fri.rso.services.FileDownloaderBean;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.UUID;

@ApplicationScoped
@Path("/file")
public class FileDownloaderControler {

    @Inject
    private FileDownloaderBean fileDownloaderBean;

    @Inject
    HttpServletRequest requestheader;

    @GET
    @Path("showInBrowser/{bucketname}/{filename}")
    @Produces("image/png")
    public Response showInBrowser(@PathParam("bucketname") String bucketName, @PathParam("filename") String fileName) {
        if (bucketName == null || fileName == null ){
            return Response.status(444, "File id od channel id was not given! ").build();
        }
        String requestHeader = requestheader.getHeader("uniqueRequestId");
        System.out.println("getting file: "+ fileName);
        requestHeader = requestHeader != null ? requestHeader : UUID.randomUUID().toString();
        String[] fileTypeArr = fileName.split("\\.");
        String fileType = fileTypeArr[fileTypeArr.length - 1].toLowerCase();

        InputStream fileObject = this.fileDownloaderBean.getFile(bucketName, fileName, requestHeader);

        System.out.println("filetype: " + fileType);

        Response.ResponseBuilder responseBuilder = Response.ok((Object) fileObject);

        switch (fileType) {
            case "jpg":
            case "png":
                System.out.println("IMAGE");
                responseBuilder.type("image/"+fileType);
                break;
            case "pdf":
                System.out.println("PDF");
                responseBuilder.type("application/pdf");
                break;
            default:
                System.out.println("OTHER");
                responseBuilder.type("text/plain");
        }
        responseBuilder.header("Content-Disposition", "filename="+fileName);
        return responseBuilder.build();
    }

    @GET
    @Path("download/{bucketname}/{filename}")
    public Response getFile(@PathParam("bucketname") String bucketName, @PathParam("filename") String fileName){
        System.out.println(bucketName);
        System.out.println(fileName);
        if (bucketName == null || fileName == null ){
            return Response.status(444, "File id od channel id was not given! ").build();
        }
        String requestHeader = requestheader.getHeader("uniqueRequestId");
        System.out.println("getting file: "+ fileName);
        requestHeader = requestHeader != null ? requestHeader : UUID.randomUUID().toString();

        Object fileObject = this.fileDownloaderBean.getFile(bucketName, fileName, requestHeader);

        if (fileObject == null) {
            Response.status(500).entity("file storage api not reachable").build();
        }

        Response.ResponseBuilder response = Response.ok(fileObject);
        response.header("Content-Disposition", "attachment; filename="+fileName);
        return response.build();

    }
}
