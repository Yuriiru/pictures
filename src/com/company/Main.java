package com.company;
//кон
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    private static final String IN_FILE_TXT = "/Users/yurii.ru/IdeaProjects/pictures/src/com/company/inFile.txt";
    private static final String OUT_FILE_TXT = "/Users/yurii.ru/IdeaProjects/pictures/src/com/company/outFile.txt";
    private static final String PICTURES_PICTURES = "/Users/yurii.ru/IdeaProjects/pictures/src/com/company/pictures";

    public static void main(String[] args) {
        String Url;
        try (BufferedReader inFile = new BufferedReader(new FileReader(IN_FILE_TXT));
             BufferedWriter outFile = new BufferedWriter(new FileWriter(OUT_FILE_TXT))) {
            while ((Url = inFile.readLine()) != null) {
                URL url = new URL(Url);

                String result;
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    result = bufferedReader.lines().collect(Collectors.joining("\n"));
                }
                Pattern email_pattern = Pattern.compile("https:\\/\\/ru\\.freepik\\.com\\/download-file\\/3707296");
                Matcher matcher = email_pattern.matcher(result);
                while (matcher.find()) {
                    outFile.write(matcher.group() + "\r\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader picturesFile = new BufferedReader(new FileReader(OUT_FILE_TXT))) {
            String pictures;
            int count = 1;
            try {
                while ((pictures = picturesFile.readLine()) != null) {
                    downloadUsingNIO(pictures, PICTURES_PICTURES + String.valueOf(count) + ".jpg");
                    count++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void downloadUsingNIO(String strUrl, String file) throws IOException {
        URL url = new URL(strUrl);
        ReadableByteChannel byteChannel = Channels.newChannel(url.openStream());
        FileOutputStream stream = new FileOutputStream(file);
        stream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
        stream.close();
        byteChannel.close();
    }
}

