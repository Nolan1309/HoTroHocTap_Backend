package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.User.GeneralDocumentAccountDTO_User;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.GeneralDocumentRepository;
import com.example.hotrohoctapbackend.dao.GeneralDocument_AccountRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.GeneralDocument;
import com.example.hotrohoctapbackend.entity.GeneralDocument_Acount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GeneralDocumentsAccountService {
    @Autowired
    private GeneralDocument_AccountRepository generalDocumentAccountRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private GeneralDocumentRepository generalDocumentRepository;

    public GeneralDocument_Acount saveDownload(GeneralDocumentAccountDTO_User generalDocumentAccountDTO_user) {
        GeneralDocument_Acount generalDocumentAcount = new GeneralDocument_Acount();
        generalDocumentAcount.setDateDownload(generalDocumentAccountDTO_user.getDateDownload());

        // Fetch and validate Account
        Account account = accountRepository.findById(generalDocumentAccountDTO_user.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + generalDocumentAccountDTO_user.getAccountId()));

        // Fetch and validate GeneralDocument
        GeneralDocument generalDocument = generalDocumentRepository.findById(generalDocumentAccountDTO_user.getGeneralDocumentId())
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + generalDocumentAccountDTO_user.getGeneralDocumentId()));
        generalDocumentAcount.setAccount(account);
        generalDocumentAcount.setGeneralDocument(generalDocument);
        return generalDocumentAccountRepository.save(generalDocumentAcount);
    }
}
