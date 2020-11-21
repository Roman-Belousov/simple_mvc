package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/books")
public class BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfController.class);
    private BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @GetMapping("/shelf_by_search")
    public String booksSearch(Model model) {
        logger.info("found on the shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookListBySearch", bookService.getAllBooksBySearch());
        return "book_shelf_by_search";
    }
    @GetMapping("/shelf_errorpage")
    public String books1(Model model) {
        logger.info(" not got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf_errorpage";
    }

    @PostMapping("/save")
    public String saveBook(Book book) {
        bookService.saveBook(book);
        logger.info("current repository size: " + bookService.getAllBooks().size());
        return "redirect:/books/shelf";
    }
    @PostMapping("/removebyauthor")
    public String removeBookByAuthor(@RequestParam(value = "bookAuthorToRemove") String bookAuthorToRemove) {
        if (bookService.removeBookByAuthor(bookAuthorToRemove)) {
            return "redirect:/books/shelf";
        } else {
            return "redirect:/books/shelf_errorpage";
        }
    }
    @PostMapping("/listbyauthor")
    public String listBookByAuthor(@RequestParam(value = "bookAuthorToList") String bookAuthorToList) {
        if (bookService.listBookByAuthor(bookAuthorToList)) {
            return "redirect:/books/shelf_by_search";
        } else {
            return "redirect:/books/shelf_errorpage";
        }
    }
    @PostMapping("/remove")
    public String removeBook(@RequestParam(value = "bookIdToRemove") Integer bookIdToRemove) {
        if (bookService.removeBookById(bookIdToRemove)) {
            return "redirect:/books/shelf";
        } else {
            return "redirect:/books/shelf_errorpage";
        }
    }
}