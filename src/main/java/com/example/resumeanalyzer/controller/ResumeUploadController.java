package com.example.resumeanalyzer.controller;

import com.example.resumeanalyzer.service.GroqService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ResumeUploadController {

    @Autowired
    private GroqService groqService;

    @PostMapping("/upload")
    public String uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("jobDescription") String jobDescription) {

        try {
            if (file == null || file.isEmpty()) {
                return "File is empty";
            }

            PDDocument document = PDDocument.load(file.getInputStream());
            PDFTextStripper stripper = new PDFTextStripper();
            String resumeText = stripper.getText(document);
            document.close();

            resumeText = resumeText.replace("\n", " ").replace("\r", " ");
            jobDescription = jobDescription.replace("\n", " ").replace("\r", " ");

            if (resumeText.length() > 2000) {
                resumeText = resumeText.substring(0, 2000);
            }

            String prompt =
                    "You are a professional AI Resume Analyzer.\n\n" +

                            "Analyze the resume against the job description and return STRICTLY in this format:\n\n" +

                            "Match Score: XX%\n\n" +

                            "Skills Present:\n- point\n- point\n\n" +

                            "Missing Skills:\n- point\n- point\n\n" +

                            "Suggestions:\n- point\n- point\n\n" +

                            "Resume:\n" + resumeText +

                            "\n\nJob Description:\n" + jobDescription;

            return groqService.analyzeResume(prompt);

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}