package com.winmart.common.condition;

import com.winmart.common.Constant.CommonConstants;
import com.winmart.common.util.DataUtil;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Condition {
  private static final Logger LOG = LogManager.getLogger(Condition.class);
  private String property;
  private String propertyType;
  private String operator;
  private Object value;
  private Object toValue;
  private List<Condition> lstCondition;
  private String expType;

  public Condition() {}

  public Condition(String property, String operator, Object value) {
    this.property = property;
    this.operator = operator;
    this.value = value;
    this.propertyType = CommonConstants.SQL_PRO_TYPE.STRING;
  }

  public Condition(String property, String propertyType, String operator, Object value) {
    this.property = property;
    this.propertyType = propertyType;
    this.operator = operator;
    if (this.propertyType.equals(CommonConstants.SQL_PRO_TYPE.LONG)) {
      if (operator.equals(CommonConstants.SQL_OPERATOR.IN)) {
        this.value = DataUtil.convertIntegerArr(value);
      } else {
        try {
          this.value = Long.parseLong((String) value);
        } catch (NumberFormatException e) {
          LOG.info("Log in condition format: " + e.toString());
          this.value = value;
        }
      }
    } else {
      this.value = value;
    }
  }

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public String getPropertyType() {
    return propertyType;
  }

  public void setPropertyType(String propertyType) {
    this.propertyType = propertyType;
  }

  public List<Condition> getLstCondition() {
    return lstCondition;
  }

  public void setLstCondition(List<Condition> lstCondition) {
    this.lstCondition = lstCondition;
  }

  public String getExpType() {
    return expType;
  }

  public void setExpType(String expType) {
    this.expType = expType;
  }

  @Override
  public String toString() {
    return "Condition{"
        + "property='"
        + property
        + '\''
        + ", operator='"
        + operator
        + '\''
        + ", value='"
        + value
        + '\''
        + '}';
  }
}
