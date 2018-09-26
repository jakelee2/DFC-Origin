package org.dbadmin.web;

/**
 * Created by henrynguyen on 6/22/16.
 */
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.dbadmin.model.*;
import org.dbadmin.service.TemplateService;
import org.dbadmin.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ScriptController {

    private final TemplateService templateService;

    @Autowired
    public ScriptController(TemplateService templateService) {
        this.templateService = templateService;
    }

    // Location of the script. This is got from the config file
    private static String scriptLocation = Configuration.getProperty("pythonScriptLocation");

    // Validator for single file
    @Resource(name="fileValidator")
    FileValidator fileValidator;

    // Validator for multiple uploaded files
    @Resource(name="multiFileValidator")
    MultiFileValidator multiFileValidator;

    // Set up file Validator
    @InitBinder("fileBucket")
    protected void initBinderFileBucket(WebDataBinder binder) {
        binder.setValidator(fileValidator);
    }


    @InitBinder("multiFileBucket")
    protected void initBinderMultiFileBucket(WebDataBinder binder) {
        binder.setValidator(multiFileValidator);
    }

    private String getUploadLocation() {
        return scriptLocation;
    }

    @RequestMapping(value={"/scripts"}, method = RequestMethod.GET)
    public String getScripts(ModelMap model) {

        String username = PrincipalUtils.getPrincipal();
        User current_user = templateService.findUserByUserName(username).iterator().next();

        List<String> userScripts = current_user.getScripts().stream().map(s -> s.getName()).collect(
            Collectors.toList());
        List<ExecScript> fileNames = CommandUtils.getScripts(getUploadLocation()).stream().filter(s -> userScripts.contains(s.getName())).collect(Collectors.toList());
        model.addAttribute("scripts", fileNames);
        return "scripts/scripts";
    }

    @RequestMapping(value={"/scripts_logs"}, method = RequestMethod.GET)
    public String getScriptsLogs(ModelMap model) {

        Collection<ScriptLog> logs = templateService.findScriptLogByName("");
        model.addAttribute("logs", logs);
        return "scripts/logs";
    }


    @RequestMapping(value = "/scripts/{scriptName}/TERMINATE", method = RequestMethod.GET)
    public String terminateScript(@PathVariable("scriptName") String scriptName, Model model) {
        String actionOutput = CommandUtils.executeTerminateCommand(scriptName, getUploadLocation());
        model.addAttribute("fileName", scriptName);
        model.addAttribute("action", "Terminate");
        model.addAttribute("actionOutput", actionOutput);
        return "scripts/actionSuccess";
    }

    private ScriptLog createScriptLog(String scriptName) {
        ScriptLog scriptLog = new ScriptLog();
        scriptLog.setScript_name(scriptName);
        java.util.Date started_at= new java.util.Date();
        String username = PrincipalUtils.getPrincipal();
        scriptLog.setStarted_at(new Timestamp(started_at.getTime()));
        scriptLog.setStarted_by(username);
        return scriptLog;
    }

    @RequestMapping(value = "/scripts/{scriptName}/START_AND_WAIT", method = RequestMethod.GET)
    public String startAndWaitScript(@PathVariable("scriptName") String scriptName, Model model) {

        // Create a Log
        ScriptLog scriptLog = createScriptLog(scriptName);

        // Execute the python script
        String actionOutput = CommandUtils.executeRunCommand(scriptName, getUploadLocation());
        // Set the completed Time
        scriptLog.setCompleted_at(new Timestamp((new java.util.Date()).getTime()));
        scriptLog.setOutput(actionOutput);
        templateService.save(scriptLog);

        model.addAttribute("fileName", scriptName);
        model.addAttribute("action", "Start and Wait");
        model.addAttribute("actionOutput", actionOutput);
        return "scripts/actionSuccess";
    }

    @RequestMapping(value = "/scripts/{scriptName}/START", method = RequestMethod.GET)
    public String startScript(@PathVariable("scriptName") String scriptName, Model model) {
        // Create a Log
        ScriptLog scriptLog = createScriptLog(scriptName);
        CommandUtils.executeNonBlockRunCommand(scriptName,
            getUploadLocation());

        String actionOutput = "This script has been executed in the background.";

            // Set the completed Time
        scriptLog.setCompleted_at(new Timestamp((new java.util.Date()).getTime()));
        scriptLog.setOutput(actionOutput);
        templateService.save(scriptLog);

        model.addAttribute("fileName", scriptName);
        model.addAttribute("action", "Start");
        model.addAttribute("actionOutput", actionOutput);
        return "scripts/actionSuccess";
    }

    @RequestMapping(value = "/scripts/{scriptName}/RESTART", method = RequestMethod.GET)
    public String reStartScript(@PathVariable("scriptName") String scriptName, Model model) {
        String actionOutput = CommandUtils.executeRestartCommand(scriptName, getUploadLocation());
        model.addAttribute("fileName", scriptName);
        model.addAttribute("action", "Restart");
        model.addAttribute("actionOutput", actionOutput);
        return "scripts/actionSuccess";
    }

    @RequestMapping(value = "/scripts/{scriptName}/DELETE", method = RequestMethod.GET)
    public String deleteScript(@PathVariable("scriptName") String scriptName, Model model) {
        templateService.deleteScript(scriptName);
        String actionOutput = CommandUtils.deleteFile(scriptName, getUploadLocation());
        model.addAttribute("fileName", scriptName);
        model.addAttribute("action", "Delete");
        model.addAttribute("actionOutput", actionOutput);
        return "scripts/actionSuccess";
    }

    @RequestMapping(value="/scriptUpload", method = RequestMethod.GET)
    public String getSingleUploadPage(ModelMap model) {
        FileBucket fileModel = new FileBucket();
        model.addAttribute("fileBucket", fileModel);
        return "scripts/scriptsUploader";
    }

    @RequestMapping(value="/scriptUpload", method = RequestMethod.POST)
    public String singleFileUpload(@Valid FileBucket fileBucket, BindingResult result, ModelMap model) throws IOException {

        if (result.hasErrors()) {
            System.out.println("validation errors");
            return "scripts/scriptsUploader";
        } else {
            MultipartFile multipartFile = fileBucket.getFile();

            FileCopyUtils.copy(fileBucket.getFile().getBytes(), new File(getUploadLocation() + fileBucket.getFile().getOriginalFilename()));

            ExecScript script = new ExecScript(fileBucket.getFile().getOriginalFilename());


            String username = PrincipalUtils.getPrincipal();
            User current_user = templateService.findUserByUserName(username).iterator().next();
            templateService.saveScript(script, current_user);

            String fileName = multipartFile.getOriginalFilename();
            model.addAttribute("fileName", fileName);
            model.addAttribute("action", "uploaded");
            model.addAttribute("actionOutput", "");
            return "scripts/actionSuccess";
        }
    }


}
