package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.GeneralDocument_Acount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "generaldocument_account")
public interface GeneralDocument_AccountRepository extends JpaRepository<GeneralDocument_Acount, Integer> {
}
