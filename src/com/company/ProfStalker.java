package com.company;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class ProfStalker {
    private URL professorUrl;
    private String professorName;
    private String professorId;

    private String readInputFromCommandLine() {
        System.out.println("Please input email ID of the person you are looking for.");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String keyboardInput;

        try {
            keyboardInput = in.readLine();
        } catch (IOException e) {
            System.out.println("An error occurred while reading the input.");
            System.out.println("Please try running the program again.");

            return readInputFromCommandLine();
        }

        return keyboardInput;
    }

    private void readProfessorId() {
        professorId = readInputFromCommandLine();
    }

    private void findFullProfessorName() {
        String baseUrl = "http://www.ecs.soton.ac.uk/people/";
        String fullUrl = baseUrl + professorId;

        try {
            professorUrl = new URL(fullUrl);
        } catch (MalformedURLException e) {
            System.out.println("Cannot find the url of the id you entered.");
            System.out.println("The program will terminate");
            System.exit(0);
        }

        try {
            professorName = parseUrl(professorUrl);
        } catch (IOException e) {
            System.out.println("There was a problem while parsing the url.");
            System.out.println("The program will terminate");
            System.exit(0);
        }
    }

    private String parseUrl(URL professorUrl) throws IOException {
        BufferedReader UrlInputReader = new BufferedReader(
                new InputStreamReader(professorUrl.openStream()));

        String inputLine;
        professorName = "";
        String className = "uos-page-title uos-main-title uos-page-title-compressed";
        while ((inputLine = UrlInputReader.readLine()) != null) {
            //find name of prof
            int index;
            if((index = inputLine.lastIndexOf(className)) != -1) {
                while(inputLine.charAt(index) != '>') {
                    index++;
                }
                index++;

                while(inputLine.charAt(index) != '<') {
                    professorName += inputLine.charAt(index);
                    index++;
                }

                break;
            }
        }

        return professorName;
    }

    private void printOutput() {
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out))) {
            out.newLine();
            out.write(professorName, 0, professorName.length());
            out.newLine();
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println("There was a problem while printing the output.");
            System.out.println("The program will terminate");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        ProfStalker profStalker = new ProfStalker();
        profStalker.readProfessorId();
        profStalker.findFullProfessorName();
        profStalker.printOutput();
    }
}