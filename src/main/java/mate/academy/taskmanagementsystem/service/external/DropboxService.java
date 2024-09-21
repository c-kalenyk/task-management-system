package mate.academy.taskmanagementsystem.service.external;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.InputStream;
import org.springframework.stereotype.Component;

@Component
public class DropboxService {
    private static final String APP_NAME = "task-management-system-of-c-kalenyk";
    private final DbxClientV2 client;

    public DropboxService(Dotenv dotenv) {
        String accessToken = dotenv.get("DROPBOX_API_TOKEN");
        DbxRequestConfig config = DbxRequestConfig.newBuilder(APP_NAME).build();
        client = new DbxClientV2(config, accessToken);
    }

    public String uploadFile(String filename, InputStream inputStream) throws Exception {
        FileMetadata metadata = client.files().uploadBuilder("/" + filename)
                .withMode(WriteMode.OVERWRITE)
                .uploadAndFinish(inputStream);
        return metadata.getId();
    }

    public String getFileLink(String dropboxFileId) throws Exception {
        return client.sharing().createSharedLinkWithSettings(dropboxFileId).getUrl();
    }
}
