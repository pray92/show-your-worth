package kr.texturized.muus.dao;

import kr.texturized.muus.domain.entity.Busking;
import kr.texturized.muus.domain.entity.Image;
import kr.texturized.muus.domain.entity.Keyword;
import kr.texturized.muus.domain.entity.PostCategoryEnum;
import kr.texturized.muus.domain.vo.BuskingCreateModelVo;
import kr.texturized.muus.infrastructure.repository.BuskingRepository;
import kr.texturized.muus.infrastructure.repository.ImageRepository;
import kr.texturized.muus.infrastructure.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DAO for Busking CUD.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BuskingDao {

    private final BuskingRepository buskingRepository;
    private final KeywordRepository keywordRepository;
    private final ImageRepository imageRepository;

    /**
     * 버스킹을 생성해요.
     *
     * @param vo 버스킹 생성에 필요한 데이터 집합을 추상화한 ModelVo
     * @return 버스킹 ID
     */
    public Long create(final BuskingCreateModelVo vo) {

        saveBusking(vo.busking());
        saveKeywords(vo.keywords(), PostCategoryEnum.BUSKING, vo.busking().getTitle());
        saveImages(vo.images(), PostCategoryEnum.BUSKING, vo.busking().getTitle());

        return vo.busking().getId();
    }


    /**
     * 버스킹 엔티티를 저장해요. 키워드나 이미지는 따로 저장해야 해요.
     *
     * @param busking 버스킹 엔티티
     */
    private void saveBusking(final Busking busking) {
        buskingRepository.save(busking);

        log.info("Busking is created({}): {}", busking.getId(), busking.getTitle());
    }

    /**
     * 키워드 엔티티를 저장합니다. 게시물 정보는 키워드 엔티티에 있어요.
     *
     * @param keywords 키워드 엔티티 목록
     * @param category 키워드에 속한 게시물 타입
     * @param title 게시물 제목
     */
    private void saveKeywords(
        final List<Keyword> keywords,
        final PostCategoryEnum category,
        final String title
    ) {
        keywords.forEach(keyword -> {
            keywordRepository.save(keyword);

            log.info("Keyword: {} for {} {} is added", keyword.getKeyword(), category, title);
        });
    }

    /**
     * 이미지 엔티티를 저장합니다. 게시물 정보는 이미지 엔티티에 있어요.
     *
     * @param images 이미지 엔티티 목록
     * @param category 이미지에 속한 게시물 타입
     * @param title 게시물 제목
     */
    private void saveImages(
            final List<Image> images,
            final PostCategoryEnum category,
            final String title
    ) {
        images.forEach(image -> {
            imageRepository.save(image);

            log.info("Image No. {} for {} {} is added in {}",
                image.getId().getUploadOrder(),
                category,
                title,
                image.getPath()
            );
        });
    }
}

