

import com.ll.Sql;
import com.ll.simpleDb.SimpleDB;
import com.ll.simpleDb.SimpleDBProxy;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class simpleDbTest {

    private  static SimpleDBProxy simpleDBProxy;

    @BeforeAll
    public static void beforeAll() {
        simpleDBProxy=new SimpleDBProxy();
        simpleDBProxy.connect();
        createArticleTable();

    }

    @BeforeEach
    public void beforeEach() {
        truncateArticleTable();
        makeArticleTestData();
    }

    private static void createArticleTable() {
        simpleDBProxy.run("DROP TABLE IF EXISTS article");

        simpleDBProxy.run("""
                CREATE TABLE article (
                    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
                    PRIMARY KEY(id),
                    createdDate DATETIME NOT NULL,
                    modifiedDate DATETIME NOT NULL,
                    title VARCHAR(100) NOT NULL,
                    `body` TEXT NOT NULL,
                    isBlind BIT(1) NOT NULL DEFAULT 0
                )
                """);
    }

    private void makeArticleTestData() {
        IntStream.rangeClosed(1, 6).forEach(no -> {
            boolean isBlind = no > 3;
            String title = "제목%d".formatted(no);
            String body = "내용%d".formatted(no);


            simpleDBProxy.run("""
                    INSERT INTO article
                    SET createdDate = NOW(),
                    modifiedDate = NOW(),
                    title = ?,
                    `body` = ?,
                    isBlind = ?
                    """, title, body, isBlind);
        });
    }

    private void truncateArticleTable() {
        simpleDBProxy.run("TRUNCATE article");
    }

    @Test
    @DisplayName("insert")
    public void t001() {
        Sql sql = simpleDBProxy.genSql();
        /*
        == rawSql ==
        INSERT INTO article
        SET createdDate = NOW() ,
        modifiedDate = NOW() ,
        title = '제목 new' ,
        body = '내용 new'
        */
        sql.append("INSERT INTO article")
                .append("SET createdDate = NOW()")
                .append(", modifiedDate = NOW()")
                .append(", title = ?", "제목 new")
                .append(", body = ?", "내용 new");
        //setString 처리 문제 해결해야함
        long newId = sql.executeUpdate(); // AUTO_INCREMENT 에 의해서 생성된 주키 리턴

        assertThat(newId).isGreaterThan(0);
    }
}
