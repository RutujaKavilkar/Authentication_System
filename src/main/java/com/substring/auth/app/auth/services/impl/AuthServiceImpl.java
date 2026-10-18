//package com.substring.auth.app.auth.services.impl;

//import com.substring.auth.app.auth.payload.UserDto;
//import com.substring.auth.app.auth.services.AuthService;
//import com.substring.auth.app.auth.services.UserService;
//import lombok.AllArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@AllArgsConstructor
//public class AuthServiceImpl implements AuthService {
//
//    private final UserService userService;
//    private  final PasswordEncoder passwordEncoder;
//
//
//    @Override
//    public UserDto registerUser(UserDto userDto) {
//        //logic
//        //verify email
//        //verify password
//        //default roles
//        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
//
//
//        return userService.createUser(userDto);
//    }
//}



//
//
//package com.substring.auth.app.auth.services.impl;
//
//import com.substring.auth.app.auth.payload.UserDto;
//import com.substring.auth.app.auth.services.AuthService;
//import com.substring.auth.app.auth.services.UserService;
//import lombok.AllArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@AllArgsConstructor
//public class AuthServiceImpl implements AuthService {
//
//    private final UserService userService;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public UserDto registerUser(UserDto userDto) {
//
//        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
//        userDto.setEnabled(true);
//        userDto.setProvider("LOCAL");
//
//        return userService.createUser(userDto);
//    }
//}

//
//package com.substring.auth.app.auth.services.impl;
//
//import com.substring.auth.app.auth.payload.UserDto;
//import com.substring.auth.app.auth.services.AuthService;
//import com.substring.auth.app.auth.services.UserService;
//import lombok.AllArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@AllArgsConstructor
//public class AuthServiceImpl implements AuthService {
//
//    private final UserService userService;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public UserDto registerUser(UserDto userDto) {
//
//        // encode password
//        userDto.setPassword(
//                passwordEncoder.encode(userDto.getPassword())
//        );
//        userDto.setEnabled(true);
//        // create user
//        return userService.createUser(userDto);
//    }
//}


package com.substring.auth.app.auth.services.impl;

import com.substring.auth.app.auth.payload.UserDto;
import com.substring.auth.app.auth.services.AuthService;
import com.substring.auth.app.auth.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto registerUser(UserDto userDto) {

        userDto.setPassword(
                passwordEncoder.encode(userDto.getPassword())
        );

        userDto.setEnabled(true);

        return userService.createUser(userDto);
    }
}