//////package com.substring.auth.app.auth.entities;
//////
//////import jakarta.persistence.Column;
//////import jakarta.persistence.Entity;
//////import jakarta.persistence.Id;
//////import jakarta.persistence.Table;
//////import lombok.*;
//////
//////import java.util.UUID;
//////
//////@Getter
//////@Setter
//////@AllArgsConstructor
//////@Builder
//////@NoArgsConstructor
//////@Entity
//////@Table(name = "roles")
//////public class Role {
//////    @Id
//////    private UUID id = UUID.randomUUID();
//////    @Column(unique = true, nullable = false)
//////    private String name;
//////}
////
////import jakarta.persistence.*;
////import lombok.Data;
////import java.util.UUID;
////@Data
////@Entity
////public class Role {
////
////    @Id
////    @GeneratedValue(strategy = GenerationType.UUID)
////    private UUID id;
////
////    private String name;
////}
////
////package com.substring.auth.app.auth.entities;
////
////import jakarta.persistence.*;
////import lombok.Data;
////
////import java.util.UUID;
////
////@Entity
////@Table(name = "roles")
////@Data
////public class Role {
////
////    @Id
////    private UUID id;
////
////    private String name;
////}
//
//
//package com.substring.auth.app.auth.entities;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import java.util.UUID;
//
//@Data
//@Entity
//public class Role {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private UUID id;
//
//    private String name;
//}

//package com.substring.auth.app.auth.entities;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.UUID;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Role {
//
//    @Id
//    private String name;
//    private UUID id;
//
//}

//package com.substring.auth.app.auth.entities;
//import java.util.*;
//import jakarta.persistence.*;
//import lombok.*;
//import org.springframework.security.core.GrantedAuthority;
//import java.util.UUID;
//@Entity
//@Table(name = "roles")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Role implements GrantedAuthority {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private UUID id;
//
//    private String name;
//
//    @Override
//    public String getAuthority() {
//        return name;
//    }
//}

package com.substring.auth.app.auth.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // ✅ FIX (important)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Override
    public String getAuthority() {
        return name; // should be: ADMIN / USER
    }

}