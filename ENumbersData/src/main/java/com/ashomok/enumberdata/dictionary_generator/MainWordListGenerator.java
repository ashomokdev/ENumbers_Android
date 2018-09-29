package com.ashomok.enumberdata.dictionary_generator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Iuliia on 30.11.2015.
 */
public class MainWordListGenerator {
    public static void main(String[] args) {
        try {
            String filename = "temp/wordlist.txt";
            Scanner sc = null;

            sc = new Scanner(new File(filename));

            List<String> lines = new ArrayList<String>();
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }


            //1.
            //E 200 (backspace)
            List<String> updated_lines = new ArrayList<String>();
            for (String item : lines) {
                updated_lines.add(item.replace("E", "E "));
            }
            WriteResult(filename, updated_lines);



            //2.
            //(E100) (brackets)
            updated_lines = new ArrayList<String>();
            for (String item : lines) {
                updated_lines.add("("+ item + ")");
            }
            WriteResult(filename, updated_lines);

            //3.
            //(E 100) (brackets)
            updated_lines = new ArrayList<String>();
            for (String item : lines) {
                updated_lines.add("("+ item.replace("E", "E ") + ")");
            }
            WriteResult(filename, updated_lines);

            //4.
            //E100(i)
            updated_lines = new ArrayList<String>();
            for (String item : lines) {
                updated_lines.add(item + "(i)");
            }
            WriteResult(filename, updated_lines);

            //5.
            //E100(ii)
            updated_lines = new ArrayList<String>();
            for (String item : lines) {
                updated_lines.add(item + "(ii)");
            }
            WriteResult(filename, updated_lines);

            //6.
            //E 100(i)
            updated_lines = new ArrayList<String>();
            for (String item : lines) {
                updated_lines.add(item.replace("E", "E ") + "(i)");
            }
            WriteResult(filename, updated_lines);

            //7.
            //E 100(ii)
            updated_lines = new ArrayList<String>();
            for (String item : lines) {
                updated_lines.add(item.replace("E", "E ") + "(ii)");
            }
            WriteResult(filename, updated_lines);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void WriteResult(String filename, List<String> updated_lines) throws IOException {
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)))) {
            for (String item : updated_lines) {
                out.println(item);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
