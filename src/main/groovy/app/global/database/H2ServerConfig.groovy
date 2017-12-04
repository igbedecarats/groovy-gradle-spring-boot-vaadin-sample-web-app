package app.global.database

import org.h2.tools.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import java.sql.SQLException

@Configuration
class H2ServerConfig {

    @Value('${h2.tcp.port:9092}')
    private String port

    @Bean(initMethod = "start", destroyMethod = "stop")
    Server server() throws SQLException {
        return Server.createTcpServer("-tcpPort", port, "-tcpAllowOthers")
    }
}
