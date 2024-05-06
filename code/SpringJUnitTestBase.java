

import junit.framework.TestCase;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A useful base class for all test cases that require spring to instantiate dependencies.
 * They need to define fields and setters. They should also define IDs with the same names in the 
 * spring file. (which is typically called by the same name as the class and is in the same package)
 * For over-riding the default spring file, over-ride the getSpringFiles() method.
 * <p>
 * The variables are auto-wired from Spring automatically
 * .
 * @author raja.kolluru
 *
 */
public class SpringJUnitTestBase extends TestCase {
	protected BeanFactory beanFactory;
	public SpringTestBase(){
		ClassPathXmlApplicationContext bf = new ClassPathXmlApplicationContext(
			   	getSpringFiles());
	   AutowireCapableBeanFactory acbf = bf.getAutowireCapableBeanFactory();
	   // autowire myself.
	   acbf.autowireBeanProperties(this, 
			   AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
	   beanFactory = bf;
	  }
	/**
	 * Override this to use different Spring files. 
	 * Default is the same as the class in the same package.
	 * @return
	 */
	protected String[] getSpringFiles(){
//		 look for a spring file with the same name as the test class.
		String name = getClass().getName().replaceAll("\\.", "/") + ".xml";
		return new String[] { name};
	}
	
}
