package org.example.springdemomentor.repository;

import org.example.springdemomentor.model.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class BookRepository {
    private final List<Book> books = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<Book> findAll() {
        return new ArrayList<>(books);
    }

    public Optional<Book> findById(Long id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
    }

    public Book save(Book book) {
        book.setId(idGenerator.getAndIncrement());
        books.add(book);
        return book;
    }

    public Book update(Long id, Book updatedBook) {
        Optional<Book> existingBook = findById(id);
        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setGenre(updatedBook.getGenre());
            book.setStock(updatedBook.getStock());
            book.setPrice(updatedBook.getPrice());
            return book;
        }
        return null;
    }

    public boolean deleteById(Long id) {
        return books.removeIf(book -> book.getId().equals(id));
    }
}