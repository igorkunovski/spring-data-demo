package com.example.api;

import com.example.entity.*;
import com.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ui")
public class UiController {

    BookService bookService;
    ReaderService readerService;
    IssueService issueService;

    @Autowired
    public UiController(BookService bookService, ReaderService readerService, IssueService issueService) {
        this.bookService = bookService;
        this.readerService = readerService;
        this.issueService = issueService;
    }

    @GetMapping("/booklist")
    public String booklist(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "booklist";
    }

    @GetMapping("/readerlist")
    public String readerlist(Model model) {
        List<Reader> readers = readerService.findAll();
        model.addAttribute("readers", readers);
        return "readerlist";
    }

    @GetMapping("/issuetable")
    public String issuetable(Model model) {
        List<Issue> issues = issueService.findAll();
        model.addAttribute("issues", issues);
        return "issuetable";
    }

    @GetMapping("/reader/{id}")
    public String readerbooks(@PathVariable Long id, Model model) {
        Optional<Reader> reader = readerService.findById(id);
        if (reader.isPresent()) {
            model.addAttribute("readerName", reader.get().getName());
            model.addAttribute("books", reader.get().getBookList());
        }
        return "readerbooklist";
    }
}
