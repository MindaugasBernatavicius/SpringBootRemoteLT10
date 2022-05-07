package cf.mindaugas.springbootremotelt10.repository;

import cf.mindaugas.springbootremotelt10.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findByTitleContaining(String title); // OrderByTitleDesc

    @org.springframework.data.jpa.repository.Query(
            value = "SELECT p FROM BlogPost p ORDER BY p.title ASC")
    List<BlogPost> findAllBlogPosts();
}
// interface BlogPostRepository extends CrudRepository<BlogPost, Long> {}
// interface BlogPostRepository extends PagingAndSortingRepository<BlogPost, Long> {}