package spring.board.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileDelete {
    @Value("${file.dir}")
    private String fileDir;

    private String getFullPath(String filename) {
        return fileDir + filename;
    }

    public void deleteFile(String savedNames, String modifyNames) {
        List<String> changedFile = checkChangedFile(savedNames, modifyNames);
        // file names = (사용자명)저장명.파일타입 => 필요한 이름 : ) 뒤의 전체
        if (!changedFile.isEmpty()) { // 변동사항 있을경우
            for (String file : changedFile) {
                String filename = separateFileName(file);

                File delFile = new File(getFullPath(filename));
                if (delFile.exists()) delFile.delete();
            }
        }
    }

    private List<String> checkChangedFile(String savedNames, String modifyNames) {
        List<String> changed = new ArrayList<>();
        String[] savedFiles = savedNames.split(",");

        for (String savedFile : savedFiles) { // 저장된 파일중에 삭제한것이 있는지 체크
            if (!modifyNames.contains(savedFile)) changed.add(savedFile);
        }

        return changed;
    }

    private String separateFileName(String fullFileName) {
        return fullFileName.substring(fullFileName.indexOf(")") + 1);
    }
}
