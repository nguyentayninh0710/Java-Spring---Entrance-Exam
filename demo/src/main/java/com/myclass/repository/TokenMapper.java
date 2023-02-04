package com.myclass.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.myclass.entity.Token;

@Mapper
@Component
public interface TokenMapper {

	@Select("SELECT id,userId,refreshToken, expiresIn FROM Tokens where refreshToken=#{refreshToken}")
	@Results(value = { @Result(property = "id", column = "id"), 
			@Result(property = "userId", column = "userId"),
			@Result(property = "refreshToken", column = "refreshToken"),
			@Result(property = "expiresIn", column = "expiresIn") })
	public Token findByToken(@Param("refreshToken") String refreshToken);

	@Update("INSERT INTO Tokens(userId, refreshToken, expiresIn, updatedAt, createdAt) "
			+ "VALUES(#{userId}, #{refreshToken}, #{expiresIn}, NOW(), NOW())")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void insert(Token token);

	@Delete("Delete from Tokens where userId = #{userId}")
	public void delete(@Param("userId") int userId);

	@Insert("Update Tokens set refreshToken = #{refreshToken} where refreshToken = #{refreshToken}")
	public void update(Token token);

}
