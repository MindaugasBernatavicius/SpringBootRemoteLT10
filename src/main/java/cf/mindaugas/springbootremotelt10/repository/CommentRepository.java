package cf.mindaugas.springbootremotelt10.repository;

import cf.mindaugas.springbootremotelt10.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {}