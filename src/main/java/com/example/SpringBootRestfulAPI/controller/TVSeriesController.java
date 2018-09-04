package com.example.SpringBootRestfulAPI.controller;

import com.example.SpringBootRestfulAPI.Service.TVSeriesService;
import com.example.SpringBootRestfulAPI.POJO.TVSeries;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

@RestController//返回Json
@RequestMapping("/tvseries")
public class TVSeriesController {

    private static final Log log = (Log) LogFactory.getLog(TVSeriesController.class);//日志

    @Autowired TVSeriesService tvSeriesService;

    @GetMapping
    public List<TVSeries> getAll(){
        if (log.isTraceEnabled()){
            log.trace("getAll();被调用了");
        }
        List<TVSeries> list = tvSeriesService.getAllSeries();
        if (log.isTraceEnabled()){
            log.trace("查询获取"+list.size()+"条记录");
        }

        return list;

    }

    @GetMapping("/{id}")
    private TVSeries getOne(@PathVariable int id){//这个参数的值会从@PathVariable中取
        if(log.isTraceEnabled()){//日志
            log.trace("getOne: " + id);
        }
        return getTvSeriesDto(id);
    }

    @PostMapping
    public TVSeries insterOne(@RequestBody TVSeries tvSeriesDto){//在Postman中的body处传入TVSeriesDto类的对象
        if (log.isTraceEnabled()){
            log.trace("传递进来的参数是" + tvSeriesDto);//日志输出
        }
        tvSeriesDto.setId(999);
        return tvSeriesDto;
    }

    @PutMapping("/{id}")//没有数据库 部分功能没有实现
    public TVSeries updateOne(@PathVariable int id , @RequestBody TVSeries tvSeriesDto){//在Postman中的body处传入TVSeriesDto类的对象
        if (log.isTraceEnabled()){
            log.trace("updateOne: "+ id);
        }
        return getTvSeriesDto(id);
    }

    @DeleteMapping("/{id}")
    public Map<String,String> deleteOne(@PathVariable int id, HttpServletRequest request,
                                        @RequestParam(value = "delete_reason",required = false)String deleteReason)throws Exception{
                                        //在url？后读取delete_reason的参数
        if(log.isTraceEnabled()){
            log.trace("deleteOne" + id);
        }
        Map<String,String> result = new HashMap<>();
        if(id == 101){
            result.put("message","#101被" + request.getRemoteAddr()+ "删除（原因" +deleteReason+"）");
        }else if(id ==102){
            throw new RuntimeException("#102不能删除");
        }else {
            throw new ResourceNotFoundException();
        }
        return result;
    }

    //上传图片
    //可以直接使用MultipartFile中的流来保存文件
    @PostMapping(value = "/{id}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)//指定请求数据的格式，文件上传MULTIPART_FORM_DATA_VALUE这种格式
    public void addPhoto(@PathVariable int id , @RequestParam("photo") MultipartFile imgFile) throws Exception{
        if (log.isTraceEnabled()){
            log.trace("接收到文件" +id+ "收到文件： " + imgFile.getOriginalFilename());
        }
        //保存图片
        FileOutputStream fos = new FileOutputStream("target/"+ imgFile.getOriginalFilename());
        IOUtils.copy(imgFile.getInputStream(),fos);
        fos.close();
    }

    //读取图片
    @GetMapping(value = "/{id}/xg", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getIcon(@PathVariable int id) throws Exception{//返回数组对象
        if(log.isTraceEnabled()){
            log.trace("getIcon("+id+")");
        }
        String xg = "target/xg.png";
        InputStream is = new FileInputStream(xg);
        //FileInputStream是Java语言中抽象类InputStream用来具体实现类的创建对象。FileInputStream可以从文件系统中的某个文件中获得输入字节
        return org.apache.commons.io.IOUtils.toByteArray(is);//将流转为数组对象
    }

    private TVSeries getTvSeriesDto(@PathVariable int id) {
        if (id == 101){
            return createBigBang();
        }else if (id == 102){
            return createWestWorld();
        }else {
            throw new ResourceNotFoundException();
        }
    }

    private TVSeries createBigBang(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2008,Calendar.OCTOBER,3,0,0);
        return new TVSeries(101,"The Big Bang Theory",11,calendar.getTime());
    }

    private TVSeries createWestWorld(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016,Calendar.OCTOBER,2,0,0);
        return new TVSeries(102,"WestWorld",1,calendar.getTime());
    }
}