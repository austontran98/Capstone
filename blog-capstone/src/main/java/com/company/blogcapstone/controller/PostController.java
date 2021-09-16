package com.company.blogcapstone.controller;

import com.company.blogcapstone.dao.PostRepository;
import com.company.blogcapstone.dto.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    @GetMapping("/createPost")
    public ModelAndView index2(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("postObject", new Post());
        modelAndView.setViewName("createPost");
        return modelAndView;
    }

    @PostMapping("/createPost")
    @ResponseBody
    public ModelAndView index3(Post newPost, Model model) {
        Post savedPost = postRepo.save(newPost);
        final int postId = savedPost.getId();
        if (newPost.getCategories() != null) {
            newPost.getCategories().forEach(category -> category.getPosts().add(newPost));
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @GetMapping()
    public ModelAndView index(Model model) {
        System.out.println(postRepo.findAll());

        model.addAttribute("postList", postRepo.findAll());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("posts");
        return modelAndView;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView getPostById(Model model, @PathVariable int id){
        Optional<Post> post = postRepo.findById(id);
        Post returnedPost;
        if(post.isPresent()){
            returnedPost = post.get();
        } else {
            returnedPost = null;
        }

        model.addAttribute("postId", returnedPost);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("postId");
        return modelAndView;
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

    @RequestMapping("/delete")
    public ModelAndView delete(@ModelAttribute(value = "post2") Post newPost, Model model) {
        postRepo.delete(newPost);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }
    
    @GetMapping("/editPost")
    public ModelAndView edit(Model model){
        
        ModelAndView modelAndView = new ModelAndView();
       // modelAndView.addObject("postObject", getPostById(10));
        modelAndView.setViewName("editPost");
        return modelAndView;
        
    }
     
    @PostMapping("/editPost")
    @ResponseBody
    public ModelAndView edit1 (Post newPost, Model model){
        Post savedPost = postRepo.save(newPost);
        final int postId = savedPost.getId();
        if(newPost.getCategories() != null){
            newPost.getCategories().forEach(category -> category.getPosts().add(newPost));
        }
        
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }
    
    @GetMapping("/author")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView getPostByAuthorId(Model model, @RequestParam int id){
        List<Post> postByAuthor = postRepo.findByAuthorId(id);
        model.addAttribute("postList", postByAuthor);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("posts");
        return modelAndView;
    }
    //
//    @GetMapping("/posts")
//    public String testConditional(Model model) {
//        
//        return "posts.html"; 
//    }

}
