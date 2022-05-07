package cf.mindaugas.springbootremotelt10.controller;

import cf.mindaugas.springbootremotelt10.model.BlogPost;
import cf.mindaugas.springbootremotelt10.model.Comment;
import cf.mindaugas.springbootremotelt10.repository.BlogPostRepository;
import cf.mindaugas.springbootremotelt10.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class BlogPostController {

    // // fake database
    // List<BlogPost> blogposts = new ArrayList<>() {{
    //     add(new BlogPost(1L, "Weather is nice", "Sunny and awesome"));
    //     add(new BlogPost(2L, "Crypto is down", "Markets are red"));
    // }};

    // @GetMapping("/api/blogposts")
    // public ResponseEntity<List<BlogPost>> getAll(){
    //     return new ResponseEntity<>(blogposts, HttpStatus.OK);
    // }
    //
    // @GetMapping("/api/blogposts/{id}")
    // public ResponseEntity<BlogPost> getById(@PathVariable int id){
    //     log.info(">>>" + id);
    //     var blogpost = blogposts.stream().filter(bp -> bp.getId() == id).findFirst().get();
    //     return new ResponseEntity<>(blogpost, HttpStatus.OK);
    // }
    //
    // @PostMapping("/api/blogposts")
    // public ResponseEntity<Void> create(@RequestBody BlogPost blogPost){
    //     blogposts.add(blogPost);
    //     return new ResponseEntity<>(HttpStatus.CREATED); // 201
    // }
    //
    // @PutMapping("/api/blogposts/{id}")
    // public ResponseEntity<Void> update(@PathVariable int id, @RequestBody BlogPost blogPost){
    //     var blogpostToUpdate = blogposts.stream().filter(bp -> bp.getId() == id).findFirst().get();
    //     blogpostToUpdate.setTitle(blogPost.getTitle());
    //     blogpostToUpdate.setText(blogPost.getText());
    //     return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    // }
    //
    // @DeleteMapping("/api/blogposts/{id}")
    // public ResponseEntity<Void> deleteById(@PathVariable int id){
    //     var blogpostToRemove = blogposts.stream().filter(bp -> bp.getId() == id).findFirst().get();
    //     blogposts.remove(blogpostToRemove);
    //     return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    // }


    @Autowired
    private BlogPostRepository bpr;

    @Autowired
    private CommentRepository cr;

    // @GetMapping("/api/blogposts")
    // public ResponseEntity<List<BlogPost>> getAll(){
    //     return new ResponseEntity<>(bpr.findAll(), HttpStatus.OK);
    // }

    @GetMapping(path = "/blogposts")
    public Iterable<BlogPost> getAllWithTitle(@RequestParam(value="title", required = false) String title){
        if (title == null) {
            return bpr.findAll();
        } else {
            List<BlogPost> blogPosts = bpr.findByTitleContaining(title);
            if (blogPosts.size() != 0) return blogPosts;
            else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/blogposts/{id}")
    public ResponseEntity<BlogPost> getById(@PathVariable Long id){
        log.info(">>>" + id);
        var bp = bpr.findById(id);
        return bp.isPresent()
                ? new ResponseEntity<>(bp.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        // return bpr.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/blogposts")
    public ResponseEntity<Void> create(@RequestBody BlogPost blogPost){
        bpr.save(blogPost);
        return new ResponseEntity<>(HttpStatus.CREATED); // 201
        // ... another option:
        // ResponseEntity<BlogPost> + return new ResponseEntity<>(bpr.save(blogPost), HttpStatus.OK); // 200
        // See: https://stackoverflow.com/a/28951049/1964707
    }

    @PutMapping("/blogposts/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody BlogPost blogPost){
        var bp = bpr.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        bp.setText(blogPost.getText());
        bp.setTitle(blogPost.getTitle());
        bpr.save(bp);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    }

    @DeleteMapping("/blogposts/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        try {
            bpr.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    }

    // ... get the blogpost from a particular comment
    @GetMapping("/comment/{id}")
    public ResponseEntity<Comment> getCommentWithBlogPost(@PathVariable Long id){
        var comm = cr.findById(id).get();
        return new ResponseEntity<>(comm, HttpStatus.OK);
    }
}