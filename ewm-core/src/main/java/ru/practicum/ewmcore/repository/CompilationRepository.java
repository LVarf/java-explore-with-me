package ru.practicum.ewmcore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmcore.model.compilation.Compilation;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Page<Compilation> findCompilationByPinnedIs(Boolean pinned, Pageable pageable);
}
