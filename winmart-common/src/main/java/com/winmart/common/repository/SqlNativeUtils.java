package com.winmart.common.repository;

import com.winmart.common.Constant.CommonConstants;
import com.winmart.common.util.DataUtil;
import com.winmart.common.util.DbWork;
import com.winmart.common.util.ObjectMapperUtils;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Session;
import org.springframework.data.domain.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import java.sql.Connection;
import java.util.*;
import java.util.function.Consumer;

@Slf4j
public class SqlNativeUtils {

    public static final String FROM = "From";
    public static final String TO = "To";
    public static final String AND = " and ";
    public static final String EQUAL = " = :";
    public static final String OR = " or ";
    public static final String GT = " > ";
    public static final String NOT = " not ";
    public static final String GTE = " >= ";
    public static final String LT = " < ";
    public static final String LTE = " <= ";
    public static final String PRE_PARAM = ":";
    private static final String IN_LIST = " in (:list";
    private static final String PARAM_LIST = "list";
    private static final String PAGE = "page";
    private static final String SIZE = "size";

    private static final String SELECT_PAGEABLE = " OFFSET :page ROWS FETCH NEXT :size ROWS ONLY ";

    private SqlNativeUtils() {
    }

    public static List<Object> readList(List<Object> value) {
        if (value == null) {
            return new ArrayList<>();
        } else return value;
    }

    public static void findInOrNot(StringBuilder strBuilder, List<Object> value, Map<String, Object> currentParams, String column, boolean isIn) {
        if (DataUtil.isNullOrEmpty(value)) return;
        strBuilder.append(AND);
        strBuilder.append(column);
        strBuilder.append(isIn ? "" : NOT);
        strBuilder.append(IN_LIST);
        strBuilder.append(currentParams.size());
        strBuilder.append(") ");
        strBuilder.append(CommonConstants.SPECIAL_CHARACTER.NEW_LINE);
        currentParams.put(PARAM_LIST + currentParams.size(), value);
    }

    public static void findActiveByDate(StringBuilder strBuilder, int value, String columnStart, String columnEnd) {
        switch (value) {
            case 1:
                strBuilder.append(String.format(" and DATE(%s) > current_date ", columnStart));
                break;
            case 2:
                strBuilder.append(String.format(" and DATE(%s) <= current_date and current_date <= DATE(%s) ", columnStart, columnEnd));
                break;
            case 3:
                strBuilder.append(String.format(" and DATE(%s) < current_date ", columnEnd));
                break;
            default:
                break;
        }
    }

    public static void findWord(StringBuilder strBuilder, String value, Map<String, Object> currentParams, String... columns) {
        if (!(!DataUtil.isNullOrEmpty(value) && columns.length > 0)) return;
        var iterator = Arrays.stream(columns).iterator();
        strBuilder.append(AND).append("(");
        while (iterator.hasNext()) {
            strBuilder.append(" lower(CAST(").append(iterator.next()).append(" AS CHAR)) like :keyword").append(currentParams.size())
                    .append(iterator.hasNext() ? OR : ")");
        }
        currentParams.put("keyword" + currentParams.size(), "%" + value.toLowerCase().trim() + "%");
    }

    public static void findWordIn(StringBuilder strBuilder, List<String> values, Map<String, Object> currentParams, String... columns) {
        if (!(!DataUtil.isNullOrEmpty(values) && columns.length > 0)) return;
        strBuilder.append(AND).append("(");
        for (int i = 0; i < values.size(); ++i) {
            if (i > 0) {
                strBuilder.append(OR);
            }
            String value = values.get(i);
            var iterator = Arrays.stream(columns).iterator();
            strBuilder.append("(");
            while (iterator.hasNext()) {
                strBuilder.append(" lower(CAST(").append(iterator.next()).append(" AS CHAR)) like :keyword").append(currentParams.size())
                        .append(iterator.hasNext() ? OR : ")");
            }
            currentParams.put("keyword" + currentParams.size(), "%" + value.toLowerCase().trim() + "%");
        }
        strBuilder.append(")");
    }

    public static void findEqual(StringBuilder strBuilder, Object value, Map<String, Object> currentParams, String column) {
        if (DataUtil.isNullOrEmpty(value)) return;
        strBuilder.append(AND);
        strBuilder.append(column);
        strBuilder.append(EQUAL);
        strBuilder.append(column);
        strBuilder.append(CommonConstants.SPECIAL_CHARACTER.NEW_LINE);
        currentParams.put(column, value);
    }


    public static void findByRange(StringBuilder strBuilder, Object from, Object to, Map<String, Object> currentParams, String column) {
        String paramName;
        if (!DataUtil.isNullOrEmpty(from)) {
            paramName = FROM + currentParams.size();
            strBuilder.append(AND).append(column).append(GTE).append(PRE_PARAM).append(paramName).append(CommonConstants.SPECIAL_CHARACTER.SPACE);
            currentParams.put(paramName, from);
        }
        if (!DataUtil.isNullOrEmpty(to)) {
            paramName = TO + currentParams.size();
            strBuilder.append(AND).append(column).append(LT).append(PRE_PARAM).append(paramName).append(CommonConstants.SPECIAL_CHARACTER.NEW_LINE);
            currentParams.put(paramName, to instanceof Date ? DateUtils.addDays((Date) to, 1) : to);
        }
    }

    public static void findByRangeDate(StringBuilder strBuilder, Date from, Date to, Map<String, Object> currentParams, String column) {
        String paramName;
        String dt = "DATE(";
        String columnName = dt + column + ")";
        if (!DataUtil.isNullOrEmpty(from)) {
            paramName = FROM + currentParams.size();
            strBuilder.append(AND).append(columnName).append(GTE).append(dt).append(PRE_PARAM).append(paramName).append(")")
                    .append(CommonConstants.SPECIAL_CHARACTER.SPACE);
            currentParams.put(paramName, from);
        }
        if (!DataUtil.isNullOrEmpty(to)) {
            paramName = TO + currentParams.size();
            strBuilder.append(AND).append(columnName).append(LTE).append(dt).append(PRE_PARAM).append(paramName).append(")")
                    .append(CommonConstants.SPECIAL_CHARACTER.NEW_LINE);
            currentParams.put(paramName, to);
        }
    }


    public static <T> List<T> searchList(EntityManager entityManager, String sqlQuery, Map<String, Object> params, Pageable page, Class<T> clazz) {
        Query query = entityManager.createNativeQuery(appendSort(sqlQuery, page), Tuple.class);
        params.forEach(query::setParameter);
        List<Tuple> data = query.getResultList();
        List<T> result = new ArrayList<>();
        data.forEach(t -> {
            Map<String, Object> tempMap = new HashMap<>();
            for (TupleElement<?> key : t.getElements()) {
                tempMap.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key.getAlias()), t.get(key));
            }
            result.add(ObjectMapperUtils.map(tempMap, clazz));
        });
        return result;
    }


    public static <T> Page<T> searchPage(EntityManager entityManager, String sqlQuery, Map<String, Object> params,
                                         Pageable page, String resultSetMapping) {
        Query query = entityManager.createNativeQuery(appendSort(sqlQuery, page), resultSetMapping);
        int pageNumber = page.getPageNumber();
        int pageSize = page.getPageSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        params.forEach(query::setParameter);
        List<T> resultList = query.getResultList();
        return new PageImpl<>(resultList, page, count(entityManager, sqlQuery, params));
    }

    public static Long count(EntityManager entityManager, String sqlQuery, Map<String, Object> params) {
        Query queryTotal = entityManager.createNativeQuery("select count(*) from (" + sqlQuery + ") res");
        params.forEach(queryTotal::setParameter);
        return DataUtil.safeToLong(queryTotal.getSingleResult(), 0L);
    }

    public static <T> List<T> getList(EntityManager entityManager, String sql, Map<String, Object> params, Class<T> clazz) {
        Query query = entityManager.createNativeQuery(sql, Tuple.class);
        params.forEach(query::setParameter);
        List<Tuple> data = query.getResultList();
        List<T> result = new ArrayList<>();
        data.forEach(t -> {
            Map<String, Object> tempMap = new HashMap<>();
            for (TupleElement<?> key : t.getElements()) {
                tempMap.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key.getAlias()), t.get(key));
            }
            result.add(ObjectMapperUtils.map(tempMap, clazz));
        });
        return result;
    }

    public static <T> List<T> getListLimit(EntityManager entityManager, String sql, Map<String, Object> params,Pageable page, Class<T> clazz) {
        Query query = entityManager.createNativeQuery(sql, Tuple.class);
        int pageNumber = page.getPageNumber();
        int pageSize = page.getPageSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        params.forEach(query::setParameter);
        List<Tuple> data = query.getResultList();
        List<T> result = new ArrayList<>();
        data.forEach(t -> {
            Map<String, Object> tempMap = new HashMap<>();
            for (TupleElement<?> key : t.getElements()) {
                tempMap.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key.getAlias()), t.get(key));
            }
            result.add(ObjectMapperUtils.map(tempMap, clazz));
        });
        return result;
    }

    private static <T> List<T> getListLimit(Query query, Map<String, Object> params, Pageable page, Class<T> clazz) {
        int pageNumber = page.getPageNumber();
        int pageSize = page.getPageSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        return getListLimit(query, params, clazz);
    }

    public static String getQueryConfig(String columnLookup, String configCode, String columnDisplay) {
        return "(select ac.description from app_config ac where ac.value = " + columnLookup
                + " and ac.config_name = '" + configCode + "' LIMIT 1) " + columnDisplay + " \n";
    }

    public static <T> List<T> getListLimit(Query query, Map<String, Object> params, Class<T> clazz) {
        params.forEach(query::setParameter);
        List<Tuple> data = query.getResultList();
        List<T> result = new ArrayList<>();
        data.forEach(t -> {
            Map<String, Object> tempMap = new HashMap<>();
            for (TupleElement<?> key : t.getElements()) {
                tempMap.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key.getAlias()), t.get(key));
            }
            result.add(ObjectMapperUtils.map(tempMap, clazz));
        });
        return result;
    }

    public static <T> Page<T> searchPage(EntityManager entityManager, String sqlQuery, Map<String, Object> params, Pageable page, Class<T> clazz) {
        return new PageImpl<>(
                getListLimit(entityManager.createNativeQuery(appendSort(sqlQuery, page), Tuple.class), params, page, clazz),
                page,
                count(entityManager, sqlQuery, params));
    }

    public static void appendPage(StringBuilder sqlBuilder, Map<String, Object> map, Pageable pageable) {
        if (pageable != null) {
            sqlBuilder.append(SELECT_PAGEABLE);
            map.put(PAGE, DataUtil.pageSize(pageable));
            map.put(SIZE, pageable.getPageSize());
        }
    }

    public static String appendSort(String query, Pageable page) {
        if (page == null) return query;
        var iterator = page.getSort().stream().iterator();
        if (!iterator.hasNext()) {
            return query;
        }
        StringBuilder strSort = new StringBuilder(query);
        strSort.append(CommonConstants.SPECIAL_CHARACTER.SPACE).append(CommonConstants.SQL_STATEMENT.ORDER_BY);
        Sort.Order order;
        while (iterator.hasNext()) {
            order = iterator.next();
            strSort.append(order.getProperty()).append(CommonConstants.SPECIAL_CHARACTER.SPACE).append(order.getDirection().name())
                    .append(iterator.hasNext() ? CommonConstants.SPECIAL_CHARACTER.COMMA : CommonConstants.SPECIAL_CHARACTER.SPACE);
        }
        return strSort.toString();
    }

    public static Pageable changeFormat(Pageable page) {
        var iterator = page.getSort().stream().iterator();
        if (!iterator.hasNext()) {
            return page;
        }
        List<Sort.Order> orders = new ArrayList<>();
        Sort.Order currentSort;
        while (iterator.hasNext()) {
            currentSort = iterator.next();
            orders.add(currentSort
                    .withProperty(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, currentSort.getProperty()))
                    .with(currentSort.getDirection()));
        }
        return PageRequest.of(page.getPageNumber(), page.getPageSize(), Sort.by(orders));
    }

    public static Pageable remapColSort(Pageable page, Map<String, String> map) {
        var iterator = page.getSort().stream().iterator();
        if (!iterator.hasNext() || map.isEmpty()) {
            return page;
        }
        List<Sort.Order> orders = new ArrayList<>();
        Sort.Order currentSort;
        while (iterator.hasNext()) {
            currentSort = iterator.next();
            orders.add(currentSort.withProperty(map.get(currentSort.getProperty())).with(currentSort.getDirection()));
        }
        return PageRequest.of(page.getPageNumber(), page.getPageSize(), Sort.by(orders));
    }

    public static String remapColSortToString(Pageable page, Map<String, String> map) {
        var iterator = page.getSort().stream().iterator();
        if (!iterator.hasNext() || map.isEmpty()) {
            return null;
        }
        StringBuilder orders = new StringBuilder(" order by ");
        Sort.Order currentSort;
        while (iterator.hasNext()) {
            currentSort = iterator.next();
            orders.append(map.get(currentSort.getProperty() + currentSort.getDirection().name()));
        }
        return orders.toString();
    }

    public static <T> List<T> searchList(EntityManager entityManager, String sqlQuery, Map<String, Object> params, Class<T> clazz) {
        Query query = entityManager.createNativeQuery(sqlQuery, clazz);
        params.forEach(query::setParameter);
        return query.getResultList();
    }

    public static List<Object[]> queryAndReturn(String procedure, Map<String, Object> map, EntityManager entityManager) {
        Query query = entityManager.createNativeQuery(procedure);
        map.forEach(query::setParameter);
        return query.getResultList();
    }

    public static <T> List<T> queryAndReturn(String procedure, Map<String, Object> map, EntityManager entityManager, Class<T> clazz) {
        Query query = entityManager.createNativeQuery(procedure);
        map.forEach(query::setParameter);
        List<Tuple> data = query.getResultList();
        List<T> result = new ArrayList<>();
        data.forEach(t -> {
            Map<String, Object> tempMap = new HashMap<>();
            for (TupleElement<?> key : t.getElements()) {
                tempMap.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key.getAlias()), t.get(key));
            }
            result.add(ObjectMapperUtils.map(tempMap, clazz));
        });
        return result;
    }


    public static List<Map<String, Object>> convertTuplesToMap(List<Tuple> tuples) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Tuple single : tuples) {
            Map<String, Object> tempMap = new HashMap<>();
            for (TupleElement<?> key : single.getElements()) {
                tempMap.put(key.getAlias(), single.get(key));
            }
            result.add(tempMap);
        }
        return result;
    }

    public static void setParamsPageable(Map<String, Object> params, Pageable pageable) {
        params.put(PAGE, pageable != null ? DataUtil.pageSize(pageable) : null);
        params.put(SIZE, pageable != null ? pageable.getPageSize() : null);
    }

    public static <T> Page<T> queryReturnPage(List<T> listResult, Pageable pageable, Integer totalCount) {
        return new PageImpl<>(listResult, pageable, totalCount);
    }

    public static boolean process(EntityManager entityManager, Consumer<Connection> consumer) {
        try (Session session = entityManager.unwrap(Session.class)) {
            DbWork myWork = new DbWork();
            session.doWork(myWork);
            Connection conn = myWork.getConnection();
            consumer.accept(conn);
        }
        return true;
    }
}
