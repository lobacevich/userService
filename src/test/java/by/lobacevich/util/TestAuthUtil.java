package by.lobacevich.util;

import by.lobacevich.security.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

public class TestAuthUtil {

    private TestAuthUtil() {
    }

    public static RequestPostProcessor user(Long userId, String role) {
        UserPrincipal principal = new UserPrincipal(userId);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        List.of(new SimpleGrantedAuthority(role))
                );

        return authentication(auth);
    }
}
