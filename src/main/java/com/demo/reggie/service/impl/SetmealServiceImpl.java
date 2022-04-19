package com.demo.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.reggie.common.CustomException;
import com.demo.reggie.dto.SetmealDto;
import com.demo.reggie.entity.Setmeal;
import com.demo.reggie.entity.SetmealDish;
import com.demo.reggie.mapper.SetmealMapper;
import com.demo.reggie.service.DishService;
import com.demo.reggie.service.SetmealDishService;
import com.demo.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hunter Chen
 * @date 2022/4/16
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {
        //如果是启售状态，抛出异常，删除失败
        for (Long id : ids) {
            LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Setmeal::getId, id);
            Setmeal setmeal = this.getOne(lambdaQueryWrapper);
            if (setmeal.getStatus() == 1) {
                throw new CustomException("套餐启售中，删除失败");
            } else {
                //删除Setmeal
                this.removeById(setmeal);
                //删除SetmealDish
                LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SetmealDish::getSetmealId, id);
                setmealDishService.remove(queryWrapper);
            }
        }
    }

}
