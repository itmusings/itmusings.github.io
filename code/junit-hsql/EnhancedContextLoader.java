package com.tesco.gmodo.utility.junit;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.support.GenericXmlContextLoader;

/**
 * 
 * @author Raja Shankar Kolluru
 * This class enhances the Spring XML context loader to accomplish the following:
 * <li>Default the xml configuration file to <class-name>.xml (with the fully
 * qualified class name)
 * <li>Include a property configurer and use <class name>.properties if that 
 * file is present in the class path.
 * <li>Include additional configurations for HSQL DB if the DaoTest
 * annotation is included in the test class definition. This would set the 
 * transactionManager and sessionFactory attributes so that they are available
 * for any additional DAOs that need to be instantiated.
 * <li>After starting HSQLDB this class also triggers the execution of a sql script
 * which needs to be configured in the script.file property in the properties file
 * <li>It also loads the hbm files as specified in the mapping.resources property 
 * in the properties file.
 *
 */
public class EnhancedContextLoader extends GenericXmlContextLoader{

	protected Class<?> clazz;
	
	@Override
	protected String[] generateDefaultLocations(Class<?> clazz) {
		this.clazz = clazz;
		List<String> list = new ArrayList<String>(); 
		String name = clazz.getName().replaceAll("\\.", "/") + ".xml";
		list.add(name);
		if (clazz.isAnnotationPresent(DaoTest.class)){
			System.out.println("dao test detected!!");
			list.add("init-hsql.xml");
		}
		String x[] = new String[list.size()];
		for (int i = 0; i < list.size();i++){
			x[i] = list.get(i);
		}
		return x;
	}

	
	@Override
	protected void customizeContext(GenericApplicationContext context) {
		super.customizeContext(context);
		addPropertyPostProcessor(context);
	}


	protected void addPropertyPostProcessor(GenericApplicationContext bf){
		String name = getPropertyFileLocation();
		URL url = getClass().getClassLoader().getResource(name);
	
		if (url == null)
			return;
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		Resource location = new ClassPathResource(name);
		ppc.setLocation(location);
		bf.addBeanFactoryPostProcessor(ppc);
	}

	protected String getPropertyFileLocation() {
		return clazz.getName().replaceAll("\\.", "/") + ".properties";
		
	}
	
	
	
	

}
