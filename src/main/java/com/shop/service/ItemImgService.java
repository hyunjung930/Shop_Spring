package com.shop.service;

import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {   //상품 이미지 업로드, 상품 이미지 정보를 저장

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            //바이트 배열을 파일 업로드 파라미터로 uploadFile 메소드 호출. 저장된 파일의 이름을 imgName 변수에 저장.
            imgUrl = "/images/item" + imgName;
            //저장한 상품 이미지를 불러올 경로 설정.
        }
        //상품 이미지 정보 저장
        /**
         * imgName: 실제 로컬에 저장된 상품 이미지 파일의 이름
         * oriImgName: 업로드했던 상품 이미지 파일의 원래 이름
         * imgUrl: 업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로
         */
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);

    }

    /**
     * 상품 이미지 데이터 수정할 때 변경 감지 기능.
     * @param itemImgId
     * @param itemImgFile
     * @throws Exception
     */
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
        if(!itemImgFile.isEmpty()){ // 기존 이미지 삭제할 경우 이미지 업데이트
            ItemImg savedITemImg = itemImgRepository.findById(itemImgId)    //상품이미지 아이디를 이용해 기존에 저장했던 상품 이미지 엔티티 조회
                    .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedITemImg.getImgName())){    //기존에 등록된 상품이미지 파일이 있을 경우 해당 파일 삭제
                fileService.deleteFile(itemImgLocation + "/" + savedITemImg.getImgName());
            }

            String oriImgName =  itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            //업데이트한 상품 이미지 파일 업로드
            String imgUrl = "/images/item/" + imgName;

            savedITemImg.updateItemImg(oriImgName, imgName, imgUrl);
            //변경된 상품이미지 셋팅. itemImgRepository.save()로직 호출 x, 엔티티 영속 상태임으로 트랜잭션이 끝날 때 update 문 실행..
        }


    }

}
