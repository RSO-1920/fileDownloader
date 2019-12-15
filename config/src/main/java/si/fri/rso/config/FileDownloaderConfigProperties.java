package si.fri.rso.config;

import com.kumuluz.ee.configuration.cdi.ConfigValue;

public class FileDownloaderConfigProperties {
    // TODO: penca_uri
    @ConfigValue(value = "upload-file-storage-api-uri", watch = true)
    private  String fileStorageApiUri;

    public String getFileStorageApiUri() {
        return this.fileStorageApiUri;
    }
    public void setFileStorageApiUri(String fileStorageApiUri) {
        this.fileStorageApiUri = fileStorageApiUri;
    }

}
