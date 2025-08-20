package com.ecom.Shopping_Cart.service;

import com.ecom.Shopping_Cart.model.UserDtls;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    UserDtls saveUser(UserDtls user);

    UserDtls getUserByEmail(String email);

    List<UserDtls> getAllUsers(String role);

    Boolean updateAccountStatus(Boolean status, Integer id);

    void increaseFailedAttempts(UserDtls user);

    void userAccountLock(UserDtls user);

    boolean unlockAccountTimeExpired(UserDtls user);

//    void resetAttempt(int userId);

    UserDtls updateUserProfile(UserDtls user, MultipartFile image);

    UserDtls updateUser(UserDtls user);

    UserDtls saveAdmin(UserDtls user);

    Boolean existsEmail(String email);

    void updateUserResetToken(String email,String resetToken);

    Integer countUser();
    @Transactional
    boolean resetPasswordByToken(String token, String newPassword);
}
