   
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


class EcbEncryptionService
{
 public SecretKeySpec myDesKey;
 public Base64 base64 = new Base64();
 //byte[] key;
 
 public EcbEncryptionService() throws Exception
 { 
	 byte[] key = (FileIOService.readFile("privateKey.txt")).getBytes("UTF-8");
	 MessageDigest sha = MessageDigest.getInstance("SHA-1");
	 key = sha.digest(key);
	 key = Arrays.copyOf(key, 16);
	 myDesKey = new SecretKeySpec(key, "AES");
 }
 
 public String doEncryption(String password, IvParameterSpec iv) throws Exception
 {
	 	Cipher aesCipherForEncryption = Cipher.getInstance("AES/ECB/PKCS5PADDING"); 
		aesCipherForEncryption.init(Cipher.ENCRYPT_MODE, this.myDesKey);
		byte[] byteDataToEncrypt = password.getBytes("UTF-8");
		return (new Base64()).encodeAsString(aesCipherForEncryption.doFinal(byteDataToEncrypt));
 }
 
 public String doDecryption(String strCipherText, IvParameterSpec iv)throws Exception
 {
		Cipher aesCipherForDecryption = Cipher.getInstance("AES/ECB/PKCS5PADDING"); 				
		aesCipherForDecryption.init(Cipher.DECRYPT_MODE, this.myDesKey);
		byte[] decodedValue = new Base64().decode(strCipherText.getBytes());
		byte[] decryptedVal = aesCipherForDecryption.doFinal(decodedValue);
		return new String(decryptedVal);
 }
 
}


