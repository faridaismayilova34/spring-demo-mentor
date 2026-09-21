package org.example.springdemomentor.service;

import org.example.springdemomentor.entity.BookEntity;
import org.example.springdemomentor.exception.BookNotFoundException;
import org.example.springdemomentor.exception.InvalidBookDataException;
import org.example.springdemomentor.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    public BookEntity getBookById(Integer id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id " + id));
    }

    public List<BookEntity> searchByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository.findByGenreIgnoreCase(genre);
    }

    public BookEntity addBook(BookEntity book) {
        validateBook(book);
        return bookRepository.save(book);
    }

    public BookEntity updateBook(Integer id, BookEntity book) {
        validateBook(book);
        BookEntity existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found for update with id: " + id));

        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setGenre(book.getGenre());
        existingBook.setStock(book.getStock());
        existingBook.setPrice(book.getPrice());

        return bookRepository.save(existingBook);
    }

    public void deleteBook(Integer id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book not found for deletion with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    private void validateBook(BookEntity book) {
        if (book.getPrice() != null && book.getPrice() < 0) {
            throw new InvalidBookDataException("Book price cannot be negative!");
        }
        if (book.getStock() != null && book.getStock() < 0) {
            throw new InvalidBookDataException("Book stock cannot be negative!");
        }
    }
}