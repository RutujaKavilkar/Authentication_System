////package com.substring.auth.app.auth.entities;
////
////import jakarta.persistence.*;
////import lombok.*;
////import org.springframework.security.core.GrantedAuthority;
////import org.springframework.security.core.authority.SimpleGrantedAuthority;
////import org.springframework.security.core.userdetails.UserDetails;
////
////import java.time.Instant;
////import java.util.*;
////
////
////@Getter
////@Setter
////@AllArgsConstructor
////@NoArgsConstructor
////@Builder
////@Entity
////@Table(name = "users")
////public class User implements UserDetails {
////    @Id
////    @GeneratedValue(strategy = GenerationType.UUID)
////    @Column(name = "user_id")
////    private UUID id;
////    @Column(name = "user_email", unique = true, length = 300)
////    private String email;
////    @Column(name = "user_name", length = 500)
////    private String name;
////    private String password;
////    private String image;
////    private boolean enable = true;
////    private Instant createdAt = Instant.now();
////    private Instant updatedAt = Instant.now();
////
////
////
////
////    @Enumerated(EnumType.STRING)
////    private Provider provider = Provider.LOCAL;
////    private  String providerId;
////
////    @ManyToMany(fetch = FetchType.EAGER)
////    @JoinTable(name = "user_roles",
////            joinColumns = @JoinColumn(name = "user_id"),
////            inverseJoinColumns = @JoinColumn(name = "role_id"))
////    private Set<Role> roles = new HashSet<>();
////
////    @PrePersist
////    protected void onCreate() {
////        Instant now = Instant.now();
////        if (createdAt == null) createdAt = now;
////        updatedAt = now;
////    }
////
////    @PreUpdate
////    protected void onUpdate() {
////        updatedAt = Instant.now();
////    }
////
////    @Override
////    public Collection<? extends GrantedAuthority> getAuthorities() {
////        return roles
////                .stream()
////                .map(role -> new SimpleGrantedAuthority(role.getName()))
////                .toList();
////    }
////
////    @Override
////    public String getUsername() {
////        return this.email;
////    }
////
////    @Override
////    public boolean isAccountNonExpired() {
////        return true;
////    }
////
////    @Override
////    public boolean isAccountNonLocked() {
////        return true;
////    }
////
////    @Override
////    public boolean isCredentialsNonExpired() {
////        return true;
////    }
////
////    @Override
////    public boolean isEnabled() {
////        return this.enable;
////    }
////}
//
////
////package com.substring.auth.app.auth.entities;
////
////import jakarta.persistence.*;
////import lombok.*;
////import org.springframework.security.core.GrantedAuthority;
////import org.springframework.security.core.authority.SimpleGrantedAuthority;
////import org.springframework.security.core.userdetails.UserDetails;
////
////import java.time.Instant;
////import java.util.*;
////
////@Getter
////@Setter
////@AllArgsConstructor
////@NoArgsConstructor
////@Builder
////@Entity
////@Table(name = "users")
////public class User implements UserDetails {
////
////    @Id
////    @GeneratedValue(strategy = GenerationType.UUID)
////    @Column(name = "user_id")
////    private UUID id;
////
////    @Column(name = "user_email", unique = true, length = 300)
////    private String email;
////
////    @Column(name = "user_name", length = 500)
////    private String name;
////
////    private String password;
////    private String image;
////
////    private boolean enable = true;
////
////    private Instant createdAt;
////    private Instant updatedAt;
////
////    @Enumerated(EnumType.STRING)
////    private Provider provider = Provider.LOCAL;
////
////    private String providerId;
////
////    // ✅ FIXED: prevents NULL roles issue with Builder + ModelMapper
////    @Builder.Default
////    @ManyToMany(fetch = FetchType.EAGER)
////    @JoinTable(
////            name = "user_roles",
////            joinColumns = @JoinColumn(name = "user_id"),
////            inverseJoinColumns = @JoinColumn(name = "role_id")
////    )
//////    private Set<Role> roles = new HashSet<>();
////    @Builder.Default
////    @ManyToMany(fetch = FetchType.EAGER)
////    @JoinTable(
////            name = "user_roles",
////            joinColumns = @JoinColumn(name = "user_id"),
////            inverseJoinColumns = @JoinColumn(name = "role_id")
////    )
////    private Set<Role> roles = new HashSet<>();
////
////    @PrePersist
////    protected void onCreate() {
////        Instant now = Instant.now();
////        if (createdAt == null) createdAt = now;
////        updatedAt = now;
////    }
////
////    @PreUpdate
////    protected void onUpdate() {
////        updatedAt = Instant.now();
////    }
////
////    @Override
////    public Collection<? extends GrantedAuthority> getAuthorities() {
////        return roles.stream()
////                .map(role -> new SimpleGrantedAuthority(role.getName()))
////                .toList();
////    }
////
////    @Override
////    public String getUsername() {
////        return this.email;
////    }
////
////    @Override
////    public boolean isAccountNonExpired() {
////        return true;
////    }
////
////    @Override
////    public boolean isAccountNonLocked() {
////        return true;
////    }
////
////    @Override
////    public boolean isCredentialsNonExpired() {
////        return true;
////    }
////
////    @Override
////    public boolean isEnabled() {
////        return this.enable;
////    }
////}
//
////
////package com.substring.auth.app.auth.entities;
////
////import jakarta.persistence.*;
////import lombok.*;
////import org.springframework.security.core.GrantedAuthority;
////import org.springframework.security.core.authority.SimpleGrantedAuthority;
////import org.springframework.security.core.userdetails.UserDetails;
////
////import java.time.Instant;
////import java.util.Collection;
////import java.util.HashSet;
////import java.util.Set;
////import java.util.UUID;
////
////@Getter
////@Setter
////@NoArgsConstructor
////@AllArgsConstructor
////@Builder
////@Entity
////@Table(name = "users")
////public class User implements UserDetails {
////
////    @Id
////    @GeneratedValue(strategy = GenerationType.UUID)
////    @Column(name = "user_id")
////    private UUID id;
////
////    @Column(name = "user_email", unique = true, length = 300)
////    private String email;
////
////    @Column(name = "user_name", length = 500)
////    private String name;
////
////    private String password;
////    private String image;
////
////    private boolean enable = true;
////
////    private Instant createdAt;
////    private Instant updatedAt;
////
////    @Enumerated(EnumType.STRING)
////    private Provider provider = Provider.LOCAL;
////
////    private String providerId;
////
////    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
////    @JoinTable(
////            name = "user_roles",
////            joinColumns = @JoinColumn(name = "user_id"),
////            inverseJoinColumns = @JoinColumn(name = "role_id")
////    )
////    @Builder.Default
////    private Set<Role> roles = new HashSet<>();
////
////    @PrePersist
////    protected void onCreate() {
////        Instant now = Instant.now();
////        if (createdAt == null) createdAt = now;
////        updatedAt = now;
////    }
////
////    @PreUpdate
////    protected void onUpdate() {
////        updatedAt = Instant.now();
////    }
////
////    @Override
////    public Collection<? extends GrantedAuthority> getAuthorities() {
////        return roles.stream()
////                .map(role -> new SimpleGrantedAuthority(role.getName()))
////                .toList();
////    }
////
////    @Override
////    public String getUsername() {
////        return this.email;
////    }
////
////    @Override
////    public boolean isAccountNonExpired() {
////        return true;
////    }
////
////    @Override
////    public boolean isAccountNonLocked() {
////        return true;
////    }
////
////    @Override
////    public boolean isCredentialsNonExpired() {
////        return true;
////    }
////
////    @Override
////    public boolean isEnabled() {
////        return this.enable;
////    }
////}
//
//package com.substring.auth.app.auth.entities;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.*;
//
//@Entity
//@Data
//public class User implements UserDetails {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private UUID id;
//
//    private String email;
//    private String name;
//    private String password;
//    private String image;
//
//    private boolean enabled;
//
//    private String provider;
//
//    @ManyToMany(fetch = FetchType.EAGER)
//    private Set<Role> roles = new HashSet<>();
//
//    @Override
//    public String getUsername() {
//        return email;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return enabled;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() { return true; }
//
//    @Override
//    public boolean isAccountNonLocked() { return true; }
//
//    @Override
//    public boolean isCredentialsNonExpired() { return true; }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles.stream()
//                .map(r -> new SimpleGrantedAuthority(r.getName()))
//                .toList();
//    }
//}

//
//package com.substring.auth.app.auth.entities;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.time.Instant;
//import java.util.*;
//
//@Entity
//@Table(name = "users")
//@Data
//public class User implements UserDetails {
//
//    @Id
//    @Column(name = "user_id")
//    private UUID id;
//
//    @Column(name = "user_email", unique = true)
//    private String email;
//
//    @Column(name = "user_name")
//    private String name;
//
//    private String password;
//
//    private String image;
//
//    @Column(name = "enable")
//    private boolean enabled;
//
//    @Column(name = "created_at")
//    private Instant createdAt;
//
//    @Column(name = "updated_at")
//    private Instant updatedAt;
//
//    private String provider;
//
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "user_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Set<Role> roles = new HashSet<>();
//
//    @PrePersist
//    public void prePersist() {
//        this.id = UUID.randomUUID();
//        this.createdAt = Instant.now();
//        this.updatedAt = Instant.now();
//        this.enabled = true;
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        this.updatedAt = Instant.now();
//    }
//
//    @Override
//    public String getUsername() {
//        return email;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return enabled;
//    }
//
//    @Override public boolean isAccountNonExpired() { return true; }
//    @Override public boolean isAccountNonLocked() { return true; }
//    @Override public boolean isCredentialsNonExpired() { return true; }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles.stream()
//                .map(r -> new SimpleGrantedAuthority(r.getName()))
//                .toList();
//    }
//}

//
//package com.substring.auth.app.auth.entities;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import java.time.Instant;
//import java.util.*;
//
//@Entity
//@Table(name = "users")
//@Data
//public class User implements UserDetails {
//
//    @Id
//    @Column(name = "user_id")
//    private UUID id;
//
//    @Column(name = "user_email", unique = true)
//    private String email;
//
//    @Column(name = "user_name")
//    private String name;
//
//    private String password;
//    private String image;
//
//    @Column(name = "enable")
//    private boolean enable;
//
//    @Column(name = "created_at")
//    private Instant createdAt;
//
//    @Column(name = "updated_at")
//    private Instant updatedAt;
//
//    private String provider;
//
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "user_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Set<Role> roles = new HashSet<>();
//
//    @PrePersist
//    public void prePersist() {
//        this.id = UUID.randomUUID();
//        this.createdAt = Instant.now();
//        this.updatedAt = Instant.now();
//        this.enabled = true;
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        this.updatedAt = Instant.now();
//    }
//
////    @Override
////    public String getUsername() {
////        return email;
////    }
//
//    //private boolean enable;
//    @Override
//    public String getPassword() {
//        return this.password;
//    }
//
//    @Override
//    public String getUsername() {
//        return this.email;
//    }
//    @Override
//    public boolean isEnabled() {
//        return enable;
//    }
//    @Override public boolean isAccountNonExpired() { return true; }
//    @Override public boolean isAccountNonLocked() { return true; }
//    @Override public boolean isCredentialsNonExpired() { return true; }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles.stream()
//                .map(r -> new SimpleGrantedAuthority(r.getName()))
//                .toList();
//    }
//}

package com.substring.auth.app.auth.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "users")  // ✅ not "user"
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String image;

    // ✅ IMPORTANT FIX
    private boolean enabled;

    private Instant createdAt;
    private Instant updatedAt;
    private String providerId;

    @Enumerated(EnumType.STRING)
    private Provider provider;
    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
  //  private Set<Role> roles;
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
   // private Set<Role> roles = new HashSet<>();

    // ================= SECURITY =================



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}


