package com.winmart.common.dto;

import com.winmart.common.condition.Condition;
import com.winmart.common.model.SortField;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDto {
  int page;
  int size;
  private List<Condition> lsCondition;
  private List<SortField> sortField;
}
