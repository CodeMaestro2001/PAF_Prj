package com.paf_grp.backend.controller.pasindu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paf_grp.backend.model.pasindu.ForgotPasswordRequest;
import com.paf_grp.backend.model.pasindu.ResetPasswordRequest;
import com.paf_grp.backend.model.pasindu.VerifyOTPRequest;
import com.paf_grp.backend.service.OTPService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class ForgotPasswordController {

    @Autowired
    private OTPService otpService;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            otpService.generateOTP(request.getEmail());
            return ResponseEntity.ok("OTP sent to email!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sending OTP. Please try again.");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOTPRequest request) {
        try {
            boolean isVerified = otpService.verifyOtp(request.getEmail(), request.getOtp());
            if (isVerified) {
                return ResponseEntity.ok("OTP verified successfully!");
            } else {
                return ResponseEntity.status(400).body("Invalid OTP. Please try again.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // Endpoint to reset the password
    @PostMapping("/reset-password")
public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
    try {
        // Check if newPassword is provided and matches the expected criteria
        boolean isReset = otpService.resetPassword(request.getEmail(), request.getNewPassword());
        if (isReset) {
            return ResponseEntity.ok("Password reset successfully!");
        } else {
            return ResponseEntity.status(400).body("Failed to reset password. Please try again.");
        }
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error resetting password.");
    }
}

}
