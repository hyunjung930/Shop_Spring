package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        // 상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        //이미지 등록
        for(int i =0; i <itemImgFileList.size() ; i++){
            ItemImg itemImg = new ItemImg();   //상품 등록 폼으로 입력 받은 데이터를 이용해 item 객체 생성
            itemImg.setItem(item);  //상품 데이터 저장
            if(i == 0)  //첫번째 이미지일 경우 대표 상품 이미지 여부 값을 "Y"로 셋팅 나머지는 "N"로 설정.
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("N");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));    //상품의 이미지 정보 저장
        }
        return item.getId();
    }
}
