package com.winmart.common.specification;

import com.winmart.common.Constant.CommonConstants;
import com.winmart.common.condition.Condition;
import com.winmart.common.model.BaseEntity;
import com.winmart.common.util.DataUtil;
import com.winmart.common.util.DateUtil;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

public class BaseSpecification<T extends BaseEntity> implements Specification<T> {
    private final List<Condition> conditionList;

    public BaseSpecification() {
        this.conditionList = new ArrayList<>();
    }

    public void add(Condition condition) {
        conditionList.add(condition);
    }

    public void add(List<Condition> conditions) {
        Condition conditionDefault = new Condition();
        conditionDefault.setProperty("isDeleted");
        conditionDefault.setPropertyType("Boolean");
        conditionDefault.setOperator("EQUAL");
        conditionDefault.setValue(false);
        conditions.add(conditionDefault);
        conditionList.addAll(conditions);
    }

    public void addNoDefault(List<Condition> conditions) {
        conditionList.addAll(conditions);
    }

    public jakarta.persistence.criteria.Predicate buildQueryCriterion(
            Condition condition, Root<T> root, CriteriaBuilder criteriaBuilder) {
        if (CommonConstants.SQL_LOGIC.OR.equalsIgnoreCase(condition.getExpType())
                || CommonConstants.SQL_LOGIC.AND.equalsIgnoreCase(condition.getExpType())) {
            return buildOrCriterion(
                    condition.getLstCondition(), root, condition.getExpType(), criteriaBuilder);
        } else {
            return buildPredicate(root, criteriaBuilder, condition);
        }
    }

    public jakarta.persistence.criteria.Predicate buildOrCriterion(
            List<Condition> lstCondition, Root<T> root, String expType, CriteriaBuilder criteriaBuilder) {
        List<jakarta.persistence.criteria.Predicate> lstPredicate = new ArrayList<>();
        for (Condition con : lstCondition) {
            lstPredicate.add(buildQueryCriterion(con, root, criteriaBuilder));
        }
        jakarta.persistence.criteria.Predicate[] predicatesArray = lstPredicate.toArray(new jakarta.persistence.criteria.Predicate[0]);
        if (CommonConstants.SQL_LOGIC.OR.equalsIgnoreCase(expType)) {
            return criteriaBuilder.or(predicatesArray);
        } else {
            return criteriaBuilder.and(predicatesArray);
        }
    }

    public jakarta.persistence.criteria.Predicate buildPredicate(
            Root<T> root, CriteriaBuilder criteriaBuilder, Condition criteria) {
        switch (criteria.getOperator()) {
            case "GREATER":
                return buildPredicateGreater(root, criteriaBuilder, criteria);
            case "LOWER":
                return buildPredicateLower(root, criteriaBuilder, criteria);
            case "GREATER_EQUAL":
                return buildPredicateGreaterEqual(root, criteriaBuilder, criteria);
            case "LOWER_EQUAL":
                return buildPredicateLowerEqual(root, criteriaBuilder, criteria);
            case "NOT_EQUAL":
                return criteriaBuilder.notEqual(root.get(criteria.getProperty()), criteria.getValue());
            case "EQUAL":
                return buildPredicateEqual(root, criteriaBuilder, criteria);
            case "LIKE":
                return criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(criteria.getProperty())),
                        "%" + criteria.getValue().toString().toLowerCase() + "%");
            case "IN":
                return criteriaBuilder
                        .in(root.get(criteria.getProperty()))
                        .value(
                                getValue(criteria.getPropertyType(), criteria.getOperator(), criteria.getValue()));
            case "NOT_IN":
                return criteriaBuilder
                        .not(root.get(criteria.getProperty()))
                        .in(getValue(criteria.getPropertyType(), criteria.getOperator(), criteria.getValue()));
            default:
                break;
        }
        return null;
    }

    public Object getValue(String propertyType, String operator, Object value) {
        if (CommonConstants.SQL_OPERATOR.IN.equalsIgnoreCase(operator)) {
            if (CommonConstants.SQL_PRO_TYPE.LONG.equalsIgnoreCase(propertyType)) {
                value = DataUtil.convertLongArr(value);
                return value;
            } else if (CommonConstants.SQL_PRO_TYPE.INT.equalsIgnoreCase(propertyType)) {
                value = DataUtil.convertIntegerArr(value);
                return value;
            } else if (CommonConstants.SQL_PRO_TYPE.DOUBLE.equalsIgnoreCase(propertyType)) {
                value = DataUtil.convertDoubleArr(value);
                return value;
            } else if (CommonConstants.SQL_PRO_TYPE.STRING.equalsIgnoreCase(propertyType)) {
                value = DataUtil.convertToStringArr(value);
                return value;
            }
        }
        return value;
    }

    protected jakarta.persistence.criteria.Predicate buildPredicateGreater(
            Root<T> root, CriteriaBuilder criteriaBuilder, Condition criteria) {
        if (criteria.getPropertyType().toLowerCase().equals(CommonConstants.SQL_PRO_TYPE.DATE)) {
            var dateValue = DateUtil.parseStringToDatetime(criteria.getValue());
            return criteriaBuilder.greaterThan(
                    criteriaBuilder.function("DATE", Date.class, root.get(criteria.getProperty())),
                    dateValue);
        } else {
            return criteriaBuilder.greaterThan(
                    root.get(criteria.getProperty()), criteria.getValue().toString());
        }
    }

    protected jakarta.persistence.criteria.Predicate buildPredicateLower(
            Root<T> root, CriteriaBuilder criteriaBuilder, Condition criteria) {
        if (criteria.getPropertyType().toLowerCase().equals(CommonConstants.SQL_PRO_TYPE.DATE)) {
            var dateValue = DateUtil.parseStringToDatetime(criteria.getValue());
            return criteriaBuilder.lessThan(
                    criteriaBuilder.function("DATE", Date.class, root.get(criteria.getProperty())),
                    dateValue);
        } else {
            return criteriaBuilder.lessThan(
                    root.get(criteria.getProperty()), criteria.getValue().toString());
        }
    }

    protected jakarta.persistence.criteria.Predicate buildPredicateGreaterEqual(
            Root<T> root, CriteriaBuilder criteriaBuilder, Condition criteria) {
        if (criteria.getPropertyType().toLowerCase().equals(CommonConstants.SQL_PRO_TYPE.DATE)) {
            var dateValue = DateUtil.parseStringToDatetime(criteria.getValue());
            return criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getProperty()), dateValue);
        } else {
            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get(criteria.getProperty()), criteria.getValue().toString());
        }
    }

    protected jakarta.persistence.criteria.Predicate buildPredicateLowerEqual(
            Root<T> root, CriteriaBuilder criteriaBuilder, Condition criteria) {
        if (criteria.getPropertyType().toLowerCase().equals(CommonConstants.SQL_PRO_TYPE.DATE)) {
            var dateValue = DateUtil.parseStringToDatetime(criteria.getValue());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateValue);
            //            calendar.add(Calendar.MINUTE, 1);
            return criteriaBuilder.lessThanOrEqualTo(
                    root.get(criteria.getProperty()), calendar.getTime());
        } else {
            return criteriaBuilder.lessThanOrEqualTo(
                    root.get(criteria.getProperty()), criteria.getValue().toString());
        }
    }

    protected jakarta.persistence.criteria.Predicate buildPredicateEqual(
            Root<T> root, CriteriaBuilder criteriaBuilder, Condition criteria) {
        if (criteria.getPropertyType().toLowerCase().equals(CommonConstants.SQL_PRO_TYPE.DATE)) {
            var dateValue = DateUtil.parseStringToDatetime(criteria.getValue());
            return criteriaBuilder.equal(
                    criteriaBuilder.function("DATE", Date.class, root.get(criteria.getProperty())),
                    dateValue);
        } else {
            return criteriaBuilder.equal(root.get(criteria.getProperty()), criteria.getValue());
        }
    }

    @Override
    public jakarta.persistence.criteria.Predicate toPredicate(
            jakarta.persistence.criteria.Root<T> root,
            CriteriaQuery<?> query,
            jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder) {

        if (conditionList == null || conditionList.isEmpty()) {
            return criteriaBuilder.conjunction();
        }

        List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

        for (Condition condition : conditionList) {
            jakarta.persistence.criteria.Predicate predicate = buildQueryCriterion(
                    condition,
                    (Root<T>) root,
                    (CriteriaBuilder) criteriaBuilder
            );
            if (predicate != null) {
                predicates.add(predicate);
            }
        }

        if (predicates.isEmpty()) {
            return criteriaBuilder.conjunction();
        }

        return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
    }
}
