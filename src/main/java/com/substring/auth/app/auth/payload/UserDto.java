//package com.substring.auth.app.auth.payload;
//
//import com.substring.auth.app.auth.entities.Provider;
//import lombok.*;
//
//import java.time.Instant;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.UUID;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class UserDto {
//
//    private UUID id;
//    private String email;
//    private String name;
//    private String password;
//    private String image;
//    private boolean enable = true;
//    private Instant createdAt = Instant.now();
//    private Instant updatedAt = Instant.now();
//    private Provider provider = Provider.LOCAL;
//    private Set<RoleDto> roles = new HashSet<>();
//}
//
//package com.substring.auth.app.auth.payload;
//import java.util.*;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.substring.auth.app.auth.entities.Provider;
//import com.substring.auth.app.auth.entities.Role;
//import lombok.*;
//
//import java.time.Instant;
//import java.util.Set;
//import java.util.UUID;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class UserDto {
//
//    private UUID id;
//    private String email;
//    private String name;
//
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    private String password;
//
//    private String image;
//    private boolean enable = true;
//    private Instant createdAt = Instant.now();
//    private Instant updatedAt = Instant.now();
//    private Provider provider = Provider.LOCAL;
//    private Set<RoleDto> roles = new HashSet<>();
//}
//
//package com.substring.auth.app.auth.payload;
//
//import com.substring.auth.app.auth.entities.Provider;
//import lombok.*;
//
//        import java.time.Instant;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.UUID;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class UserDto {
//
//    private UUID id;
//    private String email;
//    private String name;
//    private String password;
//    private String image;
//    private boolean enable = true;
//    private Instant createdAt = Instant.now();
//    private Instant updatedAt = Instant.now();
//    private Provider provider = Provider.LOCAL;
//    private Set<RoleDto> roles = new HashSet<>();
//}

//package com.substring.auth.app.auth.payload;
//
//import lombok.Data;
//
//@Data
//public class UserDto {
//    private String id;
//    private String email;
//    private String name;
//    private String password;
//    private String image;
//    private boolean enable;
//    private String provider;
//}

//package com.substring.auth.app.auth.payload;
//
//import lombok.*;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class UserDto {
//
//    private String name;
//    private String email;
//    private String password;
//    private String image;
//    private boolean enable;
//}

//
//package com.substring.auth.app.auth.payload;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.*;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class UserDto {
//
//    private String id;
//    @NotBlank
//    private String name;
//    @Email
//    private String email;
//
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    @Size(min = 6)
//    private String password;
//    private String image;
//
//    // ✅ ADD THIS
//    private boolean enabled;
//}

package com.substring.auth.app.auth.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String id;

    @NotBlank
    private String name;

    @Email
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 6)
    private String password;

    private String image;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean enabled;
}