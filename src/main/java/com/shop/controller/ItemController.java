package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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

    /**
     * 상품 수정 페이지 진입
     */
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){
        //@PathVariable : @controller에서 url에서 각 구분자에 들어오는 값 처리
        try{
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);   // 조회한 상품 데이터를 모델에 담아서 뷰로 전달
            model.addAttribute("itemFormDto", itemFormDto);
        }catch(EntityNotFoundException e){      //상품 엔티티가 존재하지 않을 경우 에러 메세지를 담아 상품 등록 페이지로 이동.
            model.addAttribute("errorMessage","존재하지 않는 상품 입니다. ");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }
    /**
     * 상품 수정 url 추가
     */
    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto,
                             BindingResult bindingResult, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMassage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
        }
        try{
            itemService.updateItem(itemFormDto, itemImgFileList);   //상품 수정 로직 호출.
        }catch(Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    /**
     * 상품 관리 화면 이동 및 조회한 상품 데이터를 화면에 전달하는 로직 구현
     */
    @GetMapping(value = {"/admin/items","/admin/items/{page}"}) //Url에 페이지가 있는 번호와 없는 번화 두가지 맵핑.
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() :0,3);
        //페이지 수와 가져올 데이터 수 설정, url 경로에 페이지 번호가 있으면 해당 페이지 번호 조회 없으면 0페이지 조회.
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);   //조회 조건과 페이징 정보를 파라미터로 넘겨 Page<Item>객체 받음
        model.addAttribute("items",items);  //조회한 상품 데이터 및 페이징 정보 뷰에 전달
        model.addAttribute("itemSearchDto", itemSearchDto); //페이지 전환시 기존 검색을 유지한 채로 이동할 수 있도록 뷰에 전달
        model.addAttribute("maxPage",5);    //상품 관리 메뉴 하단에 보여줄 페이지 번호 최대 개수

        return "item/itemMng";
    }
    /**
     * 상품 상세 페이지 이동
     */
    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl";
    }
}
