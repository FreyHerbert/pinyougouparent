package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLDataException;
import java.util.List;
import java.util.Map;

/**
 * 品牌接口实现类
 * @author leiyu
 */
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper brandMapper;

    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }

    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        // 分页条件设置
        PageHelper.startPage(pageNum, pageSize);
        // 查询出数据，将其转换成 Page<T> 对象
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);
        // 由 Page<T> 对象内部进行分页处理，并返回结果
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public PageResult findPage(TbBrand brand, int pageNum, int pageSize) {
        // 分页条件设置
        PageHelper.startPage(pageNum, pageSize);

        TbBrandExample tbBrandExample = new TbBrandExample();
        TbBrandExample.Criteria criteria = tbBrandExample.createCriteria();
        // 判断查询条件是否为空
        if (brand != null) {
            if (brand.getName() != null && brand.getName().trim().length() > 0) {
                // 设置查询条件
                criteria.andNameLike("%" + brand.getName() + "%");
            }
            if (brand.getFirstChar() != null && brand.getFirstChar().trim().length() > 0) {
                // 设置查询条件
                criteria .andFirstCharLike("%" + brand.getFirstChar() + "%");
            }
        }

        // 查询出数据，将其转换成 Page<T> 对象
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(tbBrandExample);
        // 由 Page<T> 对象内部进行分页处理，并返回结果
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(TbBrand brand){
        TbBrandExample tbBrandExample = new TbBrandExample();
        tbBrandExample.createCriteria().andNameEqualTo(brand.getName());
        List<TbBrand> tbBrands = brandMapper.selectByExample(tbBrandExample);
        if (tbBrands.size() > 0) {
            throw new RuntimeException("品牌重复");
        }
        brandMapper.insert(brand);
    }

    @Override
    public TbBrand findOne(Long id) {
        TbBrand tbBrand = brandMapper.selectByPrimaryKey(id);
        return tbBrand;
    }

    @Override
    public void update(TbBrand brand) {
        brandMapper.updateByPrimaryKey(brand);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            brandMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<Map> selectOptionList() {
        return brandMapper.selectOptionList();
    }
}
