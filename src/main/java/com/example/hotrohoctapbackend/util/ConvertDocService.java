package com.example.hotrohoctapbackend.util;

import org.springframework.stereotype.Service;
import com.convertapi.client.ConvertApi;
import com.convertapi.client.Param;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

@Service
public class ConvertDocService {

    public void convertDocxToPdf(String inputFilePath, String outputDirPath, String filename) throws ExecutionException, InterruptedException, IOException {
        Path inputPath = Paths.get(inputFilePath);
        Path outputPath = Paths.get(outputDirPath);

        ConvertApi.convert("docx", "pdf",
                new Param("File", inputPath),
                new Param("FileName", filename)
        ).get().saveFilesSync(outputPath);
    }
}
