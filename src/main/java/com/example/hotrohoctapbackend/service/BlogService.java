package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.Admin.AdminBlogAddDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminBlogDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminBlogGetOneDTO;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminBlogDTORestoreList;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminBlogDTO_v2;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminBlogGetOneDTO_V2;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminDocumentDTORestoreList;
import com.example.hotrohoctapbackend.DTO.AdminV3.Blog.BlogDTOPublic;
import com.example.hotrohoctapbackend.DTO.AdminV3.PostItem;
import com.example.hotrohoctapbackend.DTO.BlogDTO;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.BlogRepository;
import com.example.hotrohoctapbackend.dao.CategoryRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Blog;
import com.example.hotrohoctapbackend.entity.Category;
import com.example.hotrohoctapbackend.entity.GeneralDocument;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<Blog> getBlogsByNewest() {
        return blogRepository.findAllByStatusAndIsDeletedOrderByCreatedAtDesc(true, false, PageRequest.of(0, 3));
    }

    public BlogDTOPublic getBlogDetailPublic(Integer blogId) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new EntityNotFoundException("Blog not found with id: " + blogId));
        return convertToDtoPublic(blog);
    }

    public Page<BlogDTOPublic> getBlogsPublic(String title, Integer categoryId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Blog> blogs = blogRepository.findBlogsByTitleAndCategory(title, categoryId, pageable);
        return blogs.map(this::convertToDtoPublic);
    }

    public BlogDTOPublic increaseViewCount(int blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));

        blog.setViews(blog.getViews() + 1);
        return convertToDtoPublic(blogRepository.save(blog));
    }

    public Page<PostItem> searchBlogs(String title, Integer categoryId, Boolean status, LocalDateTime fromDate, LocalDateTime toDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Blog> blogPage = blogRepository.searchBlogs(title, categoryId, status, fromDate, toDate, pageable);

        return blogPage.map(this::convertToDto);
    }

    private PostItem convertToDto(Blog blog) {
        PostItem dto = new PostItem();
        dto.setId(String.valueOf(blog.getId()));
        dto.setTitle(blog.getTitle());
        dto.setContent(blog.getContent());
        dto.setSummary(blog.getSummary());
        dto.setAuthor_id(String.valueOf(blog.getAuthor().getId()));
        dto.setCreatedAt(blog.getCreatedAt().toString());
        dto.setUpdatedAt(blog.getUpdatedAt() != null ? blog.getUpdatedAt().toString() : null);
        dto.setStatus(blog.getStatus());
        dto.setFeatured(blog.getFeatured());
        dto.setCat_blog_id(blog.getCategory() != null ? String.valueOf(blog.getCategory().getId()) : null);
        dto.setImage(blog.getImage());
        dto.setViews(blog.getViews());
        dto.setCommentCount(blog.getCommentCount());
        dto.setDeleted(blog.isDeleted());
        dto.setDeletedDate(blog.getDeletedDate() != null ? blog.getDeletedDate().toString() : null);
        return dto;
    }

    private BlogDTOPublic convertToDtoPublic(Blog blog) {
        BlogDTOPublic dto = new BlogDTOPublic();
        dto.setId(String.valueOf(blog.getId()));
        dto.setTitle(blog.getTitle());
        dto.setContent(blog.getContent());
        dto.setSummary(blog.getSummary());
        dto.setAuthor_id(String.valueOf(blog.getAuthor().getId()));
        dto.setCreatedAt(blog.getCreatedAt().toString());
        dto.setUpdatedAt(blog.getUpdatedAt() != null ? blog.getUpdatedAt().toString() : null);
        dto.setStatus(blog.getStatus());
        dto.setFeatured(blog.getFeatured());
        dto.setCat_blog_id(blog.getCategory() != null ? String.valueOf(blog.getCategory().getId()) : null);
        dto.setImage(blog.getImage());
        dto.setViews(blog.getViews());
        dto.setCommentCount(blog.getCommentCount());
        dto.setDeleted(blog.isDeleted());
        dto.setDeletedDate(blog.getDeletedDate() != null ? blog.getDeletedDate().toString() : null);
        dto.setCategoryName(blog.getCategory().getName());
        dto.setAuthorName(blog.getAuthor().getFullname());
        return dto;
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


    private LocalDateTime convertTimestampToLocalDateTime(Object timestampObj) {
        if (timestampObj instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) timestampObj;
            return timestamp.toLocalDateTime();
        }
        return null;
    }

    public Optional<AdminBlogGetOneDTO_V2> getBlogByIdAdmin(Integer id) {
        // Lấy danh sách kết quả trả về từ câu truy vấn
        List<Object[]> blogData = blogRepository.findBlogByIdAdmin(id);

        // Kiểm tra nếu danh sách có kết quả trả về
        if (!blogData.isEmpty()) {
            // Lấy kết quả đầu tiên (vì chỉ có một blog)
            Object[] data = blogData.get(0); // data[0] là title, data[1] là content, v.v.

            // Chuyển đổi Object[] thành DTO
            AdminBlogGetOneDTO_V2 blogDTO = new AdminBlogGetOneDTO_V2(
                    (String) data[0],  // title
                    (String) data[1],  // content
                    (String) data[2],  // image
                    (Boolean) data[3], // status
                    (Integer) data[4],
                    (Integer) data[5],
                    (Integer) data[6]
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

    public Page<AdminBlogDTO_v2> getPaginatedBlogDetails(int page, int size) {
        // Tạo đối tượng Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Lấy kết quả phân trang từ repository
        Page<Object[]> resultPage = blogRepository.findBlogAdmin(pageable);

        // Ánh xạ kết quả từ truy vấn vào AdminBlogDTO_v2
        List<AdminBlogDTO_v2> blogDTOList = resultPage.getContent().stream().map(record -> new AdminBlogDTO_v2(
                ((Number) record[0]).longValue(), // blogId
                (String) record[1],               // title
                (String) record[2],               // content
                (String) record[3],               // authorName
                ((Number) record[4]).longValue(), // level3Id
                (String) record[5],               // categoryNameLevel3
                record[6] != null ? ((Number) record[6]).longValue() : null, // level2Id
                (String) record[7],               // categoryNameLevel2
                record[8] != null ? ((Number) record[8]).longValue() : null, // level1Id
                (String) record[9],               // categoryNameLevel1
                (Boolean) record[10],              // status
                (Boolean) record[11]              // isDeleted
        )).collect(Collectors.toList());

        // Trả về kết quả dưới dạng Page
        return new PageImpl<>(blogDTOList, pageable, resultPage.getTotalElements());
    }

    public Page<AdminBlogDTO_v2> getPaginatedBlogDetailsSearch(Integer categoryId1, Integer categoryId2, Integer categoryId3, String searchTerm, int page, int size) {
        // Tạo đối tượng Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Lấy kết quả phân trang từ repository
        Page<Object[]> resultPage = blogRepository.findBlogAdminSearch(categoryId1, categoryId2, categoryId3, searchTerm, pageable);

        // Ánh xạ kết quả từ truy vấn vào AdminBlogDTO_v2
        List<AdminBlogDTO_v2> blogDTOList = resultPage.getContent().stream().map(record -> new AdminBlogDTO_v2(
                ((Number) record[0]).longValue(), // blogId
                (String) record[1],               // title
                (String) record[2],               // content
                (String) record[3],               // authorName
                ((Number) record[4]).longValue(), // level3Id
                (String) record[5],               // categoryNameLevel3
                record[6] != null ? ((Number) record[6]).longValue() : null, // level2Id
                (String) record[7],               // categoryNameLevel2
                record[8] != null ? ((Number) record[8]).longValue() : null, // level1Id
                (String) record[9],               // categoryNameLevel1
                (Boolean) record[10],              // status
                (Boolean) record[11]              // isDeleted
        )).collect(Collectors.toList());

        // Trả về kết quả dưới dạng Page
        return new PageImpl<>(blogDTOList, pageable, resultPage.getTotalElements());
    }

    // Thêm bài viết mới
    public Blog addBlog(PostItem postItem) {
        Account author = accountRepository.findById(Integer.parseInt(postItem.getAuthor_id()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid author ID: " + postItem.getAuthor_id()));
        // Validate category ID
        Category categoryLevel3 = categoryRepository.findById(Integer.parseInt(postItem.getCat_blog_id()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + postItem.getCat_blog_id()));
        Blog blog = new Blog();
        blog.setTitle(postItem.getTitle());
        blog.setContent(postItem.getContent());
        blog.setSummary(postItem.getSummary());
        blog.setAuthor(author); // Chuyển đổi từ ID tác giả


        blog.setCreatedAt(LocalDateTime.now());

        blog.setUpdatedAt(LocalDateTime.now());

        blog.setStatus(postItem.getStatus());
        blog.setFeatured(postItem.getFeatured());
        blog.setCategory(categoryLevel3); // Chuyển đổi từ ID danh mục
        blog.setImage(postItem.getImage());

        blog.setViews(postItem.getViews());
        blog.setCommentCount(postItem.getCommentCount());
        blog.setDeleted(postItem.isDeleted());

        return blogRepository.save(blog); // Lưu bài viết mới vào DB
    }

    // Cập nhật bài viết
    public Blog updatedBlog(Integer id, PostItem postItem) {
        Account author = accountRepository.findById(Integer.parseInt(postItem.getAuthor_id()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid author ID: " + postItem.getAuthor_id()));
        Category categoryLevel3 = categoryRepository.findById(Integer.parseInt(postItem.getCat_blog_id()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + postItem.getCat_blog_id()));

        Blog blog = blogRepository.findById(id).orElseThrow(() -> new RuntimeException("Blog not found"));
        blog.setTitle(postItem.getTitle());
        blog.setContent(postItem.getContent());
        blog.setSummary(postItem.getSummary());
        blog.setAuthor(author);
        blog.setUpdatedAt(LocalDateTime.now());
        blog.setStatus(postItem.getStatus());
        blog.setFeatured(postItem.getFeatured());
        blog.setCategory(categoryLevel3);
        blog.setImage(postItem.getImage());
        blog.setDeleted(postItem.isDeleted());

        return blogRepository.save(blog); // Cập nhật bài viết vào DB
    }

    public boolean deleteBlogs(List<Integer> ids) {

        List<Blog> blogsToDelete = blogRepository.findAllById(ids);

        if (blogsToDelete.isEmpty()) {
            return false; // Nếu không tìm thấy bài viết nào trong danh sách
        }

        for (Blog blog : blogsToDelete) {
            blog.setDeleted(true); // Đánh dấu bài viết là không còn tồn tại (bị xóa)
            blog.setDeletedDate(LocalDateTime.now()); // Gán thời gian xóa
        }
        blogRepository.saveAll(blogsToDelete);

        return true;
    }


    public boolean IsdeleteBlog(int id) {
        Optional<Blog> blogOpt = blogRepository.findById(id);

        if (blogOpt.isPresent()) {
            Blog blog = blogOpt.get();
            blog.setStatus(false);
            blog.setDeletedDate(LocalDateTime.now());
            blogRepository.save(blog); // Cập nhật vào DB
            return true;
        }
        return false; // Nếu không tìm thấy bài viết
    }

    public boolean deleteByIDVV(int id) {
        Optional<Blog> blogOpt = blogRepository.findById(id);

        if (blogOpt.isPresent()) {
            Blog blog = blogOpt.get();
            blogRepository.deleteById(id);  // Xóa bài viết nếu chưa bị xóa
            return true;
        }
        return false;  // Nếu không tìm thấy bài viết
    }

    public boolean deleteBlogsByIds(List<Integer> blogIds) {
        try {
            List<Blog> blogs = blogRepository.findAllById(blogIds);
            for (Blog blog : blogs) {
                blogRepository.deleteById(blog.getId());
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Page<AdminBlogDTORestoreList> getBlogs(Integer categoryId1, Integer categoryId2, Integer categoryId3, String title, String deletedDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> resultPage = blogRepository.findBlogsBy(categoryId1, categoryId2, categoryId3, title, deletedDate, pageable);
        List<AdminBlogDTORestoreList> adminBlogDTORestoreLists = new ArrayList<>();
        for (Object[] result : resultPage) {
            AdminBlogDTORestoreList dto = new AdminBlogDTORestoreList();
            dto.setId((Integer) result[0]);
            dto.setContent((String) result[1]);
            LocalDateTime createAt = convertTimestampToLocalDateTime(result[2]);
            dto.setCreatedAt(createAt);
            LocalDateTime deleteAt = convertTimestampToLocalDateTime(result[3]);
            dto.setDeletedDate(deleteAt);


            dto.setImage((String) result[4]);
            dto.setIsDeleted((Boolean) result[5]);
            dto.setStatus((Boolean) result[6]);
            dto.setTitle((String) result[7]);
            LocalDateTime updateAt = convertTimestampToLocalDateTime(result[8]);
            dto.setUpdatedAt(updateAt);
            dto.setAuthorId((Integer) result[9]);

            dto.setCatBlogId((Integer) result[10]);
            adminBlogDTORestoreLists.add(dto);
        }
        return new PageImpl<>(adminBlogDTORestoreLists, pageable, resultPage.getTotalElements());
    }

    public Blog updateRestoreBlog(AdminBlogDTORestoreList adminBlogDTORestoreList) {
        Optional<Blog> accountOptional = blogRepository.findById(adminBlogDTORestoreList.getId());
        if (accountOptional.isEmpty()) {
            throw new RuntimeException("Document not found with id: " + adminBlogDTORestoreList.getId());
        } else {
            Blog blog = accountOptional.get();
            blog.setDeleted(false);
            return blogRepository.save(blog);
        }
    }

    public void deleteRestoreBlog(AdminBlogDTORestoreList adminBlogDTORestoreList) {
        Optional<Blog> generalDocument = blogRepository.findById(adminBlogDTORestoreList.getId());
        if (generalDocument.isEmpty()) {
            throw new RuntimeException("Account not found with id: " + adminBlogDTORestoreList.getId());
        } else {
            blogRepository.delete(generalDocument.get());
        }
    }
}
