package fmyxyz.mybatis_generatorConfig;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */

public class App {
	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/spring-context.xml");
		Generator g = ctx.getBean(Generator.class);
		g.generate();
		ctx.start();
	}
}
