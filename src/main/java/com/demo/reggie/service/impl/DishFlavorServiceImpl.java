package com.demo.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.reggie.entity.DishFlavor;
import com.demo.reggie.mapper.DishFlavorMapper;
import com.demo.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * @author Hunter Chen
 * @date 2022/4/18
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
