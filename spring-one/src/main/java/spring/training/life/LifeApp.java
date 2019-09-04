package spring.training.life;

import java.util.Locale;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class LifeApp implements CommandLineRunner{
	@Bean
	public static CustomScopeConfigurer defineThreadScope() {
		CustomScopeConfigurer configurer = new CustomScopeConfigurer();
		// WARNING: Leaks memory. Prefer 'request' scope or read here: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/support/SimpleThreadScope.html
		configurer.addScope("thread", new SimpleThreadScope());
		return configurer;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(LifeApp.class);
	}
	
	@Autowired 
	private OrderExporter exporter;
	
	// TODO [1] make singleton; test multi-thread: state is [e|v|i|l]
	// TODO [2] instantiate manually, set dependencies, pass around; no AOP
	// TODO [3] prototype scope + ObjectFactory or @Lookup. Did you said "Factory"? ...
	// TODO [4] thread/request scope. HOW it works?! Leaks: @see SimpleThreadScope javadoc

	public void run(String... args) {
		new Thread(() -> exporter.export(Locale.ENGLISH)).start();
		new Thread(() -> exporter.export(Locale.FRENCH)).start();
	}
}
@Slf4j
@Service
class OrderExporter  {
	@Autowired
	private InvoiceExporter invoiceExporter;
	@Autowired
	private LabelService labelService;

	public void export(Locale locale) {
		log.debug("Oare pe ce chem eu metodele aste a?! : " + labelService.getClass());
		log.debug("Running export in " + locale);
		labelService.load(locale);
		log.debug("Origin Country: " + labelService.getCountryName("rO"));
		invoiceExporter.exportInvoice();
	}
}
@Slf4j
@Service 
class InvoiceExporter {
	@Autowired
	private LabelService labelService;
	public void exportInvoice() {
		log.debug("Invoice Country: " + labelService.getCountryName("ES"));
	}
}

@Slf4j
@Service
@Scope(scopeName = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
class LabelService {
	private CountryRepo countryRepo;

	public LabelService(CountryRepo countryRepo) {
		System.out.println("+1 Label Service: " + this.hashCode());
		this.countryRepo = countryRepo;
	}

	private Map<String, String> countryNames;

	public void load(Locale locale) {
		log.debug("LabelService.load() on: " + this.hashCode());
		countryNames = countryRepo.loadCountryNamesAsMap(locale);
	}
	
	public String getCountryName(String iso2Code) {
		log.debug("LabelService.getCountryName() on: " + this.hashCode());
		return countryNames.get(iso2Code.toUpperCase());
	}
}

//
//class Controller1 {
//	Service1 s;
//	void handleHtt() {
//		s.m();
//	}
//}
//class Service1 {
//	Repo repo;
//	void m() {
//		repo.update(1, "new");
//	}
//}
//
//class Repo {
////	@Autowired
////	private LabelService
//	public void update(long id, String newValue) {
//		// UPDATE SET lastModifiedBy =
//		String userId = SecurityContextHolder.getContext().getPrinc;
//		System.out.println(userId);
//	}
//}