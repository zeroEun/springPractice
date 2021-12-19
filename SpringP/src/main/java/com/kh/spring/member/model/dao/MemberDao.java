package com.kh.spring.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.spring.member.model.vo.Member;

@Repository //변수 명이 클래스 명과 다를 경우 ("mDao")
public class MemberDao {

	public Member loginMember(SqlSessionTemplate sqlSession, Member m) {
		
		Member m1 = sqlSession.selectOne("memberMapper.loginMember", m);
		
		return m1;
	}

	public int insertMember(SqlSessionTemplate sqlSession, Member m) {
		return sqlSession.insert("memberMapper.insertMember", m);
	}

	public int updateMemer(SqlSessionTemplate sqlSession, Member m) {

		return sqlSession.update("memberMapper.updateMemer", m);
	}

	public int deleteMember(SqlSessionTemplate sqlSession, String userId) {
		
		return sqlSession.update("memberMapper.deleteMember", userId);
	}

	public int updatePwd(SqlSessionTemplate sqlSession, Member m) {

		return sqlSession.update("memberMapper.updatePwd", m);
	}

}
