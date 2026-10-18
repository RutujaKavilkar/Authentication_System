//package com.substring.auth.app.auth.payload;
//
//public record LoginRequest(
//        String email,
//        String password
//) {
//}

package com.substring.auth.app.auth.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String email;
    private String password;
}