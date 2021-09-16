package com.company.blogcapstone.controller;

import com.company.blogcapstone.dao.AuthorRepository;
import com.company.blogcapstone.dto.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    AuthorRepository authorRepo;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Author addAuthor(@RequestBody Author newAuthor) {
        Author savedAuthor = authorRepo.save(newAuthor);
        final int authorId = savedAuthor.getId();
        if (newAuthor.getPosts() != null) {
            newAuthor.getPosts().forEach(post -> post.setAuthorId(authorId));
        }
        savedAuthor = authorRepo.save(newAuthor);
        return savedAuthor;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Author> getAllAuthors() {
        return authorRepo.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Author getAuthorById(@PathVariable int id) {
        Optional<Author> author = authorRepo.findById(id);
        if (author.isPresent()) {
            return author.get();
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthor(@PathVariable int id) {
        authorRepo.deleteById(id);
    }

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView loginAuthor() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/login");
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView loginAuthorForm(@RequestParam String email, @RequestParam String password, Model model, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            model.addAttribute("notice", "Email address and password must be filled in order to login!");
            modelAndView.setViewName("/login");
            return modelAndView;
        }

        if (!verifyEmail(email)) {
            model.addAttribute("notice", "Email address is not recognized!");
            modelAndView.setViewName("/login");
            return modelAndView;
        }

        if (!verifyPassword(password)) {
            model.addAttribute("notice", "Password is wrong!");
            modelAndView.setViewName("/login");
            return modelAndView;
        }

        Author author = getAllAuthors().stream().filter((a) -> a.getEmail().toLowerCase().equals(email.toLowerCase())).findFirst().get();
        request.getSession().setAttribute("author", author);
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    private boolean verifyEmail(String email) {
        List<Author> authors = getAllAuthors();
        for (Author author : authors) {
            if (author.getEmail().toLowerCase().contains(email.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private boolean verifyPassword(String password) {
        List<Author> authors = getAllAuthors();
        for (Author author : authors) {
            if (author.getPassword().toLowerCase().contains(password.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

}
