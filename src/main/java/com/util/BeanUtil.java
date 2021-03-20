package com.util;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * Servlet参数封装的工具类，目的是将页面传过来的各个参数构造成一个实体类的对象
 * 通过传入需要封装的类的类对象和请求，运用反射，
 * 先创建一个空的实体对象，再遍历页面的属性值构成的枚举对象
 * @author Jinhua
 */
public class BeanUtil {
	public static <T> T getBean(Class<T> tClass, HttpServletRequest request) {
		T entity = null;
		try {
			// 创建对象的实例
			entity = tClass.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 获取name属性值组成的一个枚举对象
		Enumeration<String> parameterNames = request.getParameterNames();

		// 对属性进行遍历
		while(parameterNames.hasMoreElements()) {
			String name = parameterNames.nextElement();
			String value = request.getParameter(name);
			// 使用属性描述器,处理value值
			try {
				PropertyDescriptor pd = new PropertyDescriptor(name, tClass);

				// 获取属性对应的setter方法
				Method method = pd.getWriteMethod();

				// 获取属性的类型
				Class<?> type = pd.getPropertyType();

				// 通过反射设置属性的value值
				if (type.equals(Date.class)) {
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse(value);
					method.invoke(entity, date);
				} else if (type.equals(int.class)){
					method.invoke(entity, Integer.parseInt(value));
				} else {
					method.invoke(entity, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return entity;
	}
}