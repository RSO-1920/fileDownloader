package si.fri.rso.api.v1;

import com.kumuluz.ee.discovery.annotations.RegisterService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@RegisterService(value = "rso1920-fileDownloader")
@ApplicationPath("/v1")
public class fileDownloader extends Application{

}