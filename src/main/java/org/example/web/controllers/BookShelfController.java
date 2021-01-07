package org.example.web.controllers;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.BookAuthorToSearch;
import org.example.web.dto.BookIdToRemove;
import org.example.web.dto.BookAuthorToRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLEncoder;

@Controller
@RequestMapping(value = "/books")
@Scope("singleton")
public class BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfController.class);

    private BookService bookService;
    public static File serverFile;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info(this.toString());
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookAuthorToRemove", new BookAuthorToRemove());
        model.addAttribute("bookAuthorToSearch", new BookAuthorToSearch());
        model.addAttribute("bookList", bookService.getAllBooks());
        logger.info(model);
        return "book_shelf";
    }


    @PostMapping("/searchbyauthor")
    public String searchBookByAuthor(@Valid BookAuthorToSearch bookAuthorToSearch, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.info(" bindingResult has error in searchController");
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookAuthorToRemove", new BookAuthorToRemove());
            model.addAttribute("bookList", bookService.getAllBooks());
            logger.info(model);
            return "book_shelf";
        } else {
            logger.info(" bindingResult has NOT error");
            bookService.searchBookByAuthor(bookAuthorToSearch.getAuthorForSearch());
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookAuthorToRemove", new BookAuthorToRemove());
            model.addAttribute("bookAuthorToSearch", new BookAuthorToSearch());
            model.addAttribute("bookList", bookService.searchBookByAuthor(bookAuthorToSearch.getAuthorForSearch()));
            return "book_shelf_by_search";

        }
    }

    @PostMapping("/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookAuthorToRemove", new BookAuthorToRemove());
            model.addAttribute("bookAuthorToSearch", new BookAuthorToSearch());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            bookService.saveBook(book);
            logger.info("current repository size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/removebyauthor")
    public String removeBookByAuthor(@Valid BookAuthorToRemove bookAuthorToRemove, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.info(" bindingResult has error");
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookAuthorToSearch", new BookAuthorToSearch());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            logger.info(" bindingResult has NOT error");
            bookService.removeBookByAuthor(bookAuthorToRemove.getAuthor());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove")
    public String removeBook(@Valid BookIdToRemove bookIdToRemove, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.info(" bindingResult has error" + bindingResult.toString());
            model.addAttribute("book", new Book());
            model.addAttribute("bookAuthorToRemove", new BookAuthorToRemove());
            model.addAttribute("bookAuthorToSearch", new BookAuthorToSearch());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            logger.info(" bindingResult has NOT error");
            bookService.removeBookById(bookIdToRemove.getId());
            return "redirect:/books/shelf";
        }
    }

    @RequestMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {

        if (file.isEmpty()) {
            return "errors/500";
        } else {
            String name = file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            //create dir
            String rootPath = System.getProperty("catalina.home");
            File dir = new File(rootPath + File.separator + "external_uploads");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //create file
            File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            stream.write(bytes);
            request.getSession().setAttribute("serverFile", serverFile);

            stream.close();

            logger.info("new file saved at: " + serverFile.getAbsolutePath());

            return "redirect:/books/shelf";
        }
    }

    @RequestMapping("/download")
    public ResponseEntity<Object> downloadFile(HttpServletRequest request) throws Exception {
        serverFile = (File) request.getSession().getAttribute("serverFile");

        logger.info("start download " + serverFile.getAbsolutePath());

        File downloadfile = new File(serverFile.getAbsolutePath());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(downloadfile));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", downloadfile.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(downloadfile.length()).contentType(
                MediaType.parseMediaType("application/txt")).body(resource);
        logger.info("finish download");
        return responseEntity;
    }
}





