package com.shop.controller;

import com.shop.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafExController {

    @GetMapping(value = "/ex01")
    public String thymeleafExample01(Model model){  // model 객체를 이용해 뷰에 전달한 데이터를 key, value 구조로 넣어줌.
        model.addAttribute("data", "타임리프 예제 입니다. ");
        return "thymeleafEx/thymeleafEx01"; //templates 폴더를 기준으로 뷰의 위치와 이름(thymeleafEx01.html)을 반환.
    }

    @GetMapping(value = "/ex02")    //ItemDto 객체를 생성해 모델에 데이터를 담아 뷰단으로 전달
    public String thymeleafExample02(Model model){  // 상품 데이터 출력 예제
        ItemDto itemDto = new ItemDto();
        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemNm("테스트 상품 1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDto",itemDto);
        return "thymeleafEx/thymeleafEx02";

    }
    @GetMapping(value = "/ex03")
    public String thymeleafExample03(Model model){  //상품 리스트 출력 예제

        List<ItemDto> itemDtoList = new ArrayList<>();

        for(int i=1;i<=10;i++){ //출력할 10가지의 itemDto 객체 10가지 생성. 후 itemDtoList에 넣어줌

            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명" +i);
            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(10000*i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);

        }
        model.addAttribute("itemDtoList",itemDtoList);
        return "thymeleafEx/thymeleafEx03";
    }


    @GetMapping(value = "/ex04")
    public String thymeleafExample04(Model model){

        List<ItemDto> itemDtoList = new ArrayList<>();

        for(int i=1;i<=10;i++){ //출력할 10가지의 itemDto 객체 10가지 생성. 후 itemDtoList에 넣어줌

            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명" +i);
            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(10000*i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);

        }
        model.addAttribute("itemDtoList",itemDtoList);
        return "thymeleafEx/thymeleafEx04";
    }
    @GetMapping(value = "/ex05")
    public String thymeleafExample05(){
        return "thymeleafEx/thymeleafEx05";
    }

    @GetMapping(value = "/ex06")
    public String thymeleafExample06(String param1, String param2, Model model){
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleafEx/thymeleafEx06";
    }
    @GetMapping(value = "/ex07")
    public String thymeleafExample07(){
        return "thymeleafEx/thymeleafEx07";
    }
}
