package spring.board.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileDelete {
    @Value("${file.dir}")
    private String fileDir;

    public void deleteFile(String savedFiles, String modifyFiles) {
        List<String> changedFile = checkChangedFile(savedFiles, modifyFiles);

        if (!changedFile.isEmpty()) { // 변동사항 있을경우
            for (String file : changedFile) {
                // file names = (사용자명)저장명.파일타입 => 필요한 이름 : ) 뒤의 전체
                String filename = separateFileName(file);
                // bring file for delete
                File delFile = new File(fileDir + filename);
                if (delFile.exists()) delFile.delete();
            }
        }
    }

    private List<String> checkChangedFile(String savedFiles, String modifyFiles) {
        // 저장된 파일중에 삭제한것이 있는지 체크
        return Arrays.stream(savedFiles.split(",")) // change array to stream which is savedFiles split by ","
                .filter(savedFile -> !modifyFiles.contains(savedFile)) // check modifyFiles contain savedFile
                .collect(Collectors.toList()); // change stream to list for return
    }

    private String separateFileName(String fullFileName) {
        return fullFileName.substring(fullFileName.indexOf(")") + 1);
    }
}
