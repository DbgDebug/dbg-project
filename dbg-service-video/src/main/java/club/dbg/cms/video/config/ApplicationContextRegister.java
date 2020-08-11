package club.dbg.cms.video.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class ApplicationContextRegister implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 设置spring上下文
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextRegister.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    /**
     * ApplicationContext act = ApplicationContextRegister.getApplicationContext();
     * Service service = act.getBean(service.class);
     */
}