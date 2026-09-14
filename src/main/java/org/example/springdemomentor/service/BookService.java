package org.example.springdemomentor.service;

import org.example.springdemomentor.exception.BookNotFoundException;
import org.example.springdemomentor.exception.InvalidBookDataException;
import org.example.springdemomentor.model.Book;
import org.example.springdemomentor.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id " + id));
    }

    public List<Book> searchByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository.findAll().stream()
                .filter(b -> b.getGenre() != null && b.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    public Book addBook(Book book) {
        validateBook(book);
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book book) {
        validateBook(book);
        Book updated = bookRepository.update(id, book);
        if (updated == null) {
            throw new BookNotFoundException("Book not found for update with id: " + id);
        }
        return updated;
    }

    public void deleteBook(Long id) {
        boolean removed = bookRepository.deleteById(id);
        if (!removed) {
            throw new BookNotFoundException("Book not found for deletion with id: " + id);
        }
    }

    private void validateBook(Book book) {
        if (book.getPrice() != null && book.getPrice() < 0) {
            throw new InvalidBookDataException("Book price cannot be negative!");
        }
        if (book.getStock() != null && book.getStock() < 0) {
            throw new InvalidBookDataException("Book stock cannot be negative!");
        }
    }
}