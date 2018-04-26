package clienteCifrador;
import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;
import javax.swing.plaf.SliderUI;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

public class Cliente
{
	public static final String BLOWFISH = "BLOWFISH";
	public static final String AES = "AES";
	public static final String RSA = "RSA";
	public static final String HMACMD5 = "HMACMD5";
	public static final String HMACSHA1 = "HMACSHA1";
	public static final String HMACSHA256 = "HMACSHA256";
	private final static String PADDING="/ECB/PKCS5Padding";
	static CertificadorYHex certificador = new CertificadorYHex();
	static Cifrador cifrador = new Cifrador();

	public static void main(String[] args) throws IOException, ParseException, OperatorCreationException, CertificateException, NoSuchProviderException, NoSuchAlgorithmException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while(true) {
			String op = "";
			String info[] = new String[4];
			System.out.println("-----------------------------// Cliente Distribuidor //---------------");
			System.out.println("Eliga el tipo de Servidor con el que se va a conectar:");
			System.out.println("1. Con Cifrado");
			System.out.println("2. Sin Cifrado");
			System.out.println("3. CERRAR PROGRAMA");
			op = br.readLine();
			if(op.equals("1")) {
				info = menuCifrado(br);
				conCifrado(info[1], info[2], info[3], info[0]);
				System.out.println("El proceso de envio termino");
				System.out.println("Enter para volver al menu");
				br.readLine();
			}else if (op.equals("2")) {
				info = menuCifrado(br);
				sinCifrado(info[1], info[2], info[3], info[0]);
				System.out.println("El proceso de envio termino");
				System.out.println("Enter para volver al menu");
				br.readLine();
			}else if (op.equals("3")) {
				break;
			}
			else {
				System.out.println("ERROR: Ingrese una opcion correcta");
				br.readLine();
			}

		}
	}

	public static String[] menuCifrado(BufferedReader br){
		String [] usuInfo = new String[4];
		try {
			String opcion = "";			
			System.out.println("Ingrese la posicion del paquete");
			usuInfo[0] = br.readLine();
			System.out.println("Algoritmo de cifrado Simetrico");
			System.out.println("Eliga uno de os algoritmos a continuación:");
			System.out.println("1. Blowfish");
			System.out.println("2. AES");
			opcion = br.readLine();

			if(opcion.equals("1"))usuInfo[1]= BLOWFISH;
			else if(opcion.equals("2"))usuInfo[1]= AES;
			else System.out.println("Opcion incorrecta");

			System.out.println("Algoritmo de cifrado Asimetrico");
			System.out.println("Eliga uno de os algoritmos a continuación:");
			System.out.println("1. RSA");
			opcion = br.readLine();

			if(opcion.equals("1"))usuInfo[2]= RSA;
			else System.out.println("Opcion incorrecta");

			System.out.println("Algoritmo de cifrado HASH");
			System.out.println("Eliga uno de os algoritmos a continuación:");
			System.out.println("1. HMACMD5");
			System.out.println("2. HMACSHA1");
			System.out.println("3. HMACSHA256");
			opcion = br.readLine();

			if(opcion.equals("1"))usuInfo[3]= HMACMD5;
			else if(opcion.equals("2"))usuInfo[3]= HMACSHA1;
			else if(opcion.equals("3"))usuInfo[3]= HMACSHA256;
			else System.out.println("Opcion incorrecta");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return usuInfo;
	}

	public static void conCifrado(String algotimoSimetrico, String algoritmoAsimetrico, String algoritmoHash, String msg) {

		Socket socketCliente = null;
		PrintWriter escritor = null;
		BufferedReader lector = null;
		String ip ="127.0.0.1";
		String comunicacion= null;
		X509Certificate servidorCert= null;
		KeyPair llaveAsimetrica = null;
		String posicionString = msg;
		String [] algoritmos = {"ALGORITMOS",algotimoSimetrico,algoritmoAsimetrico,algoritmoHash};

		//Definicion del socket y sus canales de escritura y lectura.
		try {
			socketCliente = new Socket(ip, 1027);
			escritor = new PrintWriter(socketCliente.getOutputStream(), true);
			lector = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
		}catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			System.exit(1);
		}


		//Inicio del procesamiento de los envios
		try {
			String fromServer;
			String fromUser;
			int fases= 0;
			boolean ejecutar = true;
			while (ejecutar && fases <=5 )
			{
				System.out.println("Fase: " + fases);
				//Envia el mensaje HOLA
				if(fases==0 )
				{
					fromUser="HOLA";
					escritor.println(fromUser);
					fases++;
				}
				//Envia los algoritmos que se van a utilizar
				else if(fases==1 )
				{
					fromUser= algoritmos[0] + ":" + algoritmos[1] + ":" + algoritmos[2] + ":" + algoritmos[3];
					escritor.println(fromUser);
					fases++;
				}
				//Se envia el certificado del cliente
				else if(fases==2 )
				{

					try {
						fromUser="CERTCLNT";
						escritor.println(fromUser);
						Security.addProvider(new BouncyCastleProvider());
						KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
						llaveAsimetrica = keyGen.generateKeyPair();

						keyGen.initialize(1024);
						X509Certificate cert = certificador.certificado(llaveAsimetrica);
						byte[] mybyte =cert.getEncoded();
						socketCliente.getOutputStream().write(mybyte);
						socketCliente.getOutputStream().flush();
						fases++; 
					} catch (CertificateEncodingException e) {					
						e.printStackTrace();
					}
				}
				//Se recibe y procesa el certificado del servidor.
				else if(fases==3 )
				{
					comunicacion = lector.readLine();
					try{
						if(comunicacion.equalsIgnoreCase("CERTSRV"))
						{
							CertificateFactory comprobacion = CertificateFactory.getInstance("X.509");
							byte[] CS=new byte[6000];
							socketCliente.getInputStream().read(CS);
							InputStream inputStream = new ByteArrayInputStream(CS);
							servidorCert= ((X509Certificate)comprobacion.generateCertificate(inputStream));

							fromUser="ESTADO:OK";
							escritor.println(fromUser);
							fases++;
						}
					}catch (CertificateException ce) {
						fromUser="ESTADO:ERROR";
						escritor.println(fromUser);
						ejecutar= false;

					}

				}
				//Se recibe la llave Simetrica cifrada y s eprocede con esta a generar tanto ACT1 como ACT2
				else if(fases == 4)
				{
					byte[] position = posicionString.getBytes(); 
					comunicacion = lector.readLine();
					System.out.println(comunicacion);
					String [] mensaje = comunicacion.split(":");
					byte[] CAllave = DatatypeConverter.parseHexBinary(mensaje[1]);
					byte[] DALlave = cifrador.descifrarAsimetrico(CAllave, llaveAsimetrica.getPrivate(), algoritmos[2]);
					SecretKey llaveSimetrica = new SecretKeySpec(DALlave, algoritmos[1]);
					byte[] CSPosition = cifrador.cifrarSimetrico(position, llaveSimetrica, algoritmos[1]);
					fromUser = "ACT1:";
					fromUser += certificador.toHexString(CSPosition);
					escritor.println(fromUser);

					byte[] CHPosition = cifrador.cifrarMac(position, llaveSimetrica, algoritmos[3]);
					byte[] CAPostion = cifrador.cifrarAsimetrico(CHPosition, servidorCert.getPublicKey(), algoritmos[2]);
					fromUser = "ACT2:";
					fromUser += certificador.toHexString(CAPostion);
					escritor.println(fromUser);
					fases++;

				}
				//Se recibe el mensaje de confirmació de integralidad
				else if(fases == 5){
					comunicacion = lector.readLine();
					fromServer = comunicacion.split(":")[1];
					System.out.println(comunicacion);
					if(fromServer.equals("OK"))fases++;
				}
			}
			//Se cierrarn los Streams
			escritor.close();
			lector.close();
			socketCliente.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void sinCifrado(String algotimoSimetrico, String algoritmoAsimetrico, String algoritmoHash, String msg)	{
		System.out.println("Entro");

		Socket socketCliente = null;
		PrintWriter escritor = null;
		BufferedReader lector = null;
		String ip ="127.0.0.1";
		String comunicacion= null;
		X509Certificate servidorCert= null;
		KeyPair llaveAsimetrica = null;
		String posicionString = msg;
		String [] algoritmos = {"ALGORITMOS",algotimoSimetrico,algoritmoAsimetrico,algoritmoHash};

		//Definicion del socket y sus canales de escritura y lectura.
		try {
			socketCliente = new Socket(ip, 1027);
			escritor = new PrintWriter(socketCliente.getOutputStream(), true);
			lector = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
		}catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			System.exit(1);
		}


		//Inicio del procesamiento de los envios
		try {
			String fromServer;
			String fromUser;
			int fases= 0;
			boolean ejecutar = true;
			while (ejecutar && fases <=5 )
			{
				System.out.println("Fase: " + fases);
				//Envia el mensaje HOLA
				if(fases==0 )
				{
					fromUser="HOLA";
					escritor.println(fromUser);
					fases++;
				}
				//Envia los algoritmos que se van a utilizar
				else if(fases==1 )
				{
					fromUser= algoritmos[0] + ":" + algoritmos[1] + ":" + algoritmos[2] + ":" + algoritmos[3];
					escritor.println(fromUser);
					fases++;
				}
				//Se envia el certificado del cliente
				else if(fases==2 )
				{
					try {
						fromUser="CERTCLNT";
						escritor.println(fromUser);
						Security.addProvider(new BouncyCastleProvider());
						KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
						llaveAsimetrica = keyGen.generateKeyPair();

						keyGen.initialize(1024);
						X509Certificate cert =certificador.certificado(llaveAsimetrica);

						StringWriter escritorCert = new StringWriter();
						JcaPEMWriter SCert = new JcaPEMWriter(escritorCert);
						SCert.writeObject(cert);
						SCert.flush();
						SCert.close();
						String certStr = escritorCert.toString();
						certStr = certStr.trim();
						System.out.println(certStr);
						escritor.println(certStr);      
						fases++;
					} catch (CertificateEncodingException e) {
						e.printStackTrace();
					}
				}
				//Se recibe y procesa el certificado del servidor.
				else if(fases==3 )
				{
					try {
						comunicacion = lector.readLine();
						if(comunicacion.equalsIgnoreCase("CERTSRV")){
							String certificado ="";
							String actual =lector.readLine();
							while(actual.equalsIgnoreCase("-----END CERTIFICATE-----") == false){
								certificado+= actual +"\n";
								actual = lector.readLine();
							}
							certificado+= actual;
							StringReader a = new StringReader(certificado);
							PemReader lectorCertificados= new PemReader(a);
							PemObject CertificadoServidor= lectorCertificados.readPemObject();

							X509CertificateHolder certHolder = new X509CertificateHolder(CertificadoServidor.getContent());
							X509Certificate certificadoS = new JcaX509CertificateConverter().getCertificate(certHolder);

							lectorCertificados.close();
							escritor.println("ESTADO:OK");
							fases++;
						}
					}catch (Exception ce) {
						fromUser="ESTADO:ERROR";
						escritor.println(fromUser);
						ejecutar= false;

					}					
				}
				//Se recibe la llave Simetrica cifrada y s eprocede con esta a generar tanto ACT1 como ACT2
				else if(fases == 4)
				{
					fromUser = "ACT1:";
					fromUser += posicionString;
					escritor.println(fromUser);

					fromUser = "ACT2:";
					fromUser += posicionString;
					escritor.println(fromUser);
					fases++;
				}
				//Se recibe el mensaje de confirmació de integralidad
				else if(fases == 5){
					fases++;
				}
			}
			//Se cierrarn los Streams
			escritor.close();
			lector.close();
			socketCliente.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
