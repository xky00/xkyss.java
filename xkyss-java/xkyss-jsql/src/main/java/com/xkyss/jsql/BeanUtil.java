package com.xkyss.jsql;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

/**
 * <b>业务bean对象实用工具类</b><br>
 * 利用json编解码实现通用对象的克隆。
 * @author rechard
 *
 */
public class BeanUtil {
	/**
	 * 在不同类型的对象之间实现克隆
	 * @param targetClass 目标对象的类型
	 * @param sourceObject 克隆源对象
	 * @return
	 */
	public static <TARGET_CLASS, SOURCE_CLASS> TARGET_CLASS clone(Class<TARGET_CLASS> targetClass, SOURCE_CLASS sourceObject) {
		return clone(targetClass, sourceObject, false);
	}
	/**
	 * 在不同类型的对象之间实现克隆
	 * @param targetClass 目标对象的类型
	 * @param sourceObject 克隆源对象
	 * @param forcedClone 即使出现属性类型冲突也强制进行其他属性的克隆
	 * @return
	 */
	public static <TARGET_CLASS, SOURCE_CLASS> TARGET_CLASS clone(Class<TARGET_CLASS> targetClass, SOURCE_CLASS sourceObject, boolean forcedClone) {
		TARGET_CLASS targetObject = null;
		String propName = null;
		try {
			Map<String, Map<String, Method>> srcPropMethods = getProperticeMethods(sourceObject.getClass());
			if (srcPropMethods != null && srcPropMethods.size() > 0) {
				Map<String, Map<String, Method>> tagPropMethods = getProperticeMethods(targetClass);
				if (tagPropMethods != null && tagPropMethods.size() > 0) {
					targetObject = targetClass.newInstance();
					Iterator<Entry<String, Map<String, Method>>> sit = srcPropMethods.entrySet().iterator();
					while (sit.hasNext()) {
						Entry<String, Map<String, Method>> sen = sit.next();
						Map<String, Method> rwMethod = tagPropMethods.get(sen.getKey());
						if (rwMethod != null) {
							// 将源对象中与目标对象同名且同类型的属性值赋值给目标对象
							Method readMethod = sen.getValue().get("get");
							Object val = readMethod.invoke(sourceObject);
							Method writeMethod = rwMethod.get("set");
							propName = sen.getKey();
							try {
								if (val != null) {
									writeMethod.invoke(targetObject, clone(val));
								} else {
									writeMethod.invoke(targetObject, val);
								}
							} catch (Exception e) {
								if (!forcedClone) {
									// 非强制克隆则异常终止
									throw e;
								}
							}
							propName = null;
						}
					}
				}
			}
		} catch (Exception e) {
			if (propName != null) {
				throw new RuntimeException(String.format("%s属性值克隆失败", propName), e);
			} else {
				throw new RuntimeException("系统未知错误", e);
			}
		}
		return targetObject;
	}

	/**
	 * 将一段序列化数据还原成对象
	 * @param bytes 序列化数组
	 * @return 序列化数组还原而成的对象
	 */
	public static Object beanFromBytes(byte[] bytes) {
		ByteArrayInputStream bytesIn = null;
		try {
			bytesIn = new ByteArrayInputStream(bytes); 
			return new ObjectInputStream(bytesIn).readObject();
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		} finally {
			if (bytesIn != null) {
				try {
					bytesIn.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
	/**
	 * 将一个对象序列化成字节数组
	 * @param bean 目标对象
	 * @return 序列化数组
	 */
	public static byte[] bytesFromBean(Object bean) {
		ByteArrayOutputStream bytesOut = null;
		try {
			bytesOut = new ByteArrayOutputStream(); 
			new ObjectOutputStream(bytesOut).writeObject(bean);
			byte[] bytes = bytesOut.toByteArray();
			return bytes;
		} catch (IOException e) {
		} finally {
			if (bytesOut != null) {
				try {
					bytesOut.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
	/**
	 * 克隆
	 * @param object 原对象
	 * @return 克隆对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T clone(T object) {
		ByteArrayOutputStream bytesOut = null;
		ByteArrayInputStream bytesIn = null;
		try {
			bytesOut = new ByteArrayOutputStream(); 
			new ObjectOutputStream(bytesOut).writeObject(object);
			bytesIn = new ByteArrayInputStream(bytesOut.toByteArray()); 
			return (T)new ObjectInputStream(bytesIn).readObject();
		} catch (IOException e) {
			// F.log().d(e.getMessage());
		} catch (ClassNotFoundException e) {
			// F.log().d(e.getMessage());
		} finally {
			if (bytesOut != null) {
				try {
					bytesOut.close();
				} catch (IOException e) {
				}
			}
			if (bytesIn != null) {
				try {
					bytesIn.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
	/**
	 * 一次设置对象的多个属性值<br>
	 * 将一个Properties对象的键值对设置目标对象的对应属性
	 * @param bean 待设置属性的目标对象
	 * @param values Properties对象key值描述属性名称，value值指定对应属性的值，如果value指定的是一个类型对象则表示将对应属性置null。
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int setBeanProperticeValues(Object bean, Properties values) {
		int count = 0;
        Class<? extends Object> type = bean.getClass(); 
        Class<?> bpcls = null;
        Class<? extends Object> vpcls = null;
		try {
	        Map<String, Map<String, Method>> propertyDescriptors = getProperticeMethods(type);
	        Iterator<Entry<Object, Object>> it = values.entrySet().iterator();
	        while (it.hasNext()) {
	        	Entry<Object, Object> en = it.next();
	        	String mkey = en.getKey().toString();
	        	Map<String, Method> methods = propertyDescriptors.get(mkey);
	        	if (methods != null) {
	        		Method writeMethod = methods.get("set");
	                Method readMethod = methods.get("get");
	                bpcls = readMethod.getReturnType();
	                vpcls = en.getValue().getClass();
	                if (List.class.isAssignableFrom(bpcls) && vpcls.isArray()) {
	        			writeMethod.invoke(bean, array2list(en.getValue()));
	            	} else if (List.class.isAssignableFrom(vpcls) && bpcls.isArray()) {
	            		writeMethod.invoke(bean, list2array(bpcls.getComponentType(), (List<?>)en.getValue()));
	            	} else if (bpcls.isEnum() && en.getValue() instanceof String) {
	                	writeMethod.invoke(bean, Enum.valueOf((Class)bpcls, (String)en.getValue()));
	            	} else if (en.getValue() instanceof Class) {
	        			writeMethod.invoke(bean, new Object[] {null});
	                } else {
	        			writeMethod.invoke(bean, en.getValue());
	                }
        			count++;
	        	}
	        }
		} catch (Exception e) {
			if (e instanceof IllegalArgumentException && bpcls != null && vpcls != null) {
				throw new RuntimeException(String.format("%s类型的值不能赋值给%s类型的属性",vpcls.getName(), bpcls.getName()), e);
			} else {
				throw new RuntimeException("系统未知错误", e);
			}
		}
		return count;
	}
	/**
	 * 一次设置对象的多个属性值<br>
	 * 用一个模板对象的非空属性值设置目标对象的对应属性
	 * @param bean 待设置属性的目标对象
	 * @param arg 参数对象
	 * @return 填充的属性值数量
	 */
	public static <T> int setBeanProperticeValues(T bean, T arg) {
		int count = 0;
        Class<? extends Object> type = arg.getClass(); 
		try {
	        Map<String, Map<String, Method>> propertyDescriptors = getProperticeMethods(type);
	        Iterator<Entry<String, Map<String, Method>>> it = propertyDescriptors.entrySet().iterator();
	        while (it.hasNext()) {
	        	Entry<String, Map<String, Method>> en = it.next();
	        	Map<String, Method> descriptor = en.getValue();
	            Method readMethod = descriptor.get("get"); 
	            Method writeMethod = descriptor.get("set");
	            Object val = readMethod.invoke(arg, new Object[0]); 
	            if (val != null) {
	            	// 参数对象的某个属性值不为null，则用该值设置目标对象的同名属性
                	writeMethod.invoke(bean, val);
                	count++;
	            } 
	        }
		} catch (Exception e) {
			throw new RuntimeException("系统未知错误", e);
		}
		return count;
	}
	private static Object list2array(Class<?> cls, List<?> list) {
		Object[] newArray = (Object[])Array.newInstance(cls, list.size());
		for (int i = 0; i < list.size(); i++) {
			newArray[i] = list.get(i);
		}
		return newArray;
	}
	private static Object array2list(Object array) {
		Object[] arr = (Object[])array;
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < arr.length; i++) {
			list.add(arr[i]);
		}
		return list;
	}
	/**
	 * 设置指定bean对象的指定属性值
	 * @param bean 目标bean对象
	 * @param propName 目标属性名称
	 * @param propValue 目标属性值
	 * @return 如果成功返回true，否则返回false
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> boolean setBeanProperticeValue(T bean, String propName, Object propValue) {
        Class<? extends Object> type = bean.getClass();
        Class<?> bpcls = null;
        Class<? extends Object> vpcls = null;
		try {
	        Map<String, Map<String, Method>> propertyDescriptors = getProperticeMethods(type);
	        Map<String, Method> descriptor = propertyDescriptors.get(propName);
            Method writeMethod = descriptor.get("set");
            if (propValue == null) {
            	writeMethod.invoke(bean, propValue);
            	return true;
            }
            Method readMethod = descriptor.get("get");
            bpcls = readMethod.getReturnType();
            vpcls = propValue.getClass();
        	if (List.class.isAssignableFrom(bpcls) && vpcls.isArray()) {
        		// 将一个数组赋值给List
        		writeMethod.invoke(bean, array2list(propValue));
        	} else if (List.class.isAssignableFrom(vpcls) && bpcls.isArray()) {
        		// 将一个List赋值给数组
        		writeMethod.invoke(bean, list2array(bpcls.getComponentType(), (List<?>)propValue));
        	} else if (bpcls.isEnum() && propValue instanceof String) {
            	writeMethod.invoke(bean, Enum.valueOf((Class)bpcls, (String)propValue));
        	} else {
            	writeMethod.invoke(bean, propValue);
        	}
        	return true;
		} catch (Exception e) {
			if (e instanceof IllegalArgumentException && bpcls != null && vpcls != null) {
				throw new RuntimeException(String.format("%s类型的值不能赋值给%s类型的属性", vpcls.getName(), bpcls.getName()), e);
			} else {
				throw new RuntimeException("系统未知错误", e);
			}
		}
	}
	/**
	 * 获取对象的指定属性值
	 * @param bean 目标对象
	 * @param propName 目标对象的目标属性名
	 * @return 目标属性值
	 */
	public static Object getBeanProperticeValue(Object bean, String propName) {
		Map<String, Object> props = getBeanPropertices(bean, propName);
		return props.get(propName);
	}
	/**
	 * 获取对象的非空属性对象
	 * @param bean 目标对象
	 * @param propertiesNames 目标对象的目标属性名，多个属性名之间用','符号分隔，如果指定为null则返回所有属性对象。
	 * @return 目标对象的非空属性对象Map，Map规则：Map<属性名, 属性值>。
	 */
	public static Map<String, Object> getBeanPropertices(Object bean, String propertiesNames) {
		Map<String, Object> props = new HashMap<String, Object>();
		Class<? extends Object> type = bean.getClass(); 
		try {
			List<String> propNames = null;
			if (propertiesNames != null && propertiesNames.length() > 0) {
				String[] pns = propertiesNames.split(",");
				for (int i = 0; i < pns.length; i++) {
					String pn = pns[i].trim();
					if (pn.length() > 0) {
						if (propNames == null) {
							propNames = new ArrayList<String>();
						}
						propNames.add(pn);
					}
				}
			}
	        Map<String, Map<String, Method>> propertyDescriptors = getProperticeMethods(type);
	        Iterator<Entry<String, Map<String, Method>>> it = propertyDescriptors.entrySet().iterator();
	        while (it.hasNext()) {
	        	Entry<String, Map<String, Method>> en = it.next();
	            String propertyName = en.getKey();
	            if (propNames != null && !propNames.contains(propertyName)) {
            		// 非目标属性
            		continue;
	            }
	        	Map<String, Method> descriptor = en.getValue();
	            Method readMethod = descriptor.get("get"); 
                Object val = readMethod.invoke(bean, new Object[0]);
                if (val != null) {
                    props.put(propertyName, val);
                }
                if (propNames != null) {
                    propNames.remove(propertyName);
                }
                if (propNames != null && propNames.size() < 1) {
	            	// 指定了目标属性名而且指定的属性都已经提取完毕则不再继续
	            	break;
	            }
	        }
		} catch (Exception e) {
		}
		return props;
	}
	/**
	 * 判断两个对象是否完全相等
	 * @param obj1
	 * @param obj2
	 * @return 如果相等则返回true，否则返回false。
	 */
	public static boolean equals(Object obj1, Object obj2) {
		if (obj1 == null && obj2 != null ||
			obj2 == null && obj1 != null) {
			return false;
		}
		if (obj1 == null) {
			return true;
		}
		if (!obj1.getClass().equals(obj2.getClass())) {
			// 类型不同
			return false;
		}
		if (obj1 instanceof Comparable) {
			// 实现了比较接口的对象直接比较
			return obj1.equals(obj2);
		} else if (obj1.getClass().isArray()) {
			if (Array.getLength(obj1) != Array.getLength(obj2)) {
				// 数组长度不同
				return false;
			}
			// 逐一比较数组元素
			int len = Array.getLength(obj1);
			for (int i = 0; i < len; i++) {
				Object val1 = Array.get(obj1, i);
				Object val2 = Array.get(obj2, i);
				if (!equals(val1, val2)) {
					return false;
				}
			}
		} else if (obj1 instanceof Collection) {
			if (((Collection<?>)obj1).size() != ((Collection<?>)obj2).size()) {
				// 长度不同
				return false;
			}
			// 逐一比较
			Iterator<?> it1 = ((Collection<?>)obj1).iterator();
			Iterator<?> it2 = ((Collection<?>)obj2).iterator();
			while (it1.hasNext() && it2.hasNext()) {
				Object val1 = it1.next();
				Object val2 = it2.next();
				if (!equals(val1, val2)) {
					return false;
				}
			}
//		} else if (obj1 instanceof Serializable){
//			// 比较序列化以后的字节组
//			byte[] bytes1 = BeanUtil.bytesFromBean(obj1);
//			byte[] bytes2 = BeanUtil.bytesFromBean(obj2);
//			return equals(bytes1, bytes2);
		} else {
			List<String> diffs = differences(obj1, obj2);
			return diffs.size() < 1;
		}
		return true;
	}
	/**
	 * 比较两个对象的属性差异
	 * @param bean1 待比较对象
	 * @param bean2 待比较对象
	 * @return 属性值不相同的属性名列表
	 */
	public static <T> List<String> differences(T bean1, T bean2) {
		List<String> diffs = new ArrayList<String>();
		Map<String, Object> props1 = (Map<String, Object>) getBeanPropertices(bean1, null);
		Map<String, Object> props2 = (Map<String, Object>) getBeanPropertices(bean2, null);
		Iterator<String> it = props1.keySet().iterator();
		while (it.hasNext()) {
			String propName = it.next();
			Object val1 = props1.get(propName);
			Object val2 = props2.get(propName);
			if (val2 == null || !equals(val1, val2)) {
				// bean1有的属性bean2没有或者属性值不一样
				diffs.add(propName);
			}
			if (val2 != null) {
				// bean1和bean2都有的属性则将其从bean2的属性Map中删除，这样最后剩下的就是bean2有而bean1没有的属性了
				props2.remove(propName);
			}
		}
		if (props2.size() > 0) {
			diffs.addAll(props2.keySet());
		}
		return diffs;
	}
	/**
	 * 从指定的对象中查找所有特定类型的对象
	 * @param cls 目标类型
	 * @param bean 目标对象
	 * @param founds 从bean中找到的cls类型的对象
	 * @return 找到的指定类型的对象的数量
	 */
	@SuppressWarnings("unchecked")
	public static <T> int findBeansByClass(Class<T> cls, Object bean, List<T> founds) {
		String beanClassName = bean.getClass().getName();
		if (cls.getName().equals(beanClassName)) {
			// 目标对象本身就是指定类型的对象
			founds.add((T)bean);
			return 1;
		}
		if (bean.getClass().isArray()) {
			// 目标对象是一个数组对象
			int count = 0;
			try {
				Object[] array = (Object[]) bean;
				for (int i = 0; i < array.length; i++) {
					// 循环递归查找
					if (array[i] != null) {
						count += findBeansByClass(cls, array[i], founds);
					}
				}
			} catch (Exception e) {}
			return count;
		} 
		if (bean instanceof Collection) {
			Collection<?> coll = (Collection<?>) bean;
			int count = 0;
			Iterator<?> it = coll.iterator();
			while (it.hasNext()) {
				// 循环递归查找
				Object obj = it.next();
				if (obj != null) {
					count += findBeansByClass(cls, obj, founds);
				}
			}
			return count;
		} 
		if (bean instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) bean;
			int count = 0;
			Iterator<?> it = map.keySet().iterator();
			while (it.hasNext()) {
				// 循环递归查找
				Object key = it.next();
				count += findBeansByClass(cls, key, founds);
				Object val = map.get(key);
				if (val != null) {
					count += findBeansByClass(cls, val, founds);
				}
			}
			return count;
		} 
		if (beanClassName.indexOf("java.") == 0) {
			// 目标对象是一个原生数据类型
			return 0;
		}
		int count = 0;
		Class<? extends Object> type = bean.getClass(); 
		try {
			// 对目标对象的所有属性值进行循环递归查找
	        Map<String, Map<String, Method>> propertyDescriptors = getProperticeMethods(type);
	        Iterator<Entry<String, Map<String, Method>>> it = propertyDescriptors.entrySet().iterator();
	        while (it.hasNext()) {
	        	Entry<String, Map<String, Method>> en = it.next();
	        	Map<String, Method> descriptor = en.getValue();
	            Method readMethod = descriptor.get("get"); 
                Object val = readMethod.invoke(bean, new Object[0]); 
                if (val != null) {
                	if (cls.isInstance(val)) {
                    	count++;
                    	founds.add((T)val);
                	} else {
                		count += findBeansByClass(cls, val, founds);
                	}
                } 
	        }
		} catch (Exception e) {
		}
		return count;
	}
	/**
	 * 用一个模板对象填充一个目标对象的空值属性
	 * @param bean 待填充的目标对象
	 * @param defBean 模板对象
	 * @return 填充的属性值数量
	 */
	public static <T> int fillNulls(T bean, T defBean) {
		int count = 0;
        Class<? extends Object> type = bean.getClass(); 
		try {
	        Map<String, Map<String, Method>> propertyDescriptors = getProperticeMethods(type);
	        Iterator<Entry<String, Map<String, Method>>> it = propertyDescriptors.entrySet().iterator();
	        while (it.hasNext()) {
	        	Entry<String, Map<String, Method>> en = it.next();
	        	Map<String, Method> descriptor = en.getValue();
	            Method readMethod = descriptor.get("get"); 
	            Method writeMethod = descriptor.get("set");
	            Object val = readMethod.invoke(bean, new Object[0]); 
	            if (val == null) {
	            	// 目标对象的某个属性值为空
	                Object defval = readMethod.invoke(defBean, new Object[0]);
	                if (defval != null) {
	                	// 模板对象的同名属性值不为空则进行填充
	                	writeMethod.invoke(bean, defval);
	                	count++;
	                }
	            } 
	        }
		} catch (Exception e) {
		}
		return count;
	}
	/**
	 * 按照java的get/set规则分析指定class的所有方法中具有属性特性的方法<br>
	 * 以get/set成对的为准。
	 * @param cls 目标class
	 * @return 包含所有属性读写方法的Map，规则：Map<"属性名称@属性类型",Map<"set/get", 对应的方法对象>>
	 */
	private static Map<String, Map<String, Method>> getProperticeMethods(Class<?> cls) {
		Map<String, Map<String, Method>> props = new HashMap<String, Map<String, Method>>();
		Method[] methods = cls.getMethods();
		for (Method method : methods) {
			String name = method.getName();
			if (name.indexOf("set") == 0) {
				Class<?>[] pts = method.getParameterTypes();
				if (pts != null && pts.length == 1) {
					// 只有一个参数
					char firstChar = name.charAt(3);
					String propName = ("" + firstChar).toLowerCase() + name.substring(4);
					Map<String, Method> rwMethods = props.get(propName);
					if (rwMethods == null) {
						rwMethods = new HashMap<String, Method>();
						props.put(propName, rwMethods);
					}
					rwMethods.put("set", method);
				}
			} else if (name.indexOf("is") == 0) {
				Class<?>[] pts = method.getParameterTypes();
				Class<?> rt = method.getReturnType();
				if ((pts == null || pts.length == 0) && (!rt.equals(void.class))) {
					// 无参数
					char firstChar = name.charAt(2);
					String propName = ("" + firstChar).toLowerCase() + name.substring(3);
					Map<String, Method> rwMethods = props.get(propName);
					if (rwMethods == null) {
						rwMethods = new HashMap<String, Method>();
						props.put(propName, rwMethods);
					}
					rwMethods.put("get", method);
				}
			} else if (name.indexOf("get") == 0) {
				Class<?>[] pts = method.getParameterTypes();
				Class<?> rt = method.getReturnType();
				if ((pts == null || pts.length == 0) && (!rt.equals(void.class))) {
					// 无参数
					char firstChar = name.charAt(3);
					String propName = ("" + firstChar).toLowerCase() + name.substring(4);
					Map<String, Method> rwMethods = props.get(propName);
					if (rwMethods == null) {
						rwMethods = new HashMap<String, Method>();
						props.put(propName, rwMethods);
					}
					rwMethods.put("get", method);
				}
			}
		}
		Iterator<Entry<String, Map<String, Method>>> it = props.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Map<String, Method>> en = it.next();
			if (en.getValue().size() != 2) {
				it.remove();
			}
		}
		return props;
	}
}
