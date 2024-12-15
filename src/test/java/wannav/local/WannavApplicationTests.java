package wannav.local;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class WannavApplicationTests {
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
