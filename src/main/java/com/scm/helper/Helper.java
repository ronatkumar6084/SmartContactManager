package com.scm.helper;

import java.security.Principal;

public class Helper {

    public static String getEmailOfSignedInUser(Principal principal)
    {
        //if logged in with email and password how to get email
        
        return "";
    }

    public static String getLinkForEmailVerification(String emailToken){

        String link="http://localhost:8080/auth/verify-email?token=" + emailToken;
        return link;
    }

}
