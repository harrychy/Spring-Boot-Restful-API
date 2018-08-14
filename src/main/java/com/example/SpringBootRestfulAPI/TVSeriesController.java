package com.example.SpringBootRestfulAPI;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController//返回Json
@RequestMapping("/tvseries")
public class TVSeriesController {

    @GetMapping
    public List<TVSeriesDto> sayHello(){
        List<TVSeriesDto> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016,Calendar.OCTOBER,2,0,0);
        list.add(new TVSeriesDto(1,"WestWorld",1,calendar.getTime()));
        return list;
    }
}
