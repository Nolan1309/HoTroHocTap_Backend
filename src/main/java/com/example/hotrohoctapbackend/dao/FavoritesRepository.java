package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "favorites")
public interface FavoritesRepository extends JpaRepository<Favorites,Integer> {
}
