package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.GeneralDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "generaldocuments")
public interface GeneralDocumentRepository extends JpaRepository<GeneralDocument,Integer> {
}
