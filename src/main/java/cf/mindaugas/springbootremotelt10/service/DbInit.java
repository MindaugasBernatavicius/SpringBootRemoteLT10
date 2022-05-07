package cf.mindaugas.springbootremotelt10.service;

import cf.mindaugas.springbootremotelt10.model.BlogPost;
import cf.mindaugas.springbootremotelt10.model.Comment;
import cf.mindaugas.springbootremotelt10.repository.BlogPostRepository;
import cf.mindaugas.springbootremotelt10.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DbInit implements CommandLineRunner {

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