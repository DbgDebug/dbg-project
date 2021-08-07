package club.dbg.cms.upload.config;

import org.dbg.common.aop.aspect.TransactionalAndLockAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class TransactionalAndLockConfig {

    private final DataSourceTransactionManager dataSourceTransactionManager;

    public TransactionalAndLockConfig(DataSourceTransactionManager dataSourceTransactionManager) {
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }

    @Bean
    public TransactionalAndLockAspect getTransactionalAndLock() {
        return new TransactionalAndLockAspect(dataSourceTransactionManager);
    }
}
