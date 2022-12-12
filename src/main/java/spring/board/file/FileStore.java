package spring.board.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public String storeFiles(List<MultipartFile> multipartFiles) {
        StringBuffer storeFileResult = new StringBuffer();

        if (multipartFiles != null){
            for (MultipartFile multipartFile : multipartFiles) {
                if (!multipartFile.isEmpty()) storeFileResult.append(storeFile(multipartFile) + ",");
            }
        }
        return storeFileResult.toString();
    }

    private String storeFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) return "";

        String storeFileName = createStoreFileName(multipartFile.getOriginalFilename());
        try {
            multipartFile.transferTo(new File(getFullPath(storeFileName)));
        } catch (IOException e) {
            log.error("error={}", e.getMessage());
        }

        return storeFileName;
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
