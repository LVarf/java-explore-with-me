package ru.practicum.ewmcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmcore.model.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
