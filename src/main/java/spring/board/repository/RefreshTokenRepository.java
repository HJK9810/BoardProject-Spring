package spring.board.repository;

import org.springframework.stereotype.Repository;
import spring.board.web.dto.RefreshToken;

import java.util.HashMap;
import java.util.Map;

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
        store.remove(key);
    }
}
