package ru.practicum.ewmstat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewmstat.model.EndpointHit;
import ru.practicum.ewmstat.model.ViewStats;

import java.sql.Timestamp;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new ru.practicum.ewmstat.model.ViewStats(h.app, h.uri, count(h.ip)) "
            + " from EndpointHit h "
            + " where h.timestamp between ?1 and ?2 "
            + " and h.uri in (?3) "
            + " group by h.app, h.uri")
    List<ViewStats> findViewsUniqueFalse(Timestamp start, Timestamp end, String[] uris);

    @Query("select new ru.practicum.ewmstat.model.ViewStats(h.app, h.uri, count(distinct h.ip)) "
            + " from EndpointHit h "
            + " where h.timestamp between ?1 and ?2 "
            + " and h.uri in (?3) "
            + " group by h.app, h.uri")
    List<ViewStats> findViewsUniqueTrue(Timestamp start, Timestamp end, String[] uris);
}
