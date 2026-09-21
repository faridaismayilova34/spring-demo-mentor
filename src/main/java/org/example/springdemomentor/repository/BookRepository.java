package org.example.springdemomentor.repository;

import org.example.springdemomentor.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    List<BookEntity> findByGenreIgnoreCase(String genre);
}

