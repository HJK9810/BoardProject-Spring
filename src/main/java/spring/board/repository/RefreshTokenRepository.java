package spring.board.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import spring.board.exception.ApiExceptions;
import spring.board.exception.ErrorCode;
import spring.board.web.dto.RefreshToken;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class RefreshTokenRepository {

    private static final Map<String, RefreshToken> store = new HashMap<>();

    public RefreshToken save(RefreshToken token) {
        store.put(token.getKey(), token);
        return token;
    }

    public RefreshToken findByKey(String key) {
        return store.get(key);
    }

    public void deleteRefreshToken(String key) {
        try {
            store.remove(key);
        } catch (Exception e) {
            throw new ApiExceptions(ErrorCode.UNAUTHORIZED_MEMBER);
        }
    }
}
