package APITools;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

public class Utilz {
    // This sucks but is required because I cannot display jpeg on my machine for some reason
    // "java.io.IOException: Wrong JPEG library version: library is 80, caller expects 90"
    public static String StoreImageAsPNG(String fileURL, String sourceID){
        String filePath = "";
        try{
            //Create directories and file
            File imageFile = new File("/tmp/AniSnatcher/images/");
            imageFile.mkdirs();
            filePath = imageFile.toURI().toString();
            filePath += sourceID+".png";
            imageFile = new File("/tmp/AniSnatcher/images/"+sourceID+".jpg");
            if (!imageFile.exists()){
                imageFile.createNewFile();
                //Get iamge from url and write to file
                URL url = new URL(fileURL);
                HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
                BufferedInputStream inputStream = new BufferedInputStream(conn.getInputStream());
                BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(imageFile));
                int inByte;
                while((inByte = inputStream.read()) != -1) outStream.write(inByte);
                inputStream.close();
                outStream.close();

                //Convert image to png with imagemagick
                Process process = Runtime.getRuntime().exec("convert /tmp/AniSnatcher/images/"+sourceID+".jpg"+ " /tmp/AniSnatcher/images/"+sourceID+".png");
                process.waitFor();
                process = Runtime.getRuntime().exec("rm /tmp/AniSnatcher/images/"+sourceID+".jpg");
                process.waitFor();
            }
        } catch (IOException | InterruptedException e){
            System.out.println(e);
        }
        return filePath;
    }
}
