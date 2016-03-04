// Include the Dropbox SDK.
import com.dropbox.core.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

public class Main {

	static String APP_KEY = "";

	static String APP_SECRET = "";

	public static DbxAppInfo appInfo;

	public static DbxRequestConfig config;

	public static DbxWebAuthNoRedirect webAuth;

	public static String authorizeUrl;

	public static String code;

	public static DbxAuthFinish authFinish;

	public static String accessToken;

	public static DbxClient client;

	public static Scanner scanner;

	public static void uploadFile() {
		try {
			System.out.println("Uploading the file.............");
			System.out.println();
			System.out.println("Enter file name");
			scanner = new Scanner(System.in);
			String inputFileName = scanner.nextLine();
			File inputFile = new File(inputFileName);
			//get input content
			String inputFileContent = FileIOService.readFile(inputFileName);
			String encryptedContent = EncrptionService.getEncryptedFile(inputFileName);
			FileIOService.writeFile(encryptedContent, inputFileName);
			FileInputStream inputStream = new FileInputStream(inputFile);
			try {
				DbxEntry.File uploadedFile = client.uploadFile("/"+inputFileName, DbxWriteMode.add(), inputFile.length(), inputStream);
				System.out.println("Uploaded: " + uploadedFile.name.toString());
			} finally {
				FileIOService.writeFile(inputFileContent, inputFileName);
				inputStream.close();
			}
			DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");
			System.out.println("Files in the root path:");
			int counter = 0;
			for (DbxEntry child : listing.children) {
				System.out.println("File " + counter++ +	" :" + child.name );
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void downloadFile() {
		try {
			System.out.println("Dowloadingthe file.............");
			System.out.println();
			DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");
			System.out.println("Files in the root path:");
			int counter = 0;
			for (DbxEntry child : listing.children) {
				System.out.println("File " + counter++ +	" :" + child.name );
			}
			System.out.println("Enter file name");
			scanner = new Scanner(System.in);
			String outputFileName = scanner.nextLine(); 
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss");
			 //sdf.format(cal.getTime()) +"_"+ 
			FileOutputStream outputStream = new FileOutputStream( sdf.format(cal.getTime()) +"_"+ outputFileName );
			String decryptedContent = "";
			
	       //sdf.format(cal.getTime()) +"_"+ 
			try {
				DbxEntry.File downloadedFile = client.getFile("/"+ outputFileName, null,
						outputStream);
				System.out.println("Metadata: " + downloadedFile.name);
			} finally {
				outputStream.close();
			}
			decryptedContent = EncrptionService.getDecryptedFile(sdf.format(cal.getTime()) +"_"+ outputFileName);
			FileIOService.writeFile(decryptedContent,  sdf.format(cal.getTime()) +"_"+ outputFileName);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException, DbxException {
		Scanner scannerAccess = new Scanner(System.in);
		System.out.println("Enter Application Access key");
		APP_KEY = scannerAccess.nextLine();
		System.out.println("Enter Application Secret key");
		APP_SECRET = scannerAccess.nextLine();
		int option;
		appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
		config = new DbxRequestConfig("CloudFileSecurity",
				Locale.getDefault().toString());
		webAuth = new DbxWebAuthNoRedirect(config, appInfo);
		// Have the user sign in and authorize your app.
		authorizeUrl = webAuth.start();
		System.out.println("1. Go to: " + authorizeUrl);
		System.out.println("2. Click \"Allow\" (you might have to log in first)");
		System.out.println("3. Copy the authorization code.");
		code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
		// This will fail if the user enters an invalid authorization code.
		authFinish = webAuth.finish(code);
		accessToken = authFinish.accessToken;
		client = new DbxClient(config, accessToken);
		System.out.println("Linked account: " + client.getAccountInfo().displayName);
		Scanner scanner = new Scanner(System.in);
		while(true) {
			System.out.println("Enter 1 to upload, \n2 to download, \n3 to do both\n ENTER ANY OTHER KEY TO EXIT\n");
			option = scanner.nextInt();
			switch(option) {
			case 1:
				uploadFile();
				break;
			case 2:
				downloadFile();
				break;
			case 3:
				uploadFile();
				downloadFile();
				break;
			default:
				System.exit(0);
			}
		}

		/*String inputFileName = "working-draft.txt";
        String outputFileName = "magnum-opus.txt";

        File inputFile = new File(inputFileName);
        String encryptedContent = EncrptionService.getEncryptedFile(inputFileName);
        FileIOService.writeFile(encryptedContent, inputFileName);

        FileInputStream inputStream = new FileInputStream(inputFile);
        try {
            DbxEntry.File uploadedFile = client.uploadFile("/magnum-opus.txt",
                DbxWriteMode.add(), inputFile.length(), inputStream);
            System.out.println("Uploaded: " + uploadedFile.toString());
        } finally {
            inputStream.close();
        }

        DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");
        System.out.println("Files in the root path:");
        for (DbxEntry child : listing.children) {
            System.out.println("	" + child.name + ": " + child.toString());
        }

        FileOutputStream outputStream = new FileOutputStream(outputFileName);
        String decryptedContent = "";
        try {
            DbxEntry.File downloadedFile = client.getFile("/magnum-opus.txt", null,
                outputStream);
            System.out.println("Metadata: " + downloadedFile.toString());
        } finally {
            outputStream.close();
        }
        decryptedContent = EncrptionService.getDecryptedFile(outputFileName);
        FileIOService.writeFile(decryptedContent, outputFileName);*/
	}
}