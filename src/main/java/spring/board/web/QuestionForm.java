package spring.board.web;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class QuestionForm {

    private String title;
    private String contents;
    private List<MultipartFile> images;
}
