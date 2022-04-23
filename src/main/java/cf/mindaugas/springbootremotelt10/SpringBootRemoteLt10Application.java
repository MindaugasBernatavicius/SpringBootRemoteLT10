package cf.mindaugas.springbootremotelt10;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.util.*;
// import java.util.List;
// import java.util.Map;
// import java.util.HashMap;

@SpringBootApplication
public class SpringBootRemoteLt10Application {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootRemoteLt10Application.class, args);
    }
}

@Controller // MVC appsai
class GreetingController {
    @GetMapping("/") // GET / --> "Hello"
    public @ResponseBody Map<String, String> getGreeting(){
        // return "Hello !";
        // return new String[]{"Hello", "Labas"};
        Map<String, String> myMap = new HashMap<>();
        myMap.put("Jonas", "Jonaitis");
        myMap.put("Petras", "Petraitis");
        return myMap;
    }
}

@RestController // = @Controller + @ResponseBody
class GreetingController2 {
    @GetMapping("/greeting") // GET /greeting --> "Hello"
    public String getGreeting(){
        return "Hello !!!";
    }
}

@Slf4j
@RestController
@RequestMapping("/api/v1")
class BlogPostController {

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
}


@Repository
interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findByTitleContaining(String title); // OrderByTitleDesc

    @org.springframework.data.jpa.repository.Query(
            value = "SELECT p FROM BlogPost p ORDER BY p.title ASC")
    List<BlogPost> findAllBlogPosts();
}
// interface BlogPostRepository extends CrudRepository<BlogPost, Long> {}
// interface BlogPostRepository extends PagingAndSortingRepository<BlogPost, Long> {}

// @Getter
// @Setter
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private @NonNull String title;
    private @NonNull String text;
    private @NonNull String blah;

    // @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.DATE)
    @CreationTimestamp // generate current date
    private Date date;
}

@Service
class DbInit implements CommandLineRunner {

    @Autowired
    private BlogPostRepository bpr;

    @Override
    public void run(String... args) {
        // Delete all in the beginning
        this.bpr.deleteAll();

        // Create initial dummy products
        var bp1 = new BlogPost("Snowboard", "A", "blah");
        var bp2 = new BlogPost("Kittens", "A", "blah");
        var bp3 = new BlogPost("sdvdvdsvdsv", "A", "blsdvdsvdh");

        // Save to db
        this.bpr.saveAll(Arrays.asList(bp1, bp2, bp3));
    }
}
