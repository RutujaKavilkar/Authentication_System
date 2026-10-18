//package com.substring.auth.app.auth.services.impl;
//import java.util.*;
//import com.substring.auth.app.auth.config.AppConstants;
//import com.substring.auth.app.auth.payload.UserDto;
//import com.substring.auth.app.auth.entities.Provider;
//import com.substring.auth.app.auth.entities.Role;
//import com.substring.auth.app.auth.entities.User;
//import com.substring.auth.app.exceptions.ResourceNotFoundException;
//import com.substring.auth.app.auth.helpers.UserHelper;
//import com.substring.auth.app.auth.repositories.RoleRepository;
//import com.substring.auth.app.auth.repositories.UserRepository;
//import com.substring.auth.app.auth.services.UserService;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class UserServiceImpl implements UserService {
//
//
//    private final UserRepository userRepository;
//    private final ModelMapper modelMapper;
//
//    private final RoleRepository roleRepository;
//
//    @Override
//    @Transactional
//    public UserDto createUser(UserDto userDto) {
//        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
//            throw new IllegalArgumentException("Email is required");
//        }
//        if (userRepository.existsByEmail(userDto.getEmail())) {
//            throw new IllegalArgumentException("User with given email already exists");
//        }
//        // if you have extra checks __put here...
//        User user = modelMapper.map(userDto, User.class);
//        user.setProvider(userDto.getProvider() != null ? userDto.getProvider() : Provider.LOCAL);
//        //role assign here to user___for authorization
//
//        //TODO:
//        //assign the default role
//
////        Role role = roleRepository.findByName("ROLE_" + AppConstants.GUEST_ROLE).orElse(null);
////        user.getRoles().add(role);
//
////        Role role = roleRepository.findByName("ROLE_" + AppConstants.GUEST_ROLE)
////                .orElseThrow(() -> new RuntimeException("Default role ROLE_GUEST not found in DB"));
////
////        if (user.getRoles() == null) {
////            user.setRoles(new HashSet<>());
////        }
////
////        user.getRoles().add(role);
//        Role role = roleRepository.findByName("ROLE_" + AppConstants.GUEST_ROLE)
//                .orElseThrow(() -> new RuntimeException("Role not found"));
//
//        user.getRoles().add(role);
//
//        User savedUser = userRepository.save(user);
//        return modelMapper.map(savedUser, UserDto.class);
//    }
//
//    @Override
//    public UserDto getUserByEmail(String email) {
//
//        User user = userRepository
//                .findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with given email id "));
//        return modelMapper.map(user, UserDto.class);
//    }
//
//    @Override
//    public UserDto updateUser(UserDto userDto, String userId) {
//        UUID uId = UserHelper.parseUUID(userId);
//        User existingUser = userRepository
//                .findById(uId)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
//        //we are not going to change email id for this project.
//        if (userDto.getName() != null) existingUser.setName(userDto.getName());
//        if (userDto.getImage() != null) existingUser.setImage(userDto.getImage());
//        if (userDto.getProvider() != null) existingUser.setProvider(userDto.getProvider());
//        //TODO: change password updation logic...
//        if (userDto.getPassword() != null) existingUser.setPassword(userDto.getPassword());
//        existingUser.setEnable(userDto.isEnable());
//        existingUser.setUpdatedAt(Instant.now());
//        User updatedUser = userRepository.save(existingUser);
//        return modelMapper.map(updatedUser, UserDto.class);
//    }
//
//    @Override
//    public void deleteUser(String userId) {
//        UUID uId = UserHelper.parseUUID(userId);
//        User user = userRepository.findById(uId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
//        userRepository.delete(user);
//    }
//
//    @Override
//    public UserDto getUserById(String userId) {
//        User user = userRepository.findById(UserHelper.parseUUID(userId)).orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
//        return modelMapper.map(user, UserDto.class);
//    }
//
//    @Override
//    @Transactional
//    public Iterable<UserDto> getAllUsers() {
//        return userRepository
//                .findAll()
//                .stream()
//                .map(user -> modelMapper.map(user, UserDto.class))
//                .toList();
////    }
////}
//
//
//@Override
//@Transactional
//public UserDto createUser(UserDto userDto) {
//
//    if (userRepository.existsByEmail(userDto.getEmail())) {
//        throw new IllegalArgumentException("User already exists");
//    }
//
//    User user = modelMapper.map(userDto, User.class);
//
//    user.setProvider(
//            userDto.getProvider() != null ? userDto.getProvider() : "LOCAL"
//    );
//
//    Role role = roleRepository.findByName("ROLE_GUEST")
//            .orElseThrow(() -> new RuntimeException("Role not found"));
//
//    user.getRoles().add(role);
//
//    User saved = userRepository.save(user);
//
//    return modelMapper.map(saved, UserDto.class);
//}

//
//package com.substring.auth.app.auth.services.impl;
//
//import com.substring.auth.app.auth.config.AppConstants;
//import com.substring.auth.app.auth.payload.UserDto;
//import com.substring.auth.app.auth.entities.Role;
//import com.substring.auth.app.auth.entities.User;
//import com.substring.auth.app.exceptions.ResourceNotFoundException;
//import com.substring.auth.app.auth.helpers.UserHelper;
//import com.substring.auth.app.auth.repositories.RoleRepository;
//import com.substring.auth.app.auth.repositories.UserRepository;
//import com.substring.auth.app.auth.services.UserService;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class UserServiceImpl implements UserService {
//
//    private final UserRepository userRepository;
//    private final ModelMapper modelMapper;
//    private final RoleRepository roleRepository;
//
//    @Override
//    @Transactional
//    public UserDto createUser(UserDto userDto) {
//
//        if (userRepository.existsByEmail(userDto.getEmail())) {
//            throw new IllegalArgumentException("User already exists");
//        }
//
//        User user = modelMapper.map(userDto, User.class);
//
//        user.setProvider(
//                userDto.getProvider() != null ? userDto.getProvider() : "LOCAL"
//        );
//
//        Role role = roleRepository.findByName("ROLE_" + AppConstants.GUEST_ROLE)
//                .orElseThrow(() -> new RuntimeException("Role not found"));
//
//        user.getRoles().add(role);
//
//        User saved = userRepository.save(user);
//
//        return modelMapper.map(saved, UserDto.class);
//    }
//
//    @Override
//    public UserDto getUserByEmail(String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//        return modelMapper.map(user, UserDto.class);
//    }
//
//    @Override
//    public UserDto updateUser(UserDto userDto, String userId) {
//
//        UUID id = UserHelper.parseUUID(userId);
//
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        if (userDto.getName() != null) user.setName(userDto.getName());
//        if (userDto.getImage() != null) user.setImage(userDto.getImage());
//        if (userDto.getProvider() != null) user.setProvider(userDto.getProvider());
//        if (userDto.getPassword() != null) user.setPassword(userDto.getPassword());
//
//        user.setEnabled(userDto.isEnable());
//
//        User updated = userRepository.save(user);
//
//        return modelMapper.map(updated, UserDto.class);
//    }
//
//    @Override
//    public void deleteUser(String userId) {
//
//        UUID id = UserHelper.parseUUID(userId);
//
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        userRepository.delete(user);
//    }
//
//    @Override
//    public UserDto getUserById(String userId) {
//
//        User user = userRepository.findById(UserHelper.parseUUID(userId))
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        return modelMapper.map(user, UserDto.class);
//    }
//
//    @Override
//    public Iterable<UserDto> getAllUsers() {
//        return userRepository.findAll()
//                .stream()
//                .map(user -> modelMapper.map(user, UserDto.class))
//                .toList();
//    }
//}

//
//package com.substring.auth.app.auth.services.impl;
//import com.substring.auth.app.auth.entities.Provider;
//import com.substring.auth.app.auth.config.AppConstants;
//import com.substring.auth.app.auth.payload.UserDto;
//import com.substring.auth.app.auth.entities.Role;
//import com.substring.auth.app.auth.entities.User;
//import com.substring.auth.app.exceptions.ResourceNotFoundException;
//import com.substring.auth.app.auth.helpers.UserHelper;
//import com.substring.auth.app.auth.repositories.RoleRepository;
//import com.substring.auth.app.auth.repositories.UserRepository;
//import com.substring.auth.app.auth.services.UserService;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//import java.util.HashSet;
//
//@Service
//@RequiredArgsConstructor
//public class UserServiceImpl implements UserService {
//
//    private final UserRepository userRepository;
//    private final ModelMapper modelMapper;
//    private final RoleRepository roleRepository;
//
//    @Override
//    @Transactional
//    public UserDto createUser(UserDto userDto) {
//
//        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
//            throw new IllegalArgumentException("Email is required");
//        }
//
//        if (userRepository.existsByEmail(userDto.getEmail())) {
//            throw new IllegalArgumentException("User already exists with this email");
//        }
//
//        User user = modelMapper.map(userDto, User.class);
//
//        // DEFAULT VALUES
//        user.setEnabled(true);
//        user.setProvider(Provider.LOCAL);
//
//        // ASSIGN DEFAULT ROLE
////        Role role = roleRepository.findByName("ROLE_" + AppConstants.GUEST_ROLE)
////                .orElseThrow(() -> new RuntimeException("ROLE_GUEST not found in DB"));
////
////        user.getRoles().add(role);
//        Role role = roleRepository.findByName("ROLE_" + AppConstants.GUEST_ROLE)
//                .orElseThrow(() -> new RuntimeException("ROLE_GUEST not found in DB"));
//
//// ✅ FIX: initialize roles before adding
//        if (user.getRoles() == null) {
//            user.setRoles(new HashSet<>());
//        }
//
//        //user.getRoles().add(role);
//        User user = userMapper.toEntity(userDto);
//        User savedUser = userRepository.save(user);
//
////        return modelMapper.map(savedUser, UserDto.class);
//        return userMapper.toDto(savedUser);
//    }
//
//    @Override
//    public UserDto getUserByEmail(String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        return modelMapper.map(user, UserDto.class);
//    }
//
//    @Override
//    public UserDto updateUser(UserDto userDto, String userId) {
//
//        UUID id = UserHelper.parseUUID(userId);
//
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//
//        if (userDto.getName() != null)
//            user.setName(userDto.getName());
//
//        if (userDto.getImage() != null)
//            user.setImage(userDto.getImage());
//
//        if (userDto.getPassword() != null)
//            user.setPassword(userDto.getPassword());
//
//        user.setEnabled(userDto.isEnabled());
//
//        User updatedUser = userRepository.save(user);
//
//        return modelMapper.map(updatedUser, UserDto.class);
//    }
//
//    @Override
//    public void deleteUser(String userId) {
//
//        UUID id = UserHelper.parseUUID(userId);
//
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        userRepository.delete(user);
//    }
//
//    @Override
//    public UserDto getUserById(String userId) {
//
//        UUID id = UserHelper.parseUUID(userId);
//
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        return modelMapper.map(user, UserDto.class);
//    }
//
//
//    @Override
//    public Iterable<UserDto> getAllUsers() {
//        return userRepository.findAll()
//                .stream()
//                .map(user -> modelMapper.map(user, UserDto.class))
//                .toList();
//    }
//}

//
//package com.substring.auth.app.auth.services.impl;
//
//import com.substring.auth.app.auth.entities.Provider;
//import com.substring.auth.app.auth.config.AppConstants;
//import com.substring.auth.app.auth.payload.UserDto;
//import com.substring.auth.app.auth.entities.Role;
//import com.substring.auth.app.auth.entities.User;
//import com.substring.auth.app.exceptions.ResourceNotFoundException;
//import com.substring.auth.app.auth.helpers.UserHelper;
//import com.substring.auth.app.auth.repositories.RoleRepository;
//import com.substring.auth.app.auth.repositories.UserRepository;
//import com.substring.auth.app.auth.services.UserService;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//import java.util.HashSet;
//
//@Service
//@RequiredArgsConstructor
//public class UserServiceImpl implements UserService {
//
//    private final UserRepository userRepository;
//    private final ModelMapper modelMapper;
//    private final RoleRepository roleRepository;
//
//    @Override
//    @Transactional
//    public UserDto createUser(UserDto userDto) {
//
//        // ✅ validation
//        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
//            throw new IllegalArgumentException("Email is required");
//        }
//
//        if (userRepository.existsByEmail(userDto.getEmail())) {
//            throw new IllegalArgumentException("User already exists with this email");
//        }
//
//        // ✅ map DTO → Entity
//        User user = modelMapper.map(userDto, User.class);
//
//        // ✅ default values
//        user.setEnabled(true);
//        user.setProvider(Provider.LOCAL);
//
//        // ✅ assign default role
//        Role role = roleRepository.findByName("ROLE_" + AppConstants.GUEST_ROLE)
//                .orElseThrow(() -> new RuntimeException("ROLE_GUEST not found in DB"));
//
//        if (user.getRoles() == null) {
//            user.setRoles(new HashSet<>());
//        }
//
//        user.getRoles().add(role); // ✅ IMPORTANT
//
//        // ✅ save user
//        User savedUser = userRepository.save(user);
//
//        // ✅ return DTO
//        return modelMapper.map(savedUser, UserDto.class);
//    }
//
//    @Override
//    public UserDto getUserByEmail(String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        return modelMapper.map(user, UserDto.class);
//    }
//
//    @Override
//    public UserDto updateUser(UserDto userDto, String userId) {
//
//        UUID id = UserHelper.parseUUID(userId);
//
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        if (userDto.getName() != null)
//            user.setName(userDto.getName());
//
//        if (userDto.getImage() != null)
//            user.setImage(userDto.getImage());
//
//        // ⚠️ IMPORTANT: Do NOT blindly update password here
//        if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
//            user.setPassword(userDto.getPassword()); // (later encode here if needed)
//        }
//
//        // ⚠️ better to control this internally
//        user.setEnabled(userDto.isEnabled());
//
//        User updatedUser = userRepository.save(user);
//
//        return modelMapper.map(updatedUser, UserDto.class);
//    }
//
//    @Override
//    public void deleteUser(String userId) {
//
//        UUID id = UserHelper.parseUUID(userId);
//
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        userRepository.delete(user);
//    }
//
//    @Override
//    public UserDto getUserById(String userId) {
//
//        UUID id = UserHelper.parseUUID(userId);
//
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        return modelMapper.map(user, UserDto.class);
//    }
//
//    @Override
//    public Iterable<UserDto> getAllUsers() {
//        return userRepository.findAll()
//                .stream()
//                .map(user -> modelMapper.map(user, UserDto.class))
//                .toList();
//    }
//}
package com.substring.auth.app.auth.services.impl;
import com.substring.auth.app.auth.entities.Provider;
import com.substring.auth.app.auth.config.AppConstants;
import com.substring.auth.app.auth.payload.UserDto;
import com.substring.auth.app.auth.entities.Role;
import com.substring.auth.app.auth.entities.User;
import com.substring.auth.app.exceptions.ResourceNotFoundException;
import com.substring.auth.app.auth.helpers.UserHelper;
import com.substring.auth.app.auth.repositories.RoleRepository;
import com.substring.auth.app.auth.repositories.UserRepository;
import com.substring.auth.app.auth.services.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.HashSet;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {

        // ✅ validation
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("User already exists with this email");
        }

        // ✅ DTO → Entity
        User user = modelMapper.map(userDto, User.class);

//        // ✅ default values
//        user.setEnabled(true);
//        user.setProvider(Provider.LOCAL);
        // ✅ BACKEND DECIDES
        user.setEnabled(true);
        user.setProvider(Provider.LOCAL);

        // ✅ assign role
        Role role = roleRepository.findByName("ROLE_" + AppConstants.GUEST_ROLE)
                .orElseThrow(() -> new RuntimeException("ROLE_GUEST not found in DB"));

        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }

        user.getRoles().add(role);

        // ✅ save
        User savedUser = userRepository.save(user);

        // ✅ Entity → DTO
        UserDto dto = modelMapper.map(savedUser, UserDto.class);

        // 🔥 IMPORTANT FIX (UUID → String)
        dto.setId(savedUser.getId().toString());

        return dto;
    }

    @Override
    public UserDto getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserDto dto = modelMapper.map(user, UserDto.class);
        dto.setId(user.getId().toString());

        return dto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {

        UUID id = UserHelper.parseUUID(userId);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (userDto.getName() != null)
            user.setName(userDto.getName());

        if (userDto.getImage() != null)
            user.setImage(userDto.getImage());

        if (userDto.getPassword() != null)
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User updatedUser = userRepository.save(user);

        UserDto dto = modelMapper.map(updatedUser, UserDto.class);
        dto.setId(updatedUser.getId().toString());

        return dto;
    }

    @Override
    public void deleteUser(String userId) {

        UUID id = UserHelper.parseUUID(userId);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) {

        UUID id = UserHelper.parseUUID(userId);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserDto dto = modelMapper.map(user, UserDto.class);
        dto.setId(user.getId().toString());

        return dto;
    }

    @Override
    public Iterable<UserDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> {
                    UserDto dto = modelMapper.map(user, UserDto.class);
                    dto.setId(user.getId().toString());
                    return dto;
                })
                .toList();
    }
}