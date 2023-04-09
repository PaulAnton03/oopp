package server.database;

import commons.CardTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardTagRepository extends JpaRepository<CardTag, Long> {
    List<CardTag> findAllByCardId(Long cardId);

    List<CardTag> findAllByTagId(Long tagId);

    void deleteAllByCardId(Long cardId);

    void deleteAllByTagId(Long tagId);

    long countByTagId(Long tagId);
}
