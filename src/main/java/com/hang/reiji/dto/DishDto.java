package com.hang.reiji.dto;

import com.hang.reiji.domain.Dish;
import com.hang.reiji.domain.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private static final long serialVersionUID = 3496198094797775987L;

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
