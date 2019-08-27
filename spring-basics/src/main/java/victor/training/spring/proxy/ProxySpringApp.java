package victor.training.spring.proxy;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

@Slf4j
@EnableCaching
@SpringBootApplication
public class ProxySpringApp implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(ProxySpringApp.class, args);
	}

	@Autowired
	private ExpensiveOps ops;

	// TODO [1] implement decorator
	// TODO [2] apply decorator via Spring
	// TODO [3] generic java.lang.reflect.Proxy
	// TODO [4] Spring aspect
	// TODO [5] Spring cache support
	// TODO [6] Back to singleton (are you still alive?)
	public void run(String... args) throws Exception {
//		ExpensiveOps ops= new ExpensiveOps();
		System.out.println("Oare cu cine vorbesc !? " + ops.getClass());
		log.debug("\n");
		log.debug("---- CPU Intensive ~ memoization?");
		log.debug("10000169 is prime ? ");
		log.debug("Got: " + ops.isPrime(10000169) + "\n");
		log.debug("10000169 is prime ? ");
		log.debug("Got: " + ops.isPrime(10000169) + "\n");

		log.debug("---- I/O Intensive ~ \"There are only two things hard in programming...\"");
		log.debug("Folder MD5: ");
		log.debug("Got: " + ops.hashAllFiles(new File(".")) + "\n");
		log.debug("Got: " + ops.hashAllFiles(new File(".")) + "\n");

		log.debug("Simulez ca aici imi dau seama ca cineva a modificat in folder un fisier");
		ops.evictAllFileHash();
		log.debug("Folder MD5: ");
		log.debug("Got: " + ops.hashAllFiles(new File(".")) + "\n");
	}
}

@Retention(RetentionPolicy.RUNTIME)
@interface  LoggedMethod {}
@Retention(RetentionPolicy.RUNTIME)
@interface  LoggedClass {}
@Component
@Aspect
class LoggingAspect {
	private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

//	@Around("execution(* victor..*.*(..))")
//	@Around("execution(* *(..)) && @annotation(victor.training.spring.proxy.LoggedMethod)")
	@Around("execution(* *(..)) && @within(victor.training.spring.proxy.Facade)")
	public Object m(ProceedingJoinPoint point) throws Throwable {
		log.debug("AOP: Calling method {} with params {}",point.getSignature().getName(),
				Arrays.toString(point.getArgs()));
		return point.proceed();
	}
}

@Component
class AsistentaMaternala implements BeanPostProcessor {
	@Override
	public Object postProcessBeforeInitialization(Object copil, String beanName) throws BeansException {
		System.out.println("Infasez pe " + copil);
		return copil;
	}

	@Override
	public Object postProcessAfterInitialization(Object copil, String beanName) throws BeansException {
		System.out.println("Dau biberon la " + copil);
		return copil;
	}
}