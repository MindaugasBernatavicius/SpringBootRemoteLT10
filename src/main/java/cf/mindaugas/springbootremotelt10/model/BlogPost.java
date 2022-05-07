package cf.mindaugas.springbootremotelt10.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

// @Getter
// @Setter
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(exclude = "comments")
public class BlogPost {
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