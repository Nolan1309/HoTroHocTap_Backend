package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.User.GeneralDocumentAccountDTO_User;
import com.example.hotrohoctapbackend.entity.GeneralDocument_Acount;
import com.example.hotrohoctapbackend.service.GeneralDocumentsAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true") 
@RestController
@RequestMapping("/api/document-account")
public class GeneralDocumentsAccountController {
    @Autowired
    private GeneralDocumentsAccountService generalDocumentsAccountService;

    @PostMapping("/download")
    public ResponseEntity<GeneralDocument_Acount> saveDownload(@RequestBody GeneralDocumentAccountDTO_User generalDocumentAcountPayload) {
        GeneralDocument_Acount generalDocumentAcount = generalDocumentsAccountService.saveDownload(generalDocumentAcountPayload);
        return ResponseEntity.ok(generalDocumentAcount);
    }
}
