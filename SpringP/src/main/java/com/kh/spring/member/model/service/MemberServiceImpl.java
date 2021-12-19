package com.kh.spring.member.model.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.common.exception.CommException;
import com.kh.spring.member.model.dao.MemberDao;
import com.kh.spring.member.model.vo.Member;

//@EnableAspectJAutoProxy
//@Transactional(rollbackFor= {Exception.class}) //여러 개 적을 경우{Exception.class, RuntimeException.class}, 실행시킬 각 메소드 위에 적을 수도 있음
@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Autowired
	private MemberDao memberDao;
	
	@Override
	public Member loginMember(Member m) throws Exception{
		
		Member loginUser = memberDao.loginMember(sqlSession, m);
		
		if(loginUser == null) {
			throw new Exception("loginUser 확인");
		}
		
		return loginUser;
	}

	@Override
	public void insertMember(Member m) {

		int result = memberDao.insertMember(sqlSession, m);
		
		if(result < 0) {
			throw new CommException("회원가입에 실패했습니다.");
		}
		
	}

	@Override
	public Member loginMember(BCryptPasswordEncoder bCryptPasswordEncoder, Member m) {

		Member loginUser = memberDao.loginMember(sqlSession, m);
		
		if(loginUser == null) {
			throw new CommException("loginUser 확인");
		}
		
		//matches(앞 파라미터 : 평문, 뒤 파라미터: 암호화문) -> true, false 반환
		if(!bCryptPasswordEncoder.matches(m.getUserPwd(), loginUser.getUserPwd())) {
			throw new CommException("암호 불일치");
		}
		
		return loginUser;
	}

	@Override
	public Member updateMember(Member m) throws Exception {

		int result = memberDao.updateMemer(sqlSession, m);
		//memberDao.insertMember(sqlSession, m);
		if(result < 0) {
			Member loginUser = memberDao.loginMember(sqlSession, m);
			return loginUser;
		}else {
			throw new Exception("회원 수정 실패");
		}
		
	}

	@Override
	public void deleteMember(String userId) {
		
		int result = memberDao.deleteMember(sqlSession, userId);
		
		if(result < 0) {
			throw new CommException("회원탈퇴에 실패했습니다.");
		}
		
	}

	@Override
	public Member updatePwd(BCryptPasswordEncoder bCryptPasswordEncoder, Member m, String userPwd, String newPwd) {

		Member updateMem = null;
		
		//Member loginUser = memberDao.loginMember(sqlSession, m);
		
		System.out.println(m);
		if(!bCryptPasswordEncoder.matches(userPwd, m.getUserPwd())) {
			throw new CommException("암호 불일치");
		}else {
			
			m.setUserPwd(newPwd);
			int result = memberDao.updatePwd(sqlSession, m);
			
			if(result > 0) {
				updateMem = memberDao.loginMember(sqlSession, m);
			}else {
				throw new CommException("비밀번호 수정 실패");
			}
			
		}
		
		
		return updateMem;
	}

	

}
