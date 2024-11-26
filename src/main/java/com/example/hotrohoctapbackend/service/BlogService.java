package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.Admin.AdminBlogAddDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminBlogDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminBlogGetOneDTO;
import com.example.hotrohoctapbackend.DTO.BlogDTO;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.BlogCategoryRepository;
import com.example.hotrohoctapbackend.dao.BlogRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Blog;
import com.example.hotrohoctapbackend.entity.BlogCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private BlogCategoryRepository blogCategoryRepository;

    @Autowired
    private AccountRepository accountRepository;
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
    public Optional<AdminBlogGetOneDTO> getBlogByIdAdmin(Integer id) {
        // Lấy danh sách kết quả trả về từ câu truy vấn
        List<Object[]> blogData = blogRepository.findBlogByIdAdmin(id);

        // Kiểm tra nếu danh sách có kết quả trả về
        if (!blogData.isEmpty()) {
            // Lấy kết quả đầu tiên (vì chỉ có một blog)
            Object[] data = blogData.get(0); // data[0] là title, data[1] là content, v.v.

            // Chuyển đổi Object[] thành DTO
            AdminBlogGetOneDTO blogDTO = new AdminBlogGetOneDTO(
                    (String) data[0],  // title
                    (String) data[1],  // content
                    (String) data[2],  // image
                    (Boolean) data[3], // status
                    (Integer) data[4]  // cat_blog_id
            );

            // Trả về Optional chứa DTO
            return Optional.of(blogDTO);
        } else {
            // Nếu không có dữ liệu, trả về Optional.empty
            return Optional.empty();
        }
    }




    public List<BlogDTO> getAllBlogDTOs() {
        List<Object[]> results = blogRepository.findAllBlogsAsObjectArray();
        List<BlogDTO> blogDTOs = new ArrayList<>();

        for (Object[] result : results) {
            BlogDTO blogDTO = new BlogDTO();
            blogDTO.setId((Integer) result[0]);
            blogDTO.setTitle((String) result[1]);
            blogDTO.setAuthor_name((String) result[2]);
            blogDTO.setCategory_name((String) result[3]);
            blogDTO.setStatus((Boolean) result[4]);
            blogDTOs.add(blogDTO);
        }

        return blogDTOs;
    }
    public Page<AdminBlogDTO> getPaginatedBlogDetails(int page, int size) {
        // Create Pageable object (page number starts from 0, size is the number of records per page)
        Pageable pageable = PageRequest.of(page, size);

        // Fetch the paginated results
        Page<Object[]> resultPage = blogRepository.findBlogAdmin(pageable);

        // Map the results to AdminBlogDTO
        List<AdminBlogDTO> blogDTOList = resultPage.getContent().stream().map(record -> new AdminBlogDTO(
                (Integer) record[0],     // id
                (String) record[1],      // title
                (String) record[2],      // fullname
                (String) record[3],      // categoryName
                (Boolean) record[4],     // status
                (Boolean) record[5]      // isDeleted
        )).collect(Collectors.toList());
        // Return as a Page of AdminBlogDTO
        return new PageImpl<>(blogDTOList, pageable, resultPage.getTotalElements());
    }
    public Blog addBlogAdmin(AdminBlogAddDTO blogAddDTO) {
        // Validate category ID
        BlogCategory category = blogCategoryRepository.findById(blogAddDTO.getCat_id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + blogAddDTO.getCat_id()));

        // Validate author name
        Account author = accountRepository.findById(blogAddDTO.getAuthor_id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid author name: " + blogAddDTO.getAuthor_id()));

        // Create a new Blog entity
        Blog blog = new Blog();
        blog.setTitle(blogAddDTO.getTitle());
        blog.setContent(blogAddDTO.getContent());
        blog.setStatus(blogAddDTO.getStatus());
        blog.setImage(blogAddDTO.getImage());
        blog.setCreatedAt(LocalDateTime.now());
        blog.setUpdatedAt(LocalDateTime.now());
        blog.setCategory(category);
        blog.setAuthor(author);

        // Save the blog to the repository
        return blogRepository.save(blog);
    }
    public Blog updateBlogAdmin(int blogId, AdminBlogAddDTO blogAddDTO) {
        // Validate category ID
        BlogCategory category = blogCategoryRepository.findById(blogAddDTO.getCat_id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + blogAddDTO.getCat_id()));

        // Validate author ID
        Account author = accountRepository.findById(blogAddDTO.getAuthor_id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid author ID: " + blogAddDTO.getAuthor_id()));

        // Find the existing blog by ID
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found with ID: " + blogId));

        // Update the fields
        blog.setTitle(blogAddDTO.getTitle());
        blog.setContent(blogAddDTO.getContent());
        blog.setStatus(blogAddDTO.getStatus());
        blog.setImage(blogAddDTO.getImage());
        blog.setUpdatedAt(LocalDateTime.now()); // Update the timestamp for last modified

        // Update the category and author (if necessary)
        blog.setCategory(category);
        blog.setAuthor(author);

        // Save the updated blog to the repository
        return blogRepository.save(blog);
    }
}
