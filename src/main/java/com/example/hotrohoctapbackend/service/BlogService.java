package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.BlogDTO;
import com.example.hotrohoctapbackend.dao.BlogRepository;
import com.example.hotrohoctapbackend.entity.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    public List<Blog> getBlogsByNewest() {
        return blogRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, 3));
    }

    public Page<BlogDTO> getAllBlogs(Pageable pageable) {
        Page<Object[]> blogObjects = blogRepository.findAllBlogs(pageable);

        // Mapping Object[] to BlogDTO
        return blogObjects.map(obj -> new BlogDTO(
                (Integer) obj[0],  // id
                (String) obj[1],   // content
                convertTimestampToLocalDateTime(obj[2]),  // created_at
                (String) obj[3],   // title
                convertTimestampToLocalDateTime(obj[4]), // updated_at
                (Integer) obj[5],  // author_id
                (Integer) obj[6],  // cat_blog_id
                (Boolean) obj[7],   // status
                (String) obj[8], //image
                (String) obj[9], //authorName
                (String) obj[10] //categoryName
        ));
    }
    public Page<BlogDTO> getBlogsByCategoryId(Integer categoryId, Pageable pageable) {
        Page<Object[]> blogObjects = blogRepository.findByCategoryIdWithPagination(categoryId, pageable);
        // Mapping Object[] to BlogDTO
        return blogObjects.map(obj -> new BlogDTO(
                (Integer) obj[0],  // id
                (String) obj[1],   // content
                convertTimestampToLocalDateTime(obj[2]),  // created_at
                (String) obj[3],   // title
                convertTimestampToLocalDateTime(obj[4]), // updated_at
                (Integer) obj[5],  // author_id
                (Integer) obj[6],  // cat_blog_id
                (Boolean) obj[7],   // status
                (String) obj[8], //image
                (String) obj[9], //authorName
                (String) obj[10] //categoryName
        ));
    }
    public BlogDTO getBlogByID(Integer id) {
        List<Object[]> blogObjects = blogRepository.getBlog(id);

        if (!blogObjects.isEmpty()) {
            Object[] blogData = blogObjects.get(0); // Lấy phần tử đầu tiên (chỉ có 1 kết quả)

            BlogDTO dto = new BlogDTO();
            dto.setId((Integer) blogData[0]);  // id
            dto.setContent((String) blogData[1]);  // content
            dto.setCreated_at(convertTimestampToLocalDateTime(blogData[2]));  // created_at
            dto.setTitle((String) blogData[3]);  // title
            dto.setUpdated_at(convertTimestampToLocalDateTime(blogData[4]));  // updated_at
            dto.setAuthor_id((Integer) blogData[5]);  // author_id
            dto.setCat_blog_id((Integer) blogData[6]);  // cat_blog_id
            dto.setStatus((Boolean) blogData[7]);  // status
            dto.setImage((String) blogData[8]);  // image
            dto.setAuthor_name((String) blogData[9]);  // author_name
            dto.setCategory_name((String) blogData[10]);  // category_name

            return dto;
        } else {
            return null;  // Trả về null nếu không tìm thấy kết quả
        }
    }

    private LocalDateTime convertTimestampToLocalDateTime(Object timestampObj) {
        if (timestampObj instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) timestampObj;
            return timestamp.toLocalDateTime();
        }
        return null;
    }




}
