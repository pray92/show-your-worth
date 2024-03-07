package kr.texturized.muus.infrastructure.repository;

import kr.texturized.muus.domain.entity.Keyword;
import kr.texturized.muus.domain.entity.PostTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA repository for Keyword.
 */
public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    void deleteAllInBatchByPostIdAndPostType(Long postId, PostTypeEnum postType);
}
