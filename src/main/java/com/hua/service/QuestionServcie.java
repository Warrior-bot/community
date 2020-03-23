package com.hua.service;

import com.hua.dto.QuesstionDTO;
import com.hua.dto.pageDTO;
import com.hua.mapper.QuesstionMapper;
import com.hua.mapper.UserMapper;
import com.hua.model.Quesstion;
import com.hua.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServcie {
    @Autowired
    private QuesstionMapper quesstionMapper;
    @Autowired
    private UserMapper userMapper;

    //index的方法
    //分页
    public pageDTO list(Integer page, Integer size) {

        pageDTO paginationDTO = new pageDTO();
        Integer totalPage;
        Integer totalCount = quesstionMapper.count();
        if (totalCount %size ==0){
            totalPage = totalCount /size;
        }else{
            totalPage = totalCount /size +1;
        }
        if (page<1){
            page=1;
        }
        //删除Mysql数据库的时候出现limit-5，5的时候，因为把数据库删了，所以totalpage=0导致page>totalpage然后怕个=0；
        if (page>totalPage&&totalPage!=0){
            page=totalPage;
        }
        paginationDTO.setPagination(totalPage,page);

        //这里写分页
        //5*(i-1)———>size*(page-1)
        Integer offset =size * (page-1);
        //从mapper层查出所有的question对象 在传入分页  在去mapper层修改
        List<Quesstion> questions = quesstionMapper.list(offset,size);
        //这里创建QuestionDTOList是为了将questionDTO放入
        List<QuesstionDTO> quesstionDTOList = new ArrayList<>();

        //循环遍历question
        for (Quesstion question : questions) {
            //将question的creator（和User的ID对应）放入findById中去user表里面根据id查用户
            User user =userMapper.findById(question.getCreator());
            //创建一个新的questionDTO对象
            QuesstionDTO quesstionDTO = new QuesstionDTO();
            //老办法:quesstionDTO.setId(question.getId()); 一个一个接着写
            //spring提供的新办法：快速将question对象的属性拷贝到questionDTO中
            BeanUtils.copyProperties(question, quesstionDTO);
            quesstionDTO.setUser(user);
            //每一次循环都把questionDTO里面的User对象放入questionDTOList里面
            quesstionDTOList.add(quesstionDTO);
        }
        //本来之前是返回quesstionDTOList但是现在需要传递page size参数，所以新增了pageDTO类，并且把quesstionDTOList包含进去
        paginationDTO.setQuesstions(quesstionDTOList);
        return paginationDTO;
    }

    //profile的方法
    public pageDTO list(Integer userId, Integer page, Integer size) {
        pageDTO paginationDTO = new pageDTO();
        Integer totalPage;
        Integer totalCount = quesstionMapper.countByUserId(userId);
        if (totalCount %size ==0){
            totalPage = totalCount /size;
        }else{
            totalPage = totalCount /size +1;
        }
        if (page<1){
            page=1;
        }
        if (page>totalPage){
            page=totalPage;
        }
        paginationDTO.setPagination(totalPage,page);

        Integer offset =size * (page-1);
        //这里返回的questions放到for循环里面去循环遍历，
        List<Quesstion> questions = quesstionMapper.listByUserId(userId, offset, size);

        List<QuesstionDTO> quesstionDTOList = new ArrayList<>();

        for (Quesstion question : questions) {
            User user =userMapper.findById(question.getCreator());
            QuesstionDTO quesstionDTO = new QuesstionDTO();
            BeanUtils.copyProperties(question, quesstionDTO);
            quesstionDTO.setUser(user);
            //将quesstionDTO的对象加入到quesstionDTOList的集合
            quesstionDTOList.add(quesstionDTO);
        }
        //将quesstionDTOList放入paginationDTO在返回回去
        paginationDTO.setQuesstions(quesstionDTOList);
        return paginationDTO;
    }

    //Question的方法
    public QuesstionDTO getById(Integer id) {
        Quesstion quesstion=quesstionMapper.getById(id);
        QuesstionDTO quesstionDTO = new QuesstionDTO();
        BeanUtils.copyProperties(quesstion, quesstionDTO);
        User user =userMapper.findById(quesstion.getCreator());
        quesstionDTO.setUser(user);
        return quesstionDTO;
    }

    //判断是否有ID传入，如果有显示ID对应的内容，没有就不显示
    public void createOrUpdate(Quesstion quesstion) {
        if (quesstion.getId() == null){
            //创建
            quesstionMapper.create(quesstion);
        }else{
            //更新
            quesstionMapper.update(quesstion);
        }
    }
}
