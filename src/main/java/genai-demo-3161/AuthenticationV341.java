```java
package com.example.mfa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MfaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MfaApplication.class, args);
    }
}

package com.example.mfa.controller;

import com.example.mfa.service.AuthService;
import com.example.mfa.dto.LoginRequest;
import com.example.mfa.dto.MfaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Logs the user in and triggers the MFA process
     *
     * @param loginRequest contains username and password
     * @return MfaResponse indicating mfa status and instructions
     */
    @PostMapping("/login")
    public MfaResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.processLogin(loginRequest);
    }

    /**
     * Verifies the MFA code provided by the user
     *
     * @param mfaCode the MFA code sent to user's registered device
     * @return information whether MFA is successful or not
     */
    @PostMapping("/verify-mfa")
    public MfaResponse verifyMfa(@RequestParam String mfaCode) {
        return authService.verifyMfaCode(mfaCode);
    }
}

package com.example.mfa.dto;

public class LoginRequest {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

package com.example.mfa.dto;

public class MfaResponse {

    private boolean mfaRequired;
    private String message;

    public boolean isMfaRequired() {
        return mfaRequired;
    }

    public void setMfaRequired(boolean mfaRequired) {
        this.mfaRequired = mfaRequired;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package com.example.mfa.service;

import com.example.mfa.dto.LoginRequest;
import com.example.mfa.dto.MfaResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    /**
     * Processes the login for user and initiates MFA process
     *
     * @param loginRequest contains login details
     * @return MfaResponse
     */
    public MfaResponse processLogin(LoginRequest loginRequest) {
        // Validate user credentials
        boolean validCredentials = validateCredentials(loginRequest.getUsername(), loginRequest.getPassword());
        
        if (!validCredentials) {
            MfaResponse response = new MfaResponse();
            response.setMfaRequired(false);
            response.setMessage("Invalid username or password.");
            return response;
        }

        // Implement MFA initiation logic here
        
        MfaResponse response = new MfaResponse();
        response.setMfaRequired(true);
        response.setMessage("MFA code sent to registered device.");
        
        return response;
    }

    /**
     * Verifies the provided MFA code
     *
     * @param mfaCode MFA code provided by user
     * @return MfaResponse indicating MFA success or failure
     */
    public MfaResponse verifyMfaCode(String mfaCode) {
        // Verify the MFA code
        boolean isCodeValid = verifyCode(mfaCode);
        
        MfaResponse response = new MfaResponse();
        if (isCodeValid) {
            response.setMfaRequired(false);
            response.setMessage("MFA verification successful.");
        } else {
            response.setMfaRequired(true);
            response.setMessage("Invalid MFA code.");
        }

        return response;
    }

    private boolean validateCredentials(String username, String password) {
        // Security: Here you should implement your user credential validation logic
        return true;
    }

    private boolean verifyCode(String mfaCode) {
        // Security: Implement actual code verification logic
        return true; // Simulating code verification success
    }
}

```