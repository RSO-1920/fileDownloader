package si.fri.rso.services;


import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;
import java.io.*;
@RequestScoped
public class FileDownloaderBean {


    public File downloadFile(String FileId){
        // TODO: Kliči penca service

        String filePath = "./tect.txt";
        File f  = new File(filePath);
        System.out.println("I got file " + filePath );

        return f;
        //Response.ResponseBuilder response = Response.ok((Object) file);
        //response.header("Content-Disposition",
          //      "attachment; filename="+filePath);
        //return response.build();
        //return false;
    }
}
