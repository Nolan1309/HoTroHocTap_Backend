package com.example.hotrohoctapbackend.service;
import com.example.hotrohoctapbackend.dao.BlogRepository;
import com.example.hotrohoctapbackend.DTO.BlogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
@Service
public class BlogService {
    @Autowired
    private BlogRepository blogRepository;

    public List<BlogDTO> getAllBlogDTOs() {
        List<Object[]> results = blogRepository.findAllBlogsAsObjectArray();
        List<BlogDTO> blogDTOs = new ArrayList<>();

        for (Object[] result : results) {
            BlogDTO blogDTO = new BlogDTO();
            blogDTO.setId((Integer) result[0]);
            blogDTO.setTitle((String) result[1]);
            blogDTO.setAuthorFullName((String) result[2]);
            blogDTO.setCategory((String) result[3]);
            blogDTO.setStatus((String) result[4]);
            blogDTOs.add(blogDTO);
        }

        return blogDTOs;
    }
}
