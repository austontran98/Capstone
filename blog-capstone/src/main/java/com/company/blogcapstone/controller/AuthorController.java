package com.company.blogcapstone.controller;

import com.company.blogcapstone.dao.AuthorRepository;
import com.company.blogcapstone.dto.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/author")
public class AuthorController {
    @Autowired
    AuthorRepository authorRepo;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Author addAuthor(@RequestBody Author newAuthor){
        Author savedAuthor = authorRepo.save(newAuthor);
        final int authorId = savedAuthor.getId();
        if(newAuthor.getPosts() != null){
            newAuthor.getPosts().forEach(post -> post.setAuthorId(authorId));
        }
        savedAuthor = authorRepo.save(newAuthor);
        return savedAuthor;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Author> getAllAuthors(){
        return authorRepo.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Author getAuthorById(@PathVariable int id){
        Optional<Author> author = authorRepo.findById(id);
        if(author.isPresent()){
            return author.get();
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthor(@PathVariable int id){
        authorRepo.deleteById(id);
    }
}