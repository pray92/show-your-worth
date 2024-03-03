package kr.texturized.muus.dao;

import kr.texturized.muus.domain.entity.Busking;
import kr.texturized.muus.domain.entity.Image;
import kr.texturized.muus.domain.entity.Keyword;
import kr.texturized.muus.domain.entity.PostCategoryEnum;
import kr.texturized.muus.domain.entity.fk.ImageFk;
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
        saveKeywords(vo.keywords(), vo.busking().getId(), PostCategoryEnum.BUSKING, vo.busking().getTitle());
        saveImages(vo.imagePaths(), vo.busking().getId(), PostCategoryEnum.BUSKING, vo.busking().getTitle());

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
     *
     * 키워드 엔티티를 저장해요. 게시물 정보는 키워드 엔티티에 있어요.
     *
     * @param keywords 키워드 엔티티 목록
     * @param postId 키워드가 속한 게시물 ID
     * @param category 키워드가 속한 게시물 타입
     * @param title 게시물 제목
     */
    private void saveKeywords(
        final List<String> keywords,
        final Long postId,
        final PostCategoryEnum category,
        final String title
    ) {
        keywords.forEach(keyword -> {
            keywordRepository.save(Keyword.builder()
                    .postId(postId)
                    .postType(category)
                    .keyword(keyword)
                .build());
            log.info("Keyword: {} for {} {} is added", keyword, category, title);
        });
    }

    /**
     *
     * 이미지 엔티티를 저장해요. 게시물 정보는 이미지 엔티티에 있어요.
     *
     * @param imagePaths 이미지 엔티티 목록
     * @param postId 이미지가 속한 게시물 ID
     * @param category 이미지가 속한 게시물 타입
     * @param title 게시물 제목
     */
    private void saveImages(
            final List<String> imagePaths,
            final Long postId,
            final PostCategoryEnum category,
            final String title
    ) {
        for (int order = 0; order < imagePaths.size(); ++order) {
            final String imagePath = imagePaths.get(order);
            imageRepository.save(Image.builder()
                    .id(ImageFk.builder()
                            .postId(postId)
                            .postType(category)
                            .uploadOrder(order)
                        .build())
                    .path(imagePath)
                .build());

            log.info("Image No. {} for {} {} is added named by [{}]", order, category, title, imagePath);
        }
    }
}

