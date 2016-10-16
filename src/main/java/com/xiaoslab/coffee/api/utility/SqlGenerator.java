package com.xiaoslab.coffee.api.utility;

/**
 * Created by ipeli on 10/2/16.
 */
public class SqlGenerator {


    public static String generateSelectQuery(Object object) {
       /* StringBuilder whereClause = new StringBuilder();

        Hashtable<String, Object> fields = AppUtility.getFields(object);
        Set<String> keys = fields.keySet();


        if(fields.size()>0) {
            int count= 0;
            whereClause.append(" where ");
            for (String key : keys) {
                if(fields.get(key).equals(null)) {
                    if (count != 0) whereClause.append(" And ");
                    whereClause.append(key.toString() + " = " + fields.get(key));
                }
            }
        }

        StringBuilder sqlQuery = new StringBuilder("Select * From ");
        sqlQuery.append(object.getClass().getSimpleName().toString());
        sqlQuery.append(whereClause.toString());
        return sqlQuery.toString();*/

        return null;
    }

    public static String generateInsertQuery(Object object) {
        StringBuilder sqlQuery = new StringBuilder();

        return sqlQuery.toString();
    }

    public static String generateUpdateQuery(Object object) {
        StringBuilder sqlQuery = new StringBuilder();

        return sqlQuery.toString();
    }

    public static String generateDeleteQuery(Object object) {
        StringBuilder sqlQuery = new StringBuilder();

        return sqlQuery.toString();
    }


}
