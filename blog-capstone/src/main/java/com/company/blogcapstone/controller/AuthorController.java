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
@RequestMapping("/author")
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

    @PostMapping("loginAuthor")
    public ModelAndView addTeacher(HttpServletRequest request) {
        String email = request.getParameter("emailLogin");
        String password = request.getParameter("passwordLogin");
        
        List<Author> author = getAllAuthors();
        
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/post");
        
        return modelAndView;
    }
}
