package com.seocheon.fitian.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seocheon.fitian.dto.classes.SearchClassDTO;
import com.seocheon.fitian.mapper.ClassMapper;
import com.seocheon.fitian.model.ClassModel;
import com.seocheon.fitian.model.ClassParticipantModel;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassService {
	
	private final ClassMapper classMapper;
	private final SqlSessionFactory sqlSessionFactory;
	
	@Transactional
	public void createClass(String boxCode, List<ClassModel> requestList) {
		
		//boxCode 설정
		for(ClassModel r : requestList) {
			r.setBoxCode(boxCode);
		}
		
		if(requestList.get(0).getStartTime() != null && !requestList.get(0).getStartTime().isEmpty()) {
			
			batchQuery(requestList);
			
		} else {
			SearchClassDTO dto = SearchClassDTO.builder()
				    .boxCode(boxCode)
				    .startDate(LocalDate.parse("2020-06-01"))
				    .endDate(LocalDate.parse("2020-06-07"))
				    .build();
			
			List<ClassModel> classTemplate = classMapper.findByClassDates(dto);
			List<ClassModel> resultList = new ArrayList<>();
			
			for(ClassModel r : requestList) { //전체 날짜중 하루씩 루프
				
				LocalDate today = r.getClassDate();
				//request 요일 int 값 가져오기
				int dayValue = r.getClassDate().getDayOfWeek().getValue();
				
				//classTemplate에서 dayValue와 같은 요일 가져오기
				List<ClassModel> dayClasses = classTemplate.stream()
					    .filter(c -> c.getClassDate().getDayOfWeek().getValue() == dayValue )
					    .collect(Collectors.toList());
				
				for(ClassModel dayClass : dayClasses) {//해당 날짜의 수업 루프
					dayClass.setClassDate(today);
				}
				resultList.addAll(dayClasses);
			}
			
			batchQuery(resultList);
			
		}
		
	}
	
	public List<ClassModel> getClasses(SearchClassDTO dto) {
		return classMapper.findByClassDates(dto);
	}
	
	private void batchQuery(List<ClassModel> requestList) {
		
		try (SqlSession batchSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
			ClassMapper mapper = batchSession.getMapper(ClassMapper.class);

            for (ClassModel r : requestList) {
                mapper.createClass(r); // 여러 번 호출해 쿼리 쌓기
            }

            batchSession.flushStatements(); // 쌓인 쿼리를 한 번에 실행
            batchSession.commit();          // 트랜잭션 커밋
        } catch (Exception e) {
            // 필요시 롤백 처리 (try-with-resources이므로 자동 close)
            throw e;
        }
		
	}
	
	public List<ClassParticipantModel> findMembersByClassNo(int classNo) {
		return classMapper.findMembersByClassNo(classNo);
	}
	
	public void joinClass(String boxCode, ClassParticipantModel model) {
		if(!classMapper.findByClassNo(model.getClassNo()).getBoxCode().equals(boxCode)) {
			throw new IllegalArgumentException("다른 박스의 클래스는 신청할 수 없습니다.");
		}
		
		if(classMapper.findByClassNoAndUid(model)) {
	        throw new IllegalStateException("이미 참여한 수업입니다.");
	    }
		classMapper.joinClass(model);
	}
	
	public void cancelClass(String boxCode, ClassParticipantModel model) {
	    // 1. 클래스가 해당 박스에 소속된 것인지 검증
	    if(!classMapper.findByClassNo(model.getClassNo()).getBoxCode().equals(boxCode)) {
	        throw new IllegalArgumentException("다른 박스의 클래스는 취소할 수 없습니다.");
	    }

	    // 2. 참여내역이 없는 경우 예외 처리
	    if(!classMapper.findByClassNoAndUid(model)) {
	        throw new IllegalStateException("참여하지 않은 수업은 취소할 수 없습니다.");
	    }

	    // 3. 실제 취소(삭제) 처리
	    classMapper.cancelClass(model);
	}
	
	public void updateClass(String boxCode, ClassModel model) {
		model.setBoxCode(boxCode);
		classMapper.updateClass(model);
	}

	public void deleteClass(String boxCode, ClassModel model) {
		model.setBoxCode(boxCode);
		classMapper.deleteClass(model);
	}
}
