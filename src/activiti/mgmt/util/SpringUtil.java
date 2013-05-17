package activiti.mgmt.util;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import common.AppConfig;

public class SpringUtil {
	
	private static ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(AppConfig.context);
	
	
	public static <T> T getBean(Class<T> clz) {
		return ctx.getBean(clz);
	}

}
