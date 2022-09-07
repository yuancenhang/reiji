package com.hang.reiji.dto;

import com.hang.reiji.domain.Setmeal;
import com.hang.reiji.domain.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private static final long serialVersionUID = 2679772237684847006L;

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
