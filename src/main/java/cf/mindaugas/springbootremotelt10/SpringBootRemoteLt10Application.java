package cf.mindaugas.springbootremotelt10;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/api/blogposts")
    public ResponseEntity<List<BlogPost>> getAll(){
        return new ResponseEntity<>(bpr.findAll(), HttpStatus.OK);
    }

    @GetMapping("/api/blogposts/{id}")
    public ResponseEntity<BlogPost> getById(@PathVariable Long id){
        log.info(">>>" + id);
        var bp = bpr.findById(id).get();
        return new ResponseEntity<>(bp, HttpStatus.OK);
    }

    @PostMapping("/api/blogposts")
    public ResponseEntity<Void> create(@RequestBody BlogPost blogPost){
        bpr.save(blogPost);
        return new ResponseEntity<>(HttpStatus.CREATED); // 201
    }

    @PutMapping("/api/blogposts/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody BlogPost blogPost){
        var bp = bpr.findById(id).get();
        bp.setText(blogPost.getText());
        bp.setTitle(blogPost.getTitle());
        bpr.save(bp);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    }

    @DeleteMapping("/api/blogposts/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        bpr.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    }
}


@Repository
interface BlogPostRepository extends JpaRepository<BlogPost, Long> {}


// @Getter
// @Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String text;
}