package com.daily.domain.contents.application;

import com.daily.domain.contents.domain.Contents;
import com.daily.domain.contents.repository.ContentsRepository;
import com.daily.domain.contents.dto.ContentsResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ContentsServiceImpl implements ContentsService {

    private final ContentsRepository contentsRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ContentsResponse> fetchContents() {
        List<Contents> contentsList = contentsRepository.findAll();

        return contentsList.stream()
                .map(content -> ContentsResponse.builder()
                        .contentId(content.getContentsId())
                        .title(content.getTitle())
                        .author(content.getAuthor())
                        .link(content.getLink())
                        .description(content.getDescription())
                        .regDtm(content.getRegDtm())
                        .companyCd(content.getCompanyCd())
                        .contentsType(content.getContentType())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ContentsResponse fetchContentsId(Long id) {
        Optional<Contents> contentsOptional = contentsRepository.findById(id);

        if (contentsOptional.isPresent()) {
            Contents content = contentsOptional.get();
            return ContentsResponse.builder()
                        .contentId(content.getContentsId())
                        .title(content.getTitle())
                        .author(content.getAuthor())
                        .link(content.getLink())
                        .description(content.getDescription())
                        .regDtm(content.getRegDtm())
                        .companyCd(content.getCompanyCd())
                        .contentsType(content.getContentType())
                        .build();
        }
        return null;
    }

    @Override
    public void update() {}
}
