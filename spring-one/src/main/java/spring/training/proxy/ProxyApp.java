package spring.training.proxy;

import java.io.File;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)
@EnableCaching 
@SpringBootApplication
@Slf4j
public class ProxyApp implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(ProxyApp.class, args);
	}

	// [1] Implement decorator with Spring
	// [2] InterfaceProxy.proxy (no Spring)
	// [3] ClassProxy.proxy (no Spring)
	// [4] Spring Cache support
	// [5] Spring aspect, @Facade, @Logged1 
	// [6] Tips: self proxy, debugging, final
	// [7] OPT: Manual proxying using BeanPostProcessor23

	// Holy Domain Logic.
	// Very precious things that I want to keep agnostic to technical details
	@Autowired
	private ExpensiveOps ops;

	public void run(String... args) {
		log.debug("\n");
 		log.debug("---- CPU Intensive ~ memoization?");
		log.debug("10000169 is prime ? ");
		log.debug("Got: " + ops.isPrime(10000169) + "\n");
		log.debug("10000169 is prime ? ");
		log.debug("Got: " + ops.isPrime(10000169) + "\n");
		
		log.debug("---- I/O Intensive ~ \"There are only two things hard in programming...\"");
		log.debug("Folder . MD5: ");
		log.debug("Got: " + ops.hashAllFiles(new File(".")) + "\n");
		log.debug("Got: " + ops.hashAllFiles(new File(".")) + "\n");
		log.debug("Folder . MD5: ");
		log.debug("Got: " + ops.hashAllFiles(new File(".")) + "\n");
	}
}

