package community_study.mapper;

import community_study.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    @Insert("insert into user(name,account_id,token,gmt_create,gmt_update) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtUpdate})")
    void inserUser(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);
}
