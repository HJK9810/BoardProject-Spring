package spring.board.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class QuestionForm {

    @NotNull
    @Schema(name = "title", description = "제목")
    private String title;
    @NotNull
    @Schema(name = "contents", description = "내용")
    private String contents;
    @Schema(name = "images", description = "이미지 파일들")
    private List<MultipartFile> images;
    private List<String> savedImages;
}
