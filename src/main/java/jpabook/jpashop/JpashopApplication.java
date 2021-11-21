package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}

	@Bean
	Hibernate5Module hibernate5Module() {
		Hibernate5Module hibernate5Module = new Hibernate5Module();

		// JSON 생성 시점에 관련 모든 LAZY_LOADING 실시
		// 웬만하면 안 쓰는 게 좋음
		// hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);



		return hibernate5Module;
	}

}
