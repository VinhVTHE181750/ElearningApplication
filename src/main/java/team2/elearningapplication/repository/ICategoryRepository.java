package team2.elearningapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team2.elearningapplication.entity.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findCategoryByName(String name);

    Optional<Category> findCategoryById(int id);

    List<Category> findAllByIsDeleted(boolean isDeleted);
}
