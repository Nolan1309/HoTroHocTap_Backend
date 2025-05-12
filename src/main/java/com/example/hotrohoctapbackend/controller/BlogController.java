package com.example.hotrohoctapbackend.controller;

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
import com.example.hotrohoctapbackend.DTO.CourseDTO;
import com.example.hotrohoctapbackend.config.ImageKitService;
import com.example.hotrohoctapbackend.dao.BlogRepository;
import com.example.hotrohoctapbackend.entity.Blog;
import com.example.hotrohoctapbackend.entity.GeneralDocument;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.BlogService;
import io.imagekit.sdk.exceptions.*;
import io.imagekit.sdk.models.results.Result;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/newest")
    public ResponseEntity<List<Blog>> getBlogsByNewest() {
        List<Blog> blogs = blogService.getBlogsByNewest();
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/{id}")
    public ApiResponse<BlogDTOPublic> getBlogByID(@PathVariable Integer id) {
        try {
            BlogDTOPublic blogDTO = blogService.getBlogDetailPublic(id);
            return new ApiResponse<>(200, "Success", blogDTO);
        } catch (EntityNotFoundException e) {
            return new ApiResponse<>(404, "Blog not found", null);
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<BlogDTO>> GetAllBlogByCategoryID(@PathVariable Integer categoryId, Pageable pageable) {
        Page<BlogDTO> blogPage = blogService.getBlogsByCategoryId(categoryId, pageable);
        if (blogPage.hasContent()) {
            return ResponseEntity.ok(blogPage);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/blogsall")
    public ResponseEntity<List<BlogDTO>> getAllBlogDTOs() {
        List<BlogDTO> blogDTOs = blogService.getAllBlogDTOs();
        return ResponseEntity.ok(blogDTOs);
    }

    @GetMapping("/admingetall")
    public ResponseEntity<Page<AdminBlogDTO_v2>> getPaginatedBlogs(
            @RequestParam(defaultValue = "0") int page,   // Default page is 0
            @RequestParam(defaultValue = "10") int size    // Default page size is 10
    ) {
        // Create a Pageable object with page number and size
        Pageable pageable = PageRequest.of(page, size);

        // Call the service method to fetch paginated data
        Page<AdminBlogDTO_v2> blogPage = blogService.getPaginatedBlogDetails(page, size);

        // Return the data in a ResponseEntity with HTTP status OK
        return ResponseEntity.ok(blogPage);
    }

    @GetMapping("/all-get-list-search")
    public ResponseEntity<Page<AdminBlogDTO_v2>> getPaginatedBlogsSearch(
            @RequestParam(required = false) Integer categoryId1,
            @RequestParam(required = false) Integer categoryId2,
            @RequestParam(required = false) Integer categoryId3,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,   // Default page is 0
            @RequestParam(defaultValue = "10") int size    // Default page size is 10
    ) {
        // Create a Pageable object with page number and size
        Pageable pageable = PageRequest.of(page, size);

        // Call the service method to fetch paginated data
        Page<AdminBlogDTO_v2> blogPage = blogService.getPaginatedBlogDetailsSearch(categoryId1, categoryId2, categoryId3, searchTerm, page, size);

        // Return the data in a ResponseEntity with HTTP status OK
        return ResponseEntity.ok(blogPage);
    }

    @GetMapping("/public")
    public ApiResponse<Page<BlogDTOPublic>> getBlogs(
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<BlogDTOPublic> blogs = blogService.getBlogsPublic(title, categoryId, page, size);
        return new ApiResponse<>(200, "Success", blogs);
    }

    @PutMapping("/{id}/views")
    public ApiResponse<BlogDTOPublic> increaseViewCount(@PathVariable int id) {
        try {

            BlogDTOPublic updatedBlog = blogService.increaseViewCount(id);
            return new ApiResponse<>(200, "Lượt xem của bài viết đã được tăng!", updatedBlog);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Có lỗi xảy ra khi tăng lượt xem", null);
        }
    }


    @GetMapping
    public Page<PostItem> searchBlogs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return blogService.searchBlogs(title, categoryId, status, fromDate, toDate, page, size);
    }

    @Autowired
    private ImageKitService imageKitService;

    @Autowired
    private BlogRepository blogRepository;

    @PostMapping("/admin/add")
    public ResponseEntity<ApiResponse<PostItem>> createBlog(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("summary") String summary,
            @RequestParam("author_id") String authorId,
            @RequestParam("createdAt") String createdAt,
            @RequestParam("updatedAt") String updatedAt,
            @RequestParam("status") boolean status,
            @RequestParam("featured") boolean featured,
            @RequestParam("cat_blog_id") String catBlogId,
            @RequestParam("image") MultipartFile image, // Nhận file hình ảnh
            @RequestParam("views") int views,
            @RequestParam("commentCount") int commentCount,
            @RequestParam("isDeleted") boolean isDeleted
    ) throws IOException, ForbiddenException, TooManyRequestsException, InternalServerException, UnauthorizedException, BadRequestException, UnknownException {

        try {
            // Upload the image and get the URL
            Result imageUrl = imageKitService.uploadFromBytes(image);

            // Create PostItem
            PostItem postItem = new PostItem();
            postItem.setTitle(title);
            postItem.setContent(content);
            postItem.setSummary(summary);
            postItem.setAuthor_id(authorId);
            postItem.setCreatedAt(createdAt);
            postItem.setUpdatedAt(updatedAt);
            postItem.setStatus(status);
            postItem.setFeatured(featured);
            postItem.setCat_blog_id(catBlogId);
            postItem.setImage(imageUrl.getUrl()); // Set the image URL

            postItem.setViews(views);
            postItem.setCommentCount(commentCount);
            postItem.setDeleted(isDeleted);

            // Add blog and get the result
            Blog createdBlog = blogService.addBlog(postItem);

            // Wrap the response in ApiResponse
            ApiResponse<PostItem> apiResponse = new ApiResponse<>(HttpStatus.CREATED.value(), "Blog created successfully", postItem);

            // Return the response
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (Exception ex) {
            // Handle exceptions and return a bad request response with the error message
            ApiResponse<PostItem> apiResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Error while creating blog: " + ex.getMessage(), null);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("admin/update/{id}")
    public ResponseEntity<ApiResponse<PostItem>> updateBlog(
            @PathVariable Integer id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("summary") String summary,
            @RequestParam("author_id") String authorId,
            @RequestParam("updatedAt") String updatedAt,
            @RequestParam("status") boolean status,
            @RequestParam("featured") boolean featured,
            @RequestParam("cat_blog_id") String catBlogId,
            @RequestParam(value = "image", required = false) MultipartFile image, // Nhận file hình ảnh
            @RequestParam("views") int views,
            @RequestParam("commentCount") int commentCount,
            @RequestParam("isDeleted") boolean isDeleted) {
        try {

            Blog existingBlog = blogRepository.findById(id).get();
            String imageUrlCheck = existingBlog.getImage();
            if (image != null && !image.isEmpty()) {
                Result imageUploadResult = imageKitService.uploadFromBytes(image);
                imageUrlCheck = imageUploadResult.getUrl(); // Update with new image URL
            }

            PostItem postItem = new PostItem();
            postItem.setTitle(title);
            postItem.setContent(content);
            postItem.setSummary(summary);
            postItem.setAuthor_id(authorId);
            postItem.setUpdatedAt(updatedAt);
            postItem.setStatus(status);
            postItem.setFeatured(featured);
            postItem.setCat_blog_id(catBlogId);
            postItem.setImage(imageUrlCheck);

            postItem.setViews(views);
            postItem.setCommentCount(commentCount);
            postItem.setDeleted(isDeleted);

            blogService.updatedBlog(id, postItem);
            ApiResponse<PostItem> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Blog updated successfully", postItem);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (IllegalArgumentException | InternalServerException | BadRequestException | UnknownException |
                 ForbiddenException | TooManyRequestsException | UnauthorizedException | IOException ex) {
            ApiResponse<PostItem> apiResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Error while updating blog", null);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("admin/detail/{id}")
    public ResponseEntity<AdminBlogGetOneDTO_V2> getBlogById(@PathVariable Integer id) {
        Optional<AdminBlogGetOneDTO_V2> blogDTO = blogService.getBlogByIdAdmin(id);

        if (blogDTO.isPresent()) {
            return ResponseEntity.ok(blogDTO.get());
        } else {
            return ResponseEntity.notFound().build();  // If no blog found, return 404
        }
    }

    @DeleteMapping("/hide")
    public ResponseEntity<String> deleteBlogs(@RequestBody List<Integer> ids) {
        boolean areDeleted = blogService.deleteBlogs(ids);

        if (areDeleted) {
            return ResponseEntity.ok("Các bài viết đã được xóa.");
        } else {
            return ResponseEntity.status(404).body("Một số bài viết không tồn tại.");
        }
    }


    @DeleteMapping("/restore/{id}")
    public ResponseEntity<String> ShowActiveBlog(@PathVariable int id) {
        boolean isDeleted = blogService.IsdeleteBlog(id);

        if (isDeleted) {
            return ResponseEntity.ok("Bài viết đã được khôi phục.");
        } else {
            return ResponseEntity.status(404).body("Bài viết không tồn tại.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        boolean isDeleted = blogService.deleteByIDVV(id);

        if (isDeleted) {
            return ResponseEntity.ok("Bài viết đã xoa thanh cong.");
        } else {
            return ResponseEntity.status(404).body("Bài viết không tồn tại.");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteBlogsByIds(@RequestBody List<Integer> blogIds) {
        boolean isDeleted = blogService.deleteBlogsByIds(blogIds);
        if (isDeleted) {
            return ResponseEntity.ok("Các bài viết đã được xóa thành công.");
        } else {
            return ResponseEntity.status(400).body("Có lỗi xảy ra khi xóa các bài viết.");
        }
    }

    @GetMapping("/restore/list-all-blogs")
    public Page<AdminBlogDTORestoreList> getBlogs(
            @RequestParam(required = false) Integer categoryId1,
            @RequestParam(required = false) Integer categoryId2,
            @RequestParam(required = false) Integer categoryId3,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String deletedDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (title.equals("")) {
            title = null;
        }
        if (deletedDate.equals("")) {
            deletedDate = null;
        }

        return blogService.getBlogs(categoryId1, categoryId2, categoryId3, title, deletedDate, page, size);
    }

    @PutMapping("/restore/{blogId}")
    public ResponseEntity<Blog> restoreBlog(@PathVariable Integer blogId) {
        AdminBlogDTORestoreList adminDocumentDTORestoreList = new AdminBlogDTORestoreList();
        adminDocumentDTORestoreList.setId(blogId);
        Blog restoredAccount = blogService.updateRestoreBlog(adminDocumentDTORestoreList);
        return ResponseEntity.ok(restoredAccount);
    }

    @DeleteMapping("/delete/{blogId}")
    public ResponseEntity<String> deleteBlog(@PathVariable Integer blogId) {
        AdminBlogDTORestoreList adminDocumentDTORestoreList = new AdminBlogDTORestoreList();
        adminDocumentDTORestoreList.setId(blogId);
        blogService.deleteRestoreBlog(adminDocumentDTORestoreList);
        return ResponseEntity.ok("Blog permanently deleted.");
    }


}
