package ntukhpi.semit.dde.studentsdata.controller;

import ntukhpi.semit.dde.studentsdata.entity.AcademicGroup;
import ntukhpi.semit.dde.studentsdata.files.ExcelUtilities;
import ntukhpi.semit.dde.studentsdata.service.EmailSender;
import ntukhpi.semit.dde.studentsdata.service.interf.AcademicGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ExcelController {
    private final AcademicGroupService academicGroupService;

    @Autowired
    public ExcelController(AcademicGroupService academicGroupService) {
        this.academicGroupService = academicGroupService;
    }

    @PostMapping("/groups/download_students")
    public ResponseEntity<byte[]> downloadStudents(@RequestParam Long id, @RequestParam String report_form) throws IOException {
        AcademicGroup groupFromDB = academicGroupService.getAcademicGroupById(id);

        if (groupFromDB == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        String agName = groupFromDB.getGroupName();
        String fullSavePath = getFullSavePath();
        String fullFileName = ExcelUtilities.saveToWBExcelWithName(fullSavePath, agName, groupFromDB, report_form);
        System.out.println(fullFileName);
        File file = new File(fullFileName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String encodedFileName = URLEncoder.encode(file.getName(), "UTF-8");
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8"));
        headers.setContentDispositionFormData("attachment", encodedFileName);
        headers.setContentLength(file.length());

        return new ResponseEntity<>(org.apache.commons.io.FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    @PostMapping("/groups/send_students")
    public String sendStudentsEmail( @RequestParam Long id,
                                     @RequestParam String report_form,
                                     @RequestParam String email_to) throws IOException {
        AcademicGroup groupFromDB = academicGroupService.getAcademicGroupById(id);
        if (report_form.charAt(0) == 'F') {
            String agName = groupFromDB.getGroupName();
            String fullSavePath = getFullSavePath();
            File fileSaveDir = new File(fullSavePath);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdir();
            }
            String fullfilename = ExcelUtilities.saveToWBExcelWithName(fullSavePath, agName, groupFromDB, report_form);
            System.out.println(fullfilename);
            System.out.println(email_to);
            Path path = Paths.get(fullfilename);
            String filename = path.getFileName().toString();
            System.out.println(filename);
            EmailSender.sendEmail(email_to, filename, fullSavePath);
        }
        return "redirect:/groups";
    }
    private String getFullSavePath() {
        // Get the user's home directory
        String userHome = System.getProperty("user.home");

        // Define the relative path within the user's home directory where you want to save the file
        String relativePath = "/Downloads/"; // You can adjust this path as needed

        // Create the full path by combining the user's home directory and the relative path
        return userHome + relativePath;
    }
}
