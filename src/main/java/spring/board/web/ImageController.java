package spring.board.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Controller
public class ImageController {

    @Value("${file.dir}")
    private String fileDir;

    // image 반환
    @GetMapping(value = "/image/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> userSearch(@PathVariable("fileName") String fileName) {
        byte[] imageByteArray = null;
        try {
            InputStream imageStream = new FileInputStream(fileDir + fileName);
            imageByteArray = IOUtils.toByteArray(imageStream);
            imageStream.close();
        } catch (IOException e) {
            log.error("파일을 찾지 못했습니다.");
            log.error(e.toString());
        }

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }
}
