package org.jharkendar.data.summary;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SummaryRepository extends CrudRepository<JpaSummary, String> {

    @Query(value = "SELECT s FROM JpaSummary s JOIN s.topic t WHERE t.id = :topicId")
    List<JpaSummary> findByTopicId(@Param("topicId") String topicId);

}
