package com.ngwisefood.app.utility;



import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by andy.ng on 1/11/18.
 */

public class MyUtils {

    public static Random random;

    public static byte[] binaryStringToByteArray(String binaryString) {

        short a = Short.parseShort(binaryString, 2);
        ByteBuffer bytes = ByteBuffer.allocate(2).putShort(a);

        byte[] array = bytes.array();

        return array;
    }

    public static String asciiToHex(String asciiStr) {

        char[] chars = asciiStr.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(Integer.toHexString((int) ch));
        }

        return hex.toString();

    }

    public static String hexToAscii(String hexStr) {

        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();

    }

    public static byte[] hexStringToByteArray(String s) {

        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {

            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));

        }
        return data;

    }

    public static String convertByteToUnicode(byte[] bytes) {

        try {
            String output = new String(bytes, "UTF-16");
            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    public static String hexStringToUnicode(String hexString) {

        byte[] bytes = hexStringToByteArray(hexString);
        String unicodeString = convertByteToUnicode(bytes);

        return unicodeString;

    }


    /**
     * @param min
     * @param max
     * @return Generate Random int number from min to max
     */
    public static int randomInteger(int min, int max) {
        if (random == null) {
            random = new Random();
        }
        int randomValue = random.nextInt(max - min + 1) + min;

        return randomValue;
    }



    public static double randomDouble(double min, double max) {

        double random = Math.random() * (max - min) + min;

        return random;
    }


    public static String generateRandomString(int length) {
        String text = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        int maxIndex = text.length() - 1;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(text.charAt(randomInteger(0, maxIndex)));
        }
        return stringBuilder.toString();
    }

    public static String generateRandomText(int length) {
        String text = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int maxIndex = text.length() - 1;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(text.charAt(randomInteger(0, maxIndex)));
        }
        return stringBuilder.toString();
    }

    public static String generateRandomHex(int length) {
        String text = "1234567890abcdef";
        int maxIndex = text.length() - 1;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(text.charAt(randomInteger(0, maxIndex)));
        }
        return stringBuilder.toString();
    }

    private static int sumHex(String hexNumber) {

        int length = hexNumber.length();
        int totalValue = 0;
        for (int i = 0; i < length; i++) {
            totalValue += Integer.parseInt(hexNumber.substring(i, i + 1), 16);
        }
        return totalValue;

    }

    private static int sumAscii(String hexNumber) {

        int length = hexNumber.length();
        int totalValue = 0;
        for (int i = 0; i < length; i++) {
            totalValue += (int) hexNumber.charAt(i);
        }
        return totalValue;

    }

    public static String uuidFormatConvertion(String uuid) {
        try {
            String plain = uuid;
            String uuidFormatted = String.format("%1$s-%2$s-%3$s-%4$s-%5$s", plain.substring(0, 8), plain.substring(8, 12), plain.substring(12, 16), plain.substring(16, 20), plain.substring(20, 32));
            return uuidFormatted.toUpperCase();
        } catch (Exception e) {
            return "-";
        }

    }

    public static String maxTextLength(String text, int max) {
        if (text.length() < max) {
            return text;
        } else {
            String returnText = text.substring(0, max) + "...";
            return returnText;
        }

    }

    public static boolean isUuid(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String removeTrailingZero(String s) {
        s = s.indexOf(".") < 0 ? s : s.replaceAll("0*$", "").replaceAll("\\.$", "");
        return s;
    }

    public static String roundUp_backup(double value, int decimal) {

        DecimalFormat decimalFormat;
        switch (decimal) {
            case 0:
                decimalFormat = new DecimalFormat("0");
                break;
            case 1:
                decimalFormat = new DecimalFormat("0.0");
                break;
            case 2:
                decimalFormat = new DecimalFormat("0.00");
                break;
            case 3:
                decimalFormat = new DecimalFormat("0.000");
                break;
            case 4:
                decimalFormat = new DecimalFormat("0.0000");
                break;
            default:
                decimalFormat = new DecimalFormat("0.00");
                break;
        }

        return decimalFormat.format(value);
    }

    public static DecimalFormat getFormatter(int decimal){
        String totalZero = "";
        for (int i = 0; i < decimal; i++) {
            totalZero += "0";
        }
        String format = "###,###,###." + totalZero;

        DecimalFormat formatter = new DecimalFormat(format);

        return formatter;
    }

    public static String formatCurrency(double value, int decimal) {

        try{
            String format = "%,." + decimal + "f";
            String numString = String.format(format, value);
            return numString;
        }catch (Exception e){
            e.printStackTrace();
            try{
                String totalZero = "";
                for (int i = 0; i < decimal; i++) {
                    totalZero += "#";
                }
                String format = "###,###,###." + totalZero;
                DecimalFormat formatter = new DecimalFormat(format);

                return formatter.format(value);
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }

        return Double.toString(value);

    }

    public static double roundByDecimal(double num, int decimal) {
        double roundDecimal = Math.pow(10, decimal);
        long roundValue = Math.round(roundDecimal * num);
        double dblRoundValue = (double) roundValue;
        return dblRoundValue / roundDecimal;
    }



    public static void writeFile(String filename, String inputFileContent) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte data[] = inputFileContent.getBytes();
            FileOutputStream out = new FileOutputStream(filename);
            out.write(data);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copy(File src, File dst) throws IOException {
        if(dst.exists()){
            dst.delete();
        }
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }

    public static String[] getLines(InputStream inputStream) {
        // =============== Method 2 ===============
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] lines = byteArrayOutputStream.toString().split("\r\n");

        return lines;
    }

    public static List getLineList(File file){

        List<String> lineList = new ArrayList<>();

        try{
            FileReader fr=new FileReader(file);   //reads the file
            BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream

            String line;
            while((line=br.readLine())!=null)
            {
                lineList.add(line);
            }
            fr.close();
            br.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return lineList;
    }



    /**
     * int resId = MyUtils.getResId(iconName,R.drawable.class);
     *
     * @param resName
     * @param c
     * @return
     */
    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    public static String listJoin(String delimiter, List<String> list) {
        String joinText = "";
        for (String text : list) {
            if (joinText.isEmpty() == false) {
                joinText += delimiter;
            }
            joinText += text;
        }
        return joinText;
    }


    public static void zipDirectory(String sourceFile, String output) throws IOException {
        //String sourceFile = "zipTest";
        FileOutputStream fos = new FileOutputStream(output);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(sourceFile);

        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    public static void unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if(ze.isDirectory()){
                    if(dir.exists() == false){
                        dir.mkdir();
                    }
                }

                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
            }
        } finally {
            zis.close();
        }
    }

    public static void unzip(String sourceFile, String outputFile) throws IOException {
        String fileZip = sourceFile;
        File destDir = new File(outputFile);
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    public static void deleteFile(File file){
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File subFile : files){
                deleteFile(subFile);
            }
            file.delete();
        }else{
            file.delete();
        }
    }

    public static String readFile(String filePath){
        try{
            InputStream inputStream = new FileInputStream(filePath);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int i;
            try {
                i = inputStream.read();
                while (i != -1) {
                    byteArrayOutputStream.write(i);
                    i = inputStream.read();
                }
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream.close();

            return byteArrayOutputStream.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }


    public static String getCounterInHourMinuteSecond(int counter){
        int hours = counter / 3600;
        int minutes = (counter % 3600) / 60;
        int seconds = counter % 60;

        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        return timeString;
    }

    public static String getCounterInMinuteSecond(int counter){

        int minutes = (counter % 3600) / 60;
        int seconds = counter % 60;

        String timeString = String.format("%02d:%02d", minutes, seconds);

        return timeString;
    }

    public static String getBodyText(HttpExchange httpExchange){
        try{
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);


            int b;
            StringBuilder stringBuilder = new StringBuilder();
            while ((b = br.read()) != -1) {
                stringBuilder.append((char) b);
            }
            return stringBuilder.toString();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static long convertIpAddressToDecimal(String ipAddress){


        String[] addrArray = ipAddress.split("\\.");

        long ipDecimal = 0;
        for (int i = 0; i < addrArray.length; i++) {
            int power = 3 - i;
            ipDecimal += ((Integer.parseInt(addrArray[i]) % 256 * Math.pow(256, power)));
        }

        return ipDecimal;

    }

    public static String escapeURLCharacter(String text){
        try{
            text = text.replaceAll("\\\\x", "%");
            String result = URLDecoder.decode(text,"utf-8");
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return text;
    }

    public static String generateUniqueId(String prefix){
        if(prefix == null){
            prefix = generateRandomString(10);
        }
        String uniqueId = prefix + "_" + System.currentTimeMillis()+ "_" + MyUtils.generateRandomString(10);
        return uniqueId;
    }

    public static void printMemory() {

        Runtime runtime  = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory()/1000000L;
        long freeMemory  = runtime.freeMemory()/1000000L;

        String result = "Memory: Used=" + (totalMemory - freeMemory) + "MB, Total=" + totalMemory + "MB, Free=" + freeMemory + "MB";
        System.out.println(result);

    }

    public static long getUniqueId(){
        String nanoTime = System.nanoTime() + "";
        String randomNumber = MyUtils.randomInteger(100,900) + "";
        String longIdSstring = nanoTime + randomNumber;
        Long longId = Long.parseLong(longIdSstring);
        return longId;
    }
}
