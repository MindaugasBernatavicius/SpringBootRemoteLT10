package cf.mindaugas.springbootremotelt10.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private @NonNull String text;

    @ManyToOne
    @JoinColumn(name="bp_id")
    @JsonSerialize(using = BlogpostSerializer.class)
    private @NonNull BlogPost blogPost;
}