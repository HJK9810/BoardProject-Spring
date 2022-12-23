package spring.board.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String storeFiles(List<MultipartFile> multipartFiles) {
        if (multipartFiles == null) return "";

        return multipartFiles.stream()
                .filter(multipartFile -> !multipartFile.isEmpty()) // check not null
                .map(multipartFile -> // return string for "(input filename)storeFileName.fileType,"
                        String.format("(%s)%s,", inputFileName(multipartFile.getOriginalFilename()), storeFile(multipartFile)))
                .collect(Collectors.joining()); // join all strings
    }

    private String storeFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) return "";

        String storeFileName = createStoreFileName(multipartFile.getOriginalFilename());
        try {
            multipartFile.transferTo(new File(fileDir + storeFileName));
        } catch (IOException e) {
            log.error("error={}", e.toString()); // print exception
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
        if (pos == -1) return "";
        else return originalFilename.substring(pos + 1);
    }

    private String inputFileName(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        if (pos == -1) return originalFilename;
        else return originalFilename.substring(0, pos);
    }
}
