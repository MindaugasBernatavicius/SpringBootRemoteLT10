package cf.mindaugas.springbootremotelt10;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.*;
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
import java.io.IOException;
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


@Repository
interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findByTitleContaining(String title); // OrderByTitleDesc

    @org.springframework.data.jpa.repository.Query(
            value = "SELECT p FROM BlogPost p ORDER BY p.title ASC")
    List<BlogPost> findAllBlogPosts();
}
// interface BlogPostRepository extends CrudRepository<BlogPost, Long> {}
// interface BlogPostRepository extends PagingAndSortingRepository<BlogPost, Long> {}

@Repository
interface CommentRepository extends JpaRepository<Comment, Long> {}

// @Getter
// @Setter
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(exclude = "comments")
class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private @NonNull String title;
    private @NonNull String text;
    // @JsonIgnore - this is a very powerfull annotation,
    // ... too powerful as you will not see this field ar all in JSON anymore
    private @NonNull String blah;

    // @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.DATE)
    @CreationTimestamp // generate current date
    private Date date;

    @OneToMany(mappedBy = "blogPost")
    //@JoinColumn(name="bp_id")
    // ... avoiding join table on 1:M, see: https://stackoverflow.com/a/2096971/1964707
    @JsonSerialize(using = CommentsSerializer.class)
    private Set<Comment> comments;

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private @NonNull String text;

    @ManyToOne
    @JoinColumn(name="bp_id")
    @JsonSerialize(using = BlogpostSerializer.class)
    private @NonNull BlogPost blogPost;
}


@Service
class DbInit implements CommandLineRunner {

    @Autowired
    private BlogPostRepository bpr;

    @Autowired
    private CommentRepository cr;

    @Override
    public void run(String... args) {
        // Delete all in the beginning
        this.cr.deleteAll();
        this.bpr.deleteAll();

        // Create initial dummy blogposts
        var bp1 = new BlogPost("Snowboard", "A", "blah");
        this.bpr.save(bp1);

        // When @OneToMany is used on BlogPost
        // ... using hybernate sequence mechanism for id generation, we should not hardcode Ids
        var c1 = new Comment("Comm1", bp1);
        var c2 = new Comment("Comm2", bp1);

        this.cr.saveAll(Arrays.asList(c1, c2));

        // Cannot add or update a child row: a foreign key constraint fails (`sdaremotelt10`.`blog_post_comments`)
        // ... comments need to created in comments table, before blog_post_comments table can establish a relation to it
        // ... this should be done only when using unidirectional relationship, not bidirectional
        // bp1.setComments(new HashSet<>(){{ add(c1); add(c2); }});

        System.out.println(bp1);

        var bp2 = new BlogPost("Kittens", "A", "blah");
        var bp3 = new BlogPost("sdvdvdsvdsv", "A", "blsdvdsvdh");

        // When seeding with @ManyToOne on Comments side
        // var c1 = new Comment(1L, "Comm1", bp1);
        // var c2 = new Comment(1L, "Comm2", bp1);

        // Save to db
        this.bpr.saveAll(Arrays.asList(bp2, bp3));
        // this.cr.saveAll(Arrays.asList(c1, c2));
    }
}

class CommentsSerializer extends StdSerializer<Set<Comment>> {
    public CommentsSerializer() {
        this(null);
    }
    public CommentsSerializer(Class<Set<Comment>> t) {
        super(t);
    }

    @Override
    public void serialize(
            Set<Comment> comments,
            JsonGenerator generator,
            SerializerProvider provider
    )
            throws IOException, JsonProcessingException
    {
//        Set<Long> ids = new HashSet<>();
//        for (Comment comment : comments)
//            ids.add(comment.getId());
//        generator.writeObject(ids);

        List<Map<Long, String>> idToTitleMaps = new ArrayList<>();
        for (Comment comment : comments)
            idToTitleMaps.add(new HashMap<>(){{ put(comment.getId(), comment.getText()); }});
        generator.writeObject(idToTitleMaps);
    }
}

class BlogpostSerializer extends StdSerializer<BlogPost> {
    public BlogpostSerializer() {
        this(null);
    }
    public BlogpostSerializer(Class<BlogPost> t) {
        super(t);
    }

    @Override
    public void serialize(
            BlogPost blogPost,
            JsonGenerator jgen,
            SerializerProvider provider)
            throws IOException, JsonProcessingException
    {
        jgen.writeStartObject();
        jgen.writeNumberField("id", blogPost.getId());
        jgen.writeStringField("title", blogPost.getTitle());
        // jgen.writeStringField("text", blogPost.getText());
        jgen.writeEndObject();
    }
}