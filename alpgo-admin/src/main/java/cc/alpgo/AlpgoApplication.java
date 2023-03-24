package cc.alpgo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动程序
 *
 * @author alpgo
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableAsync
public class AlpgoApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(AlpgoApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  UI Admin启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
