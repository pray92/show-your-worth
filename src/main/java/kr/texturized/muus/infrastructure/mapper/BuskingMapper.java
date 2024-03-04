package kr.texturized.muus.infrastructure.mapper;

import java.util.List;
import java.util.Optional;

import kr.texturized.muus.domain.vo.BuskingProfileResultVo;
import kr.texturized.muus.domain.vo.BuskingSearchResultVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * DAO for Busking Selection.
 */
@Mapper
public interface BuskingMapper {

    /**
     * Get Active buskings for map.
     * Since this method is basic showing in map, it should be ready to start or already started
     *
     * @param latitude Offset latitude
     * @param longitude Offset longitude
     * @param width Range of width to show, it should be converted to latitude
     * @param height Range of height to show, it should be converted to longitude
     * @return List of Busking, just id and coordinate
     */
    List<BuskingSearchResultVo> search(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("width") double width,
            @Param("height") double height
    );

    /**
     * 버스킹 프로필을 조회해요.
     *
     * @param buskingId 버스킹 ID
     * @return 호스팅 유저의 정보와 버스킹 프로필 정보 Vo
     */
    Optional<BuskingProfileResultVo> profile(Long buskingId);
}
