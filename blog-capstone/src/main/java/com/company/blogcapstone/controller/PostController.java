package com.company.blogcapstone.controller;

import com.company.blogcapstone.dao.PostRepository;
import com.company.blogcapstone.dto.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class PostController {

    @Autowired
    PostRepository postRepo;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post addPost(@RequestBody Post newPost) {
        Post savedPost = postRepo.save(newPost);
        final int postId = savedPost.getId();
        if (newPost.getCategories() != null) {
            newPost.getCategories().forEach(category -> category.getPosts().add(newPost));
        }
        savedPost = postRepo.save(newPost);
        return savedPost;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView getAllPost(Model model) {
        List<Post> posts = postRepo.findAll();
        model.addAllAttributes(posts);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("post");
        return modelAndView;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Post getPostById(@PathVariable int id) {
        Optional<Post> post = postRepo.findById(id);
        if (post.isPresent()) {
            return post.get();
        } else {
            return null;
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePost(@RequestBody Post updatedPost) {
        postRepo.save(updatedPost);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable int id) {
        postRepo.deleteById(id);
    }
}
