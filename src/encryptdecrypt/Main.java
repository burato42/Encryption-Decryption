package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;


public class Main {

    private static String readFromFile(String path) {
        File file = new File(path);
        String toModify = "";

        try (Scanner scanner = new Scanner(file)) {
            toModify = scanner.nextLine();
        } catch (FileNotFoundException e) {
            System.out.println("Error. No file found: " + path);
        }

        return toModify;
    }

    private static void writeToFile(String path, String data) {
        File file = new File(path);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(data);
        } catch (IOException e) {
            System.out.println("Error. No file found: " + path);
        }
    }

    public static void main(String[] args) {
        String parameterKey = null;
        String parameterValue;
        String algorithm;
        int offset;
        String data;
        String result;

        HashMap<String, String> params = new HashMap<>();
        for (String s : args) {
            if (s.startsWith("-")) {
                parameterKey = s;
            } else {
                parameterValue = s;
                params.put(parameterKey, parameterValue);
            }
        }

        // Get offset
        if (params.containsKey("-key")) {
            offset = Integer.parseInt(params.get("-key"));
        } else {
            offset = 0;
        }

        // Get data to decode/encode
        if (!params.containsKey("-data") && !params.containsKey("-in")) {
            data = "";
        } else if (params.containsKey("-data")) {
            data = params.get("-data");
        } else if (params.containsKey("-in")) {
            data = readFromFile(params.get("-in"));
            if (data.equals("")) {
                System.out.println("Error. File is not found");
                return;
            }
        } else {
            System.out.println("Error. Data is not present");
            return;
        }

        algorithm = params.getOrDefault("-alg", "shift");

        EncoderFactory encoderFactory = new EncoderFactory(algorithm);
        EncoderDecoder encoderDecoder = encoderFactory.getInstance();

        // Get a resulting string
        if (!params.containsKey("-mode") || params.get("-mode").equals("enc")) {
            result = encoderDecoder.encode(data, offset);
        } else {
            result = encoderDecoder.decode(data, offset);
        }

        if (!params.containsKey("-out")) {
            System.out.println(result);
        } else {
            writeToFile(params.get("-out"), result);
        }
    }
}
