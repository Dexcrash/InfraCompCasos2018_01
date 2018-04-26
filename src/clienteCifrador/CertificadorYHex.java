package clienteCifrador;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class CertificadorYHex {

	public String toHexString(byte[] array)
	{
		return DatatypeConverter.printHexBinary(array);
	}

	public byte[] toByteArray(String s)
	{
		return DatatypeConverter.parseHexBinary(s);
	}
	
	public X509Certificate certificado(KeyPair key) throws ParseException, OperatorCreationException, CertificateException, NoSuchProviderException
	{

		// Generacion de llaves
		try {
			String creado ="2018-08-10";
			String hasta ="2024-08-10";
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date Creado =  formato.parse(creado);
			java.util.Date Hasta =  formato.parse(hasta);
			X500Name subname = new X500Name("CN=Uniandes");
			BigInteger serial =new BigInteger(128,new Random());
			JcaX509v3CertificateBuilder certi = new JcaX509v3CertificateBuilder(subname, serial,Creado, Hasta, subname, key.getPublic());
			ContentSigner Firma =new JcaContentSignerBuilder("SHA256WithRSAEncryption").build(key.getPrivate());
			X509Certificate gener= new JcaX509CertificateConverter().getCertificate(certi.build(Firma)) ;
			return gener;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
