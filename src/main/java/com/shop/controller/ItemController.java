package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController { //상품등록페이지 접근할 수 있게 Controller 생성

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "/item/itemForm";
    }

    //상품을 등록하는 url
    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList){

        if(bindingResult.hasErrors()){
            return "item/itemForm"; // 상품 등록시 필수 값이 없으면 다시 상품 등록 페이지로 전환
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            //상품 등록시 첫번쨰 이미지가 없으면 에러메세지와 함께 상품등록 페이지로 이동, 상품의 첫번쨰 이미지는 필수값으로 지정.
            model.addAttribute("errorMessage", "첫번쨰 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        try{
            itemService.saveItem(itemFormDto, itemImgFileList);
            //상품 저장 로직 불러옴. 매개 변수로 상품 정보와 상품 이미지 정보를 담고 있는 itemImgFileList 넘겨줌
        }catch(Exception e){
            model.addAttribute("errorMessage", " 상품등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";   // 상품이 제대로 등록시 메인페이지로 이동.
    }
}
