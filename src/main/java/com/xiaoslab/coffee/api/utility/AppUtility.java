package com.xiaoslab.coffee.api.utility;

/**
 * Created by ipeli on 10/15/16.
 */
public class AppUtility {

//    public static Hashtable<String, Object> getFields(Object object){
//        Class theClass = object.getClass();
//        Hashtable<String, Object> privateFields = new Hashtable<String, Object>();
//
//        Field[] fields = theClass.getDeclaredFields();
//
//        for (Field field : fields) {
//            Object value = new Object();
//            String fieldName = field.getName().substring(0,1).toUpperCase().toString() + field.getName().substring(1,field.getName().length());
//            try {
//                Method m = object.getClass().getMethod("get" + fieldName, new Class[] {});
//                if(!m.invoke(object).equals(null)) {
//                    value = m.invoke(object);
//                }
//            }
//            catch (NoSuchMethodException e){
//                continue;
//            }
//            catch (InvocationTargetException e){
//
//                continue;
//            }
//            catch (IllegalAccessException e){
//                continue;
//            }
//
//            privateFields.put(fieldName, value);
//        }
//        return privateFields;
//    }
}
