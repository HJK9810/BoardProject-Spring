package spring.board.web;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import spring.board.exception.ApiExceptions;
import spring.board.exception.ErrorCode;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
            throw new ApiExceptions(ErrorCode.FILE_NOT_FOUND);
        }

        return ResponseEntity.ok(imageByteArray);
    }
}
