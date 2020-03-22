package com.hua.mapper;

import com.hua.dto.QuesstionDTO;
import com.hua.model.Quesstion;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuesstionMapper {
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) value (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
     void create(Quesstion quesstion);


    @Select("SELECT * FROM question  LIMIT  #{offset},#{size}")
    List<Quesstion> list(@Param(value="offset")Integer offset, @Param(value="size")Integer size);

    @Select("SELECT count(1) from question ")
    Integer count();

    @Select("SELECT * FROM question where creator= #{userId} limit #{offset},#{size}")
    List<Quesstion> listByUserId( @Param(value="userId") Integer userId, @Param(value="offset")Integer offset, @Param(value="size")Integer size);

    @Select("SELECT count(1) from question where creator= #{userId}")
    Integer countByUserId(@Param(value="userId")Integer userId);

    @Select("SELECT * FROM question where id= #{id}")
    Quesstion getById(@Param(value="id") Integer id);

    @Update("update question set title=#{title},description=#{description},gmt_modified=#{gmtModified},tag=#{tag} where id=#{id}")
    void update(Quesstion quesstion);
}
