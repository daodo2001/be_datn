package com.fpt.hotel.service;

import com.fpt.hotel.model.User;
import com.fpt.hotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Optional;

@Service
public class ForgotPasswordServiceImpl implements  ForgotPasswordService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    MailerService mailerService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User findByEmail(String email) throws MessagingException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = null;
        if(userOptional.isPresent()){
            user = userOptional.get();
            String newPassword = this.randomNewPassword();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Dear: ");
            stringBuilder.append("<b>");
            stringBuilder.append(user.getUsername());
            stringBuilder.append("<br />");
            stringBuilder.append("</b>");
            stringBuilder.append("Mật khẩu mới của bạn là : ");
            stringBuilder.append("<b>");
            stringBuilder.append(newPassword);
            stringBuilder.append("</b>");

            mailerService.queue(user.getEmail(), "Quên mật khẩu", stringBuilder.toString());
        }
        return user;
    }

    private String randomNewPassword () {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        int size = 15;
        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(size);

        for (int i = 0; i < size; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
