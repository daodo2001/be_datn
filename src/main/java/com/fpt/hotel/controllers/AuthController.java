package com.fpt.hotel.controllers;

import com.fpt.hotel.exception.TokenRefreshException;
import com.fpt.hotel.model.ERole;
import com.fpt.hotel.model.RefreshToken;
import com.fpt.hotel.model.Role;
import com.fpt.hotel.model.User;
import com.fpt.hotel.payload.request.LogOutRequest;
import com.fpt.hotel.payload.request.LoginRequest;
import com.fpt.hotel.payload.request.SignupRequest;
import com.fpt.hotel.payload.request.TokenRefreshRequest;
import com.fpt.hotel.payload.response.JwtResponse;
import com.fpt.hotel.payload.response.MessageResponse;
import com.fpt.hotel.payload.response.ResponseObject;
import com.fpt.hotel.payload.response.TokenRefreshResponse;
import com.fpt.hotel.repository.RoleRepository;
import com.fpt.hotel.repository.UserRepository;
import com.fpt.hotel.security.jwt.JwtUtils;
import com.fpt.hotel.security.services.RefreshTokenService;
import com.fpt.hotel.security.services.UserDetailsImpl;
import com.fpt.hotel.service.ForgotPasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    ForgotPasswordService forgotPasswordService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            if (userDetails.getEnabled() == 0) {
                return ResponseEntity.badRequest().body(new MessageResponse("Tài khoản của bạn đang bị khóa!"));
            }

            String jwt = jwtUtils.generateJwtToken(userDetails);

            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
            // throw new BadCredentialsException

            return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                    userDetails.getUsername(), userDetails.getEmail(), roles));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Sai tên tài khoản hoặc mật khẩu"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest ,
                                          BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors("password")){
            return ResponseEntity.badRequest().body(new MessageResponse(bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }else if(bindingResult.hasFieldErrors("username")){
            return ResponseEntity.badRequest().body(new MessageResponse(bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Lỗi: tài khoản đã được sử dụng!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Lỗi: Email đã được sử dụng!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        user.setRoles(roles);

        // Kích hoạt tài khoản hoạt động
        user.setEnabled(1);
        logger.info("Insert data: " + userRepository.save(user));


        return ResponseEntity.ok(new MessageResponse("Đăng ký tài khoản thành công!"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken).map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser).map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
        refreshTokenService.deleteByUserId(logOutRequest.getUserId());
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }

    @PostMapping("forgot-password")
    public ResponseEntity<ResponseObject> findAllHotels(@RequestParam("email") String email) throws MessagingException {
        User user = forgotPasswordService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Không có email này!", null)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(HttpStatus.OK.toString(), "Khôi phục mật khẩu mới thành công , mời bạn check email!", user.getEmail())
        );
    }
}
