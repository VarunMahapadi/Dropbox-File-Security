import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Scanner;

import javax.crypto.spec.IvParameterSpec;

import com.dropbox.core.*;
import com.dropbox.core.http.*;
import com.dropbox.core.json.*;
import com.dropbox.core.util.*;
import com.fasterxml.*;
import com.fasterxml.jackson.core.base.*;
import com.fasterxml.jackson.core.format.*;
import com.fasterxml.jackson.core.filter.*;
import com.fasterxml.jackson.core.io.*;
import com.fasterxml.jackson.core.json.*;
import com.fasterxml.jackson.core.sym.*;
import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.core.util.*;

public class EncrptionService {
	private static byte[] DEFAULT_IV = null;
	public EncrptionService() {
		// TODO Auto-generated constructor stub
	}

	public static String getEncryptedFile(String fileName){
		String encryptedContent = null;
		try {
			
			
			EcbEncryptionService ecb = new EcbEncryptionService();
			CbcEncryptionService cbc = new CbcEncryptionService();
			CtrEncryptionService ctr = new CtrEncryptionService();
			String encryptedSring, decryptedSring;
			//String input = "OneTwoThree";
			String input = FileIOService.readFile(fileName);
			byte[] iv = new byte[16];
			//SecureRandom prng = new SecureRandom();
			//prng.nextBytes(iv);
			IvParameterSpec  ivs = new IvParameterSpec(iv);
					
					
			encryptedContent = ecb.doEncryption(input, null);
			encryptedContent = cbc.doEncryption(encryptedContent, ivs);
			encryptedContent = ctr.doEncryption(encryptedContent, ivs);
				
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		return  encryptedContent;
	}
	public static String getDecryptedFile(String fileName){
		String decryptedContent = null;
		try {
			
			
			EcbEncryptionService ecb = new EcbEncryptionService();
			CbcEncryptionService cbc = new CbcEncryptionService();
			CtrEncryptionService ctr = new CtrEncryptionService();
			String encryptedSring, decryptedSring;
			//String input = "OneTwoThree";
			byte[] iv = new byte[16];
			//SecureRandom prng = new SecureRandom();
			//prng.nextBytes(iv);
			IvParameterSpec  ivs = new IvParameterSpec(iv);
			String output = FileIOService.readFile(fileName);
			decryptedContent =  ctr.doDecryption(output, ivs);
			decryptedContent =  cbc.doDecryption(decryptedContent, ivs);
			decryptedContent =  ecb.doDecryption(decryptedContent, null);
			 
			
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		return decryptedContent;
	}
	
	

}
