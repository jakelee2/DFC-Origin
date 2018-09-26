package org.dbadmin.util;

import org.dbadmin.model.ExecScript;
import org.dbadmin.model.ScriptState;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by henrynguyen on 6/22/16.
 */
public class CommandUtils {

    /**
     * Returns a list of scripts from a file Location
     * @param fileLoc location of the python scripts
     * @return a List of scripts
     */
    public static List<ExecScript> getScripts(String fileLoc) {
        List<String> fileNames = getFileNames(fileLoc);
        List<ExecScript> scripts = fileNames.stream().filter(f -> f.endsWith(".py")).map(
            f -> {
                String pid = getScriptPID(f, fileLoc);
                ScriptState state = pid.compareToIgnoreCase("") == 0 ? ScriptState.STOPPED : ScriptState.RUNNING;
                return new ExecScript(f, state);
            })
            .collect(Collectors.toList());
        return scripts;
    }

    /**
     * Returns a list of files from a file location
     * @param fileLoc location of python scripts
     * @return a list of file Name.
     */
    public static List<String> getFileNames(String fileLoc){
        File rootFolder = new File(fileLoc);
        List<String> fileNames = Arrays.stream(rootFolder.listFiles())
            .map(f -> f.getName())
            .collect(Collectors.toList());
        return fileNames;
    }

    /**
     * Run the python script
     * @param scriptName script name
     * @param fileLoc script path
     * @return output of the script
     */
    public static String executeRunCommand(String scriptName, String fileLoc) {
        String command = "python " + fileLoc + scriptName;
        String actionOutput = executeCommand(command);
        return actionOutput;
    }

    /**
     * Run the pythons script in background mode
     * @param scriptName script name
     * @param fileLoc script path location
     * @return output of the execution
     */
    public static String executeNonBlockRunCommand(String scriptName, String fileLoc) {
        String command = "python " + fileLoc+scriptName + " & ";
        String actionOutput = executeNonBlockCommand(command);
        return actionOutput;
    }

    /**
     * restart a script. First stop that script and then run it in background
     * @param scriptName script name
     * @param fileLoc script location
     * @return output of restart scripts
     */
    public static String executeRestartCommand(String scriptName, String fileLoc) {
        executeTerminateCommand(scriptName,fileLoc);
        return executeNonBlockRunCommand(scriptName,fileLoc);
    }

    /**
     * Delete a file from a certain location
     * @param scriptName file name that will be deleted
     * @param fileLoc file location
     * @return output of the file deletion.
     */
    public static String deleteFile(String scriptName, String fileLoc) {
        try{
            File file = new File(fileLoc + scriptName);
            if(file.delete()){
                return file.getName() + " is deleted!";
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return "Delete operation is failed.";
    }

    /**
     * Return Process ID of a running script
     * @param scriptName script name
     * @param fileLoc script location
     * @return process ID
     */
    public static String getScriptPID(String scriptName, String fileLoc) {
        String command = "sh "+ fileLoc + "checkScript.sh "+scriptName;
        String actionOutput = executeCommand(command);
        return actionOutput;
    }

    /**
     * Terminate a running script
     * @param scriptName script name
     * @param fileLoc script folder location
     * @return output of terminate process
     */
    public static String executeTerminateCommand(String scriptName, String fileLoc) {
        String command = "sh "+ fileLoc + "stopScript.sh "+scriptName;
        String actionOutput = executeCommand(command);
        return actionOutput;
    }

    /**
     * run the command in the background
     * @param command command to be run
     * @return output of execution.
     */
    public static String executeNonBlockCommand(String command) {
        try {
            Process p = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Run the command
     * @param command command to be run
     * @return output of the command
     */
    public static String executeCommand(String command) {
        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }
}
