package com.savstanis.btcs;

import com.savstanis.btcs.exceptions.FileFormatException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BitcoinScriptFileReader {
    public String readFile(String fileName) {
        if (!fileName.endsWith(".btcs")) {
            throw new FileFormatException();
        }

        try (var br = new BufferedReader(new FileReader(fileName))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
