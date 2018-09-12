package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.sql.SQLDataException;
import java.util.List;
import java.util.Map;

/**
 * 品牌接口
 * @author leiyu
 */
public interface BrandService {
    /**
     * 返回包含所有品牌表中信息的集合
     * @return
     */
    List<TbBrand> findAll();

    /**
     * 分页方法，对品牌进行分页
     * @param pageNum 当前页号
     * @param pageSize 页大小（一页显示几条）
     * @return 返回分好页的数据，这些数据用 RageResult 进行封装
     */
    PageResult findPage(int pageNum, int pageSize);

    /**
     * 更具条件进行品牌信息查询，及提供分页功能
     * @param brand 查询条件，用相应的 pojo 进行封装
     * @param pageNum 当前页号
     * @param pageSize 页大小（一页显示几条）
     * @return 返回分好页的数据，这些数据用 RageResult 进行封装
     */
    PageResult findPage(TbBrand brand, int pageNum, int pageSize);

    /**
     * 增加品牌信息
     * @param brand 增加的品牌信息
     * @exception SQLDataException 出现重复的品牌抛出该异常
     */
    void add(TbBrand brand);

    /**
     * 根据 id 查询品牌信息
     * @param id 要查询的 id
     * @return 包含表中有信息返回品牌信息的对象，否则为null
     */
    TbBrand findOne(Long id);

    /**
     * 修改品牌信息
     * @param brand 包含品牌信息的对象，对其进行修改
     */
    void update(TbBrand brand);

    /**
     * 根据 id 删除品牌信息
     * @param ids 这是个数组，删除数组中 id 所对应的品牌信息
     */
    void delete(Long[] ids);

    /**
     * 在使用 select2 前端控件时，对数据各式有了改变。
     * 该方法就是进行数据封装成 select2 所需要的格式。
     * @return 返回下拉列表数据
     */
    List<Map> selectOptionList();
}
