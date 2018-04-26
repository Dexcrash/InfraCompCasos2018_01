package clienteCifrador;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

public class Cifrador {

	public byte[] cifrarSimetrico(byte[] text , SecretKey llaveSimetrica, String algoritmo) {
		byte [] cipheredText;
		try {
		    algoritmo = algoritmo + ((algoritmo.equals("DES")) || (algoritmo.equals("AES")) ? "/ECB/PKCS5Padding" : "");
			Cipher cipher = Cipher.getInstance(algoritmo);
			byte [] clearText = text;
			String s1 = new String (clearText);
			System.out.println("CS: Texto original: " + s1);
			cipher.init(Cipher.ENCRYPT_MODE, llaveSimetrica);
			cipheredText = cipher.doFinal(clearText);
			String s2 = new String (cipheredText);
			System.out.println("CS: Texto cifrada: " + s2);
			return cipheredText;
		}
		catch (Exception e) {
			System.out.println("Excepcion: " + e.getMessage());
			return null;
		}
	}

	public byte[] descifrarSimetrico(byte [] cipheredText, SecretKey llaveSimetrica, String algoritmo) {
		try {
			algoritmo = algoritmo + ((algoritmo.equals("DES")) || (algoritmo.equals("AES")) ? "/ECB/PKCS5Padding" : "");
			Cipher cipher = Cipher.getInstance(algoritmo);
			cipher.init(Cipher.DECRYPT_MODE, llaveSimetrica);
			byte [] clearText = cipher.doFinal(cipheredText);
			String s3 = new String(clearText);
			System.out.println("DS: Texto original: " + s3);
			return clearText;
		}
		catch (Exception e) {
			System.out.println("Excepcion: " + e.getMessage());
		}
		return null;
	}

	public byte[] cifrarAsimetrico(byte [] text, PublicKey llavePublica, String algoritmo) {
		try {
			byte [] clearText = text;
			Cipher cipher = Cipher.getInstance(algoritmo);
			String s1 = new String (clearText);
			System.out.println("CA: Texto original: " + s1);
			cipher.init(Cipher.ENCRYPT_MODE, llavePublica);
			byte [] cipheredText = cipher.doFinal(clearText);
			System.out.println("CA: Texto cifrada: " + cipheredText);
			return cipheredText;
		}
		catch (Exception e) {
			System.out.println("Excepcion: " + e.getMessage());
			return null;
		}
	}

	public byte[] descifrarAsimetrico(byte[] cipheredText, PrivateKey llavePrivada, String algoritmo) {
		try {
			Cipher cipher = Cipher.getInstance(algoritmo);
			cipher.init(Cipher.DECRYPT_MODE, llavePrivada);
			byte [] clearText = cipher.doFinal(cipheredText);
			String s3 = new String(clearText);
			System.out.println("DA: Texto original: " + s3);
			return clearText;
		}
		catch (Exception e) {
			System.out.println("Excepcion: " + e.getMessage());
		}
		return null;
	}

	public byte[] cifrarMac(byte[] msg, SecretKey key, String algoritmo)
	{
		Mac mac;
		try {
			mac = Mac.getInstance(algoritmo);
			mac.init(key);
			byte[] bytes = mac.doFinal(msg);
			return bytes;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
