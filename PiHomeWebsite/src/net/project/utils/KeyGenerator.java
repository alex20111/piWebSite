package net.project.utils;

import java.util.Random;

public class KeyGenerator {

	public static int MAX_LENGTH = 35;	
	
	public String generateKey(int keyLength) {
		if (keyLength > MAX_LENGTH){
			keyLength = MAX_LENGTH;
		}
        String chars = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        
        int l = chars.length();
        
        while (salt.length() < keyLength) { // length of the random string.
            int index = rnd.nextInt(l);
            salt.append(chars.charAt(index));
        }

        return salt.toString();

    }
}
