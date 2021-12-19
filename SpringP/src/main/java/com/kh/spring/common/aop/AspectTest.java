package com.kh.spring.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

//@Aspect
@Component
public class AspectTest {

	@Pointcut("execution(* com.kh.spring..*ServiceImpl.*(..))")
	public void beforePointCut() {}
	
	@Before("beforePointCut()")
	public void before(JoinPoint join) throws Exception{
		Signature sig = join.getSignature();
		
		Object[] params = join.getArgs();
		for(Object obj : params) {
			System.out.println("obj " + obj);
		}
		
		System.out.println("메소드 호출전 확인" + sig.getDeclaringTypeName() + " : " + sig.getName());
	}
	
	
	@Pointcut("execution(* com.kh.spring..*ServiceImpl.*(..))")
	public void afterPointCut() {}
	
	//예외 상관없이 무조건(finally와 같은 역할)
	@After("afterPointCut()")
	public void after(JoinPoint join) throws Exception{
		Signature sig = join.getSignature();
		
		
		System.out.println("메소드 호출 후 확인" + sig.getDeclaringTypeName() + " : " + sig.getName());
	}
	
	
	@Pointcut("execution(* com.kh.spring..*ServiceImpl.*(..))")
	public void afterReturning() {}
	
	//정상적으로 수행했을 때 메소드 수행 결과 리턴
	@AfterReturning(pointcut="afterReturning()", returning="returnObj")
	public void returningPoint(JoinPoint join, Object returnObj) throws Exception{
		Signature sig = join.getSignature();
		
		
		System.out.println("afterReturning 확인" + sig.getDeclaringTypeName() + " : " + sig.getName() + " : " + returnObj);
	}
	
	
	@Pointcut("execution(* com.kh.spring..*ServiceImpl.*(..))")
	public void afterThrowing() {}
	
	//예외 발생했을 때
	@AfterThrowing(pointcut="afterThrowing()", throwing="exceptionObj")
	public void throwingPoint(JoinPoint join, Exception exceptionObj) throws Exception{
		Signature sig = join.getSignature();
		
		System.out.println("afterThrowing 확인" + sig.getDeclaringTypeName() + " : " + sig.getName());
		
		if(exceptionObj instanceof IllegalArgumentException) {
			System.out.println("부적합한 값이 입력되었습니다.");
		}else {
			System.out.println("예외발생메세지 : " + exceptionObj.getMessage());
			System.out.println("예외발생종류 : " + exceptionObj.getClass().getName());
		}
	}
	
	@Pointcut("execution(* com.kh.spring..*ServiceImpl.*(..))")
	public void aroundPointCut() {}
	
	//타켓이되는 메소드의 반환값 반환
	@Around("aroundPointCut()")
	public Object aroundLog(ProceedingJoinPoint join) throws Throwable{
		
		String methodName = join.getSignature().getName();
		StopWatch stopWatch = new StopWatch();
		
		stopWatch.start();
		
		Object obj = join.proceed(); //전후를 나누는 기준
		
		stopWatch.stop();
		
		System.out.println(methodName + " 소요시간(ms) " + stopWatch.getTotalTimeMillis() + "초");
		return obj;
		
	}
}
