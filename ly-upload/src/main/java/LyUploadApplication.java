import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by pass on 2019/11/28.
 */
@EnableDiscoveryClient
@SpringBootApplication
public class LyUploadApplication {
    public static void main(String[] args){
        SpringApplication.run(LyUploadApplication.class);
    }
}
