package si.fri.rso.api.v1.controler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
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
    @Operation(description = "File display", summary = "displaying files in browser", tags = "file, in, browser", responses = {
            @ApiResponse(responseCode = "200",
                    description = "show file",
                    content = @Content( schema = @Schema(implementation = InputStream.class))
            ),
            @ApiResponse(responseCode = "500",
                    description = "error getting file",
                    content = @Content( schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(responseCode = "444",
                    description = "file params not given",
                    content = @Content( schema = @Schema(implementation = String.class))
            ),
    })
    @Metered(name = "file__show_metered")
    @Timed(name = "file_show_times")
    @Counted(name = "file_show_header")
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

        if (fileObject == null) {
            return Response.status(500).entity("file storage api not reachable").build();
        }

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
    @Operation(description = "File download", summary = "download file to user machine", tags = "file, download", responses = {
            @ApiResponse(responseCode = "200",
                    description = "file download",
                    content = @Content( schema = @Schema(implementation = InputStream.class))
            ),
            @ApiResponse(responseCode = "500",
                    description = "error getting file",
                    content = @Content( schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(responseCode = "444",
                    description = "file params not given",
                    content = @Content( schema = @Schema(implementation = String.class))
            ),
    })
    @Metered(name = "file__download_metered")
    @Timed(name = "file_download_times")
    @Counted(name = "file_download_header")
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
            return Response.status(500).entity("file storage api not reachable").build();
        }

        Response.ResponseBuilder response = Response.ok(fileObject);
        response.header("Content-Disposition", "attachment; filename="+fileName);
        return response.build();

    }
}
