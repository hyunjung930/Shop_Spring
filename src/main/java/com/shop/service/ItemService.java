package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    //상품 등록
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

    //등록된 상품 불러오기
    @Transactional(readOnly = true) // 상품데이터를 읽는 트랜잭션을 읽기 전용으로 설정. jpa가 더디체킹(변경감지)을 수행하지 않아 성능 향상
    public ItemFormDto getItemDtl(Long itemId){

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId); //해당 상품 이미지 조회. 상품 이미지 아이디를 오름 차순으로 가져옴
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for(ItemImg itemImg : itemImgList){ //조회한 ItemImg 엔티티를 ItemImgDto 객체로 만들어 리스트 추가
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        // 상품의 아이디를 통해 상품 엔티티를 조회, 존재하지 않을 때 EntityNotFoundException 발생
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }


}
