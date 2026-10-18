//package com.substring.auth.app.auth.payload;
//
//public record TokenResponse(
//        String accessToken,
//        String refreshToken,
//        long expiresIn,
//        String tokenType,
//        UserDto user
//) {
//
//    public static TokenResponse of(String accessToken, String refreshToken, long expiresIn, UserDto user) {
//        return new TokenResponse(accessToken, refreshToken, expiresIn, "Bearer", user);
//    }
//
//}

package com.substring.auth.app.auth.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponse {

    private String accessToken;
    private String refreshToken;
    private long expiresIn;
    private UserDto user;

    public static TokenResponse of(String access, String refresh, long exp, UserDto user) {
        return TokenResponse.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .expiresIn(exp)
                .user(user)
                .build();
    }
}