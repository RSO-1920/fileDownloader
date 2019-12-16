package si.fri.rso.api.v1;

import com.kumuluz.ee.discovery.annotations.RegisterService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@RegisterService(value = "rso1920-fileDownloader")
@ApplicationPath("/v1")
@OpenAPIDefinition(info = @Info(title = "File downloader and file visualizer REST api", version = "v1", contact = @Contact(), license = @License(),
        description = "Showing differet types of files except videos"), servers = @Server(url ="http://localhost:8084/v1"))
public class fileDownloader extends Application{
}