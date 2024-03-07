package kr.texturized.muus.infrastructure.mapper.handler.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import kr.texturized.muus.domain.entity.PostTypeEnum;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

/**
 * PostCategory type handler for myBatis.
 * ref: <a href="https://goodteacher.tistory.com/653">06. Enum 타입의 활용 by 은서파</a>
 */
@MappedTypes(PostTypeEnum.class)
public class PostCategoryHandler implements TypeHandler<PostTypeEnum> {

    @Override
    public void setParameter(PreparedStatement ps, int i, PostTypeEnum parameter, JdbcType jdbcType)
        throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public PostTypeEnum getResult(ResultSet rs, String columnName) throws SQLException {
        return getType(rs.getString(columnName));
    }

    @Override
    public PostTypeEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
        return getType(rs.getString(columnIndex));
    }

    @Override
    public PostTypeEnum getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getType(cs.getString(columnIndex));
    }

    private PostTypeEnum getType(String key) {
        return PostTypeEnum.fromKey(key);
    }
}
