package com.paymentgateway.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class RazorpaySignatureUtil {

	public static boolean verify(String payload, String signature, String secret) {
	    try {
	        Mac mac = Mac.getInstance("HmacSHA256");
	        mac.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));
	        byte[] digest = mac.doFinal(payload.getBytes());
	        
	        StringBuilder hash = new StringBuilder();
	        for (byte b : digest) hash.append(String.format("%02x", b));
	        
	        String generatedSignature = hash.toString();

	        // --- ADD DEBUG LOGS ---
	        System.out.println("Payload: " + payload);
	        System.out.println("Expected Signature (Razorpay): " + signature);
	        System.out.println("Generated Signature (Backend): " + generatedSignature);
	        // ----------------------

	        return generatedSignature.equals(signature);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}
