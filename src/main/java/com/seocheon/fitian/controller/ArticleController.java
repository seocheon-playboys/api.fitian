package com.seocheon.fitian.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.seocheon.fitian.auth.annotation.AllowedRanks;
import com.seocheon.fitian.auth.annotation.CurrentUser;
import com.seocheon.fitian.auth.security.CustomUserDetails;
import com.seocheon.fitian.dto.ArticleListRequest;
import com.seocheon.fitian.dto.ArticleRequest;
import com.seocheon.fitian.dto.ArticleResponseDto;
import com.seocheon.fitian.dto.ArticleSummaryDto;
import com.seocheon.fitian.model.ApiResponse;
import com.seocheon.fitian.model.ArticleModel;
import com.seocheon.fitian.service.ArticleService;
import com.seocheon.fitian.service.S3Service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name="Article API", description="아티클 관련 기능 API")
@RestController
@RequestMapping("/article")
@SecurityRequirement(name="bearerAuth")
@Slf4j
public class ArticleController {

	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private S3Service s3Sv;
	
	//아티클 조희
	@Operation(summary = "특정 아티클 반환", description ="member/owner/manager 가 articleNo로 글 내용을 가져옵니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"member","owner","manager"})
	@GetMapping("/getArticle")
    public ResponseEntity<ApiResponse<ArticleResponseDto>> getArticle(
    		@Parameter(description="반환받고 싶은 아티클의 articleNo", required = true)
    		@RequestParam("articleNo") int articleNo,
    		@CurrentUser CustomUserDetails userDetails) {
		
		ArticleModel article = articleService.getArticle(articleNo);
		
		if(!article.getBoxCode().equals(userDetails.getMember().getBoxCode())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("다른 박스의 글은 볼 수 없습니다."));
		}
    	return ResponseEntity.ok(ApiResponse.success(ArticleResponseDto.from(article)));
    }
	
	//아티클 조희
		@Operation(summary = "박스 소개글 반환", description ="박스 소개글을 반환합니다.",
				security = @SecurityRequirement(name = "bearerAuth"))
		@AllowedRanks({"guest","member","owner","manager"})
		@GetMapping("/getIntroduction")
	    public ResponseEntity<ApiResponse<ArticleResponseDto>> getIntroduction(
	    		@CurrentUser CustomUserDetails userDetails) {
			
			ArticleModel article = articleService.getIntroduction(userDetails.getMember().getBoxCode());
			
			if(!article.getBoxCode().equals(userDetails.getMember().getBoxCode())) {
				return ResponseEntity.badRequest().body(ApiResponse.failure("다른 박스의 소개글은 볼 수 없습니다."));
			}
	    	return ResponseEntity.ok(ApiResponse.success(ArticleResponseDto.from(article)));
	    }
	
	//아티클 리스트 조회
	@Operation(summary = "아티클 리스트 조회", description = "boxCode 와 Category로 아티클 목록을 반환합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"guest","member","owner","manager"})
	@PostMapping("/getArticleList")
    public ResponseEntity<ApiResponse<List<ArticleSummaryDto>>> getArticleList(
    		@RequestBody ArticleListRequest request,
    		@CurrentUser CustomUserDetails userDetails) {
		
		if(!request.getBoxCode().equals(userDetails.getMember().getBoxCode())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("다른 박스의 글은 볼 수 없습니다."));
		}
		
		List<ArticleSummaryDto> articleList = articleService.getArticleList(request);
    	
		return ResponseEntity.ok(ApiResponse.success(articleList));
    }
	
	//아티클 생성
	@Operation(summary = "아티클 생성", description = "owner 또는 manager가 아티클을 생성합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PostMapping("/createArticleOLD")
    public ResponseEntity<ApiResponse<Void>> createArticle(
    		@RequestBody ArticleRequest request,
    		@CurrentUser CustomUserDetails userDetails) {
		
		if(!request.getBoxCode().equals(userDetails.getMember().getBoxCode()) || !request.getUid().equals(userDetails.getUsername())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("요청 내용의 uid 또는 boxCode가 다릅니다."));
		}
    	
    	try {
    		articleService.createArticle(request);
			return ResponseEntity.ok(ApiResponse.success(null, "아티클이 등록되었습니다.")) ;
		} catch (Exception ex) {
			log.error("아티클 등록 중 예기치 못한 오류 ", ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.failure("서버 오류로 아티클 등록에 실패했습니다."));
		}
    }
	
	//이미지 아티클 생성
	@Operation(summary = "이미지 아티클 생성", description = "owner 또는 manager가 이미지 아티클을 생성합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PostMapping(value = "/createArticle", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> createArticleWithImage(
    		@RequestPart(value = "ArticleModel") ArticleRequest request,
    		@RequestPart(value = "ArticleImages", required = false) MultipartFile[] images,
    		@CurrentUser CustomUserDetails userDetails) throws IOException {
		
		if(!request.getBoxCode().equals(userDetails.getMember().getBoxCode()) || !request.getUid().equals(userDetails.getUsername())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("요청 내용의 uid 또는 boxCode가 다릅니다."));
		}
		
		if(images == null) { //이미지가 없는 경우
			
			try {
	    		articleService.createArticle(request);
				return ResponseEntity.ok(ApiResponse.success(null, "아티클이 등록되었습니다.")) ;
			} catch (Exception ex) {
				log.error("아티클 등록 중 예기치 못한 오류 ", ex);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.failure("서버 오류로 아티클 등록에 실패했습니다."));
			}
			
		} else { //이미지가 있는경우
			
			//images 장수 제한
			if (images.length > 6) { //이미지가 6장 이상인 경우
				return ResponseEntity.badRequest().body(ApiResponse.failure("6장 이상의 사진은 업로드할 수 없습니다."));
	    	}
			
			//images.s3service -> 파일명 받아오기
    		List<String> ImageUrls = s3Sv.uploadFiles(images);
    		//model.set파일명
    		request.setArticleImgUrls(ImageUrls);
    		//model.createArticle
        	
        	try {
        		articleService.createArticleWithImage(request);
    			return ResponseEntity.ok(ApiResponse.success(null, "아티클이 등록되었습니다.")) ;
    		} catch (Exception ex) {
    			log.error("아티클 등록 중 예기치 못한 오류 ", ex);
    			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.failure("서버 오류로 아티클 등록에 실패했습니다."));
    		}
		}
		
    }
	
	//이미지 아티클 수정
	@Operation(summary = "이미지 아티클 수정", description = "owner 또는 manager가 이미지 아티클을 수정합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PutMapping(value = "/updateArticle", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> updateArticle(
    		@RequestPart(value = "ArticleModel") ArticleModel model,
    		@RequestPart(value = "ArticleImages", required = false) MultipartFile[] images,
    		@CurrentUser CustomUserDetails userDetails) throws IOException {
		
		if(!model.getBoxCode().equals(userDetails.getMember().getBoxCode()) || !model.getUid().equals(userDetails.getUsername())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("요청 내용의 uid 또는 boxCode가 다릅니다."));
		}
		
		String url = model.getArticleImgUrl();
		if(url != null && !url.trim().isEmpty()) {//url이 존재
			s3Sv.deleteFile(model.getArticleImgUrl()); // 이미지 삭제
			if(images != null && !images[0].isEmpty()) { // 이미지 존재
				List<String> ImageUrls = s3Sv.uploadFiles(images); // 업로드
				model.setArticleImgUrls(ImageUrls);
			} 
		}
		
		if(images != null && !images[0].isEmpty()) {
			List<String> ImageUrls = s3Sv.uploadFiles(images); // 업로드
			model.setArticleImgUrls(ImageUrls);
		}

		try {
			articleService.updateArticle(model);
			return ResponseEntity.ok(ApiResponse.success(null, "아티클이 수정되었습니다.")) ;
		} catch (Exception ex) {
			log.error("아티클 수정 중 예기치 못한 오류 ", ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.failure("서버 오류로 아티클 수정에 실패했습니다."));
		}
    	
    }
	
	//이미지 아티클 삭제
	@Operation(summary = "이미지 아티클 삭제", description = "owner 또는 manager가 이미지 아티클을 삭제합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PutMapping("/deleteArticle")
    public ResponseEntity<ApiResponse<Void>> deleteArticle(
    		@RequestBody ArticleModel model,
    		@CurrentUser CustomUserDetails userDetails) {
		
		if(!model.getBoxCode().equals(userDetails.getMember().getBoxCode()) || !model.getUid().equals(userDetails.getUsername())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("요청 내용의 uid 또는 boxCode가 다릅니다."));
		}

		List<String> articleImgUrls = articleService.getArticleimgUrls(model).getArticleModel().getArticleImgUrls();
		try {
			for(int i = 0; i < articleImgUrls.size(); i++) {
				s3Sv.deleteFile(articleImgUrls.get(i));
			}
			articleService.deleteArticle(model);
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.failure("서버 오류로 아티클 삭제에 실패했습니다."));
		}
		
		return ResponseEntity.ok(ApiResponse.success(null, "아티클이 삭제되었습니다.")) ;
    }
}
