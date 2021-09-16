package com.company.blogcapstone.controller;

import com.company.blogcapstone.dao.AuthorRepository;
import com.company.blogcapstone.dto.Author;
import com.company.blogcapstone.dto.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
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

    @PostMapping("login")
    public ModelAndView addTeacher(@RequestParam String email, @RequestParam String password, HttpServletRequest request ) {
        System.out.println(email);
        System.out.println(password);

        if (verifyEmail(email) && verifyPassword(password)) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("login");
            return modelAndView;
        } 
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
