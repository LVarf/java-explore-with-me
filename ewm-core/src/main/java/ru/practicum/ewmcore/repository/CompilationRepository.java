package ru.practicum.ewmcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmcore.model.compilation.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

}
