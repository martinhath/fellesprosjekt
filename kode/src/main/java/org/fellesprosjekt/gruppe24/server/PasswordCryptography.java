package org.fellesprosjekt.gruppe24.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PasswordCryptography {
	
	private static MessageDigest md;
	private static SecureRandom sRand;
	private static Logger lgr;
	
	private String hash;
	private String salt;
	
	static{
		try {
			md = MessageDigest.getInstance("SHA-256");
			sRand = new SecureRandom();
			lgr = Logger.getLogger(PasswordCryptography.class.getName());
		} catch (NoSuchAlgorithmException ex) {
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}
	
	public PasswordCryptography(String s) {
		this.salt = generateSalt();
		saltAndHash(s);
	}
	
	public PasswordCryptography(String s, String salt) {
		this.salt = salt;
		saltAndHash(s);
	}
	
	private void saltAndHash(String s) {
		s += this.salt;
		this.hash = stringToSHA256(s);
	}
	
	private String stringToSHA256(String s) {
		md.update(s.getBytes());
		byte[] byteData = md.digest();
		return byteToHex(byteData);
	}
	
	private String byteToHex(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
        	sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
	}
	
	private String generateSalt() {
		return byteToHex(getNextSalt());
	}
	
	private byte[] getNextSalt() {
		byte[] salt = new byte[16];
	    sRand.nextBytes(salt);
	    return salt;
	}
	
	public String getHash() {
		return hash;
	}
	
	public String getSalt() {
		return salt;
	}
	
	public boolean compareHash(String hash) {
		return this.hash.equals(hash);
	}
	
}
