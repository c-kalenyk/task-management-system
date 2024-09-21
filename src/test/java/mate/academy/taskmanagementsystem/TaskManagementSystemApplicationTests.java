package mate.academy.taskmanagementsystem;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TaskManagementSystemApplicationTests {
    @BeforeAll
    static void setUp() {
        if (System.getenv("CI") == null) {
            Dotenv dotenv = Dotenv.configure().load();
            System.setProperty("SPRING_MAIL_USERNAME", dotenv.get("SPRING_MAIL_USERNAME"));
            System.setProperty("SPRING_MAIL_PASSWORD", dotenv.get("SPRING_MAIL_PASSWORD"));
            System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
            System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));
        } else {
            System.setProperty("SPRING_MAIL_USERNAME", System.getenv("SPRING_MAIL_USERNAME"));
            System.setProperty("SPRING_MAIL_PASSWORD", System.getenv("SPRING_MAIL_PASSWORD"));
            System.setProperty("JWT_SECRET", System.getenv("JWT_SECRET"));
            System.setProperty("JWT_EXPIRATION", System.getenv("JWT_EXPIRATION"));
        }
    }

    @Test
    void contextLoads() {
    }
}
