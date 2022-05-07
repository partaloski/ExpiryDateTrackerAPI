package com.example.expirydatetrackerapi.helpers;

import java.util.Random;

public class AuthCodeGenerator {
    public static String generate(){
        Random random = new Random();
        String allchars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder auth_code = new StringBuilder();
        char [] characters = allchars.toCharArray();
        for(int i=0; i<32; i++){
            auth_code.append(characters[random.nextInt(characters.length)]);
        }
        System.out.println(auth_code);
        return auth_code.toString();
    }
}
