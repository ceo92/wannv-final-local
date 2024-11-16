package please_do_it.yumi;

import static org.assertj.core.api.Assertions.assertThat;
import static please_do_it.yumi.domain.QFood.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import please_do_it.yumi.domain.Food;
import please_do_it.yumi.domain.QFood;
import please_do_it.yumi.domain.QRestaurant;
import please_do_it.yumi.domain.Restaurant;

@SpringBootTest
@Transactional
class YumiApplicationTests {
	@Autowired
	EntityManager em;
	@Test
	void aa(){


	}


	@Test
	void search(){

	}

	@Test
	void contextLoads() {

	}

}
