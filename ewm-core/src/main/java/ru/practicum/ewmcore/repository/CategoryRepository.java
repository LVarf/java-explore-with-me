package ru.practicum.ewmcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmcore.model.category.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
