package com.telehealthpro.service;

import com.telehealthpro.dto.UserRegistrationDto;
import com.telehealthpro.dto.UserResponseDto;
import com.telehealthpro.entity.User;
import com.telehealthpro.exception.DuplicateResourceException;
import com.telehealthpro.exception.PasswordMismatchException;
import com.telehealthpro.exception.ResourceNotFoundException;
import com.telehealthpro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(UserRegistrationDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordMismatchException("Password and Confirm Password do not match");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("An account already exists with email: " + dto.getEmail());
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setMobile(dto.getMobile());
        user.setGender(dto.getGender());
        user.setDob(dto.getDob());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("ROLE_USER");
        user.setEnabled(true);

        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public long countAllUsers() {
        return userRepository.count();
    }

    public UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getMobile(),
                user.getGender(),
                user.getDob(),
                user.getRole()
        );
    }

    public List<UserResponseDto> toResponseDtoList(List<User> users) {
        return users.stream().map(this::toResponseDto).collect(Collectors.toList());
    }
}
