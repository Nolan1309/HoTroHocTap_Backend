package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.AdminBlogAddDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminBlogDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminBlogGetOneDTO;
import com.example.hotrohoctapbackend.DTO.BlogDTO;
import com.example.hotrohoctapbackend.DTO.CourseDTO;
import com.example.hotrohoctapbackend.entity.Blog;
import com.example.hotrohoctapbackend.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
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

    @GetMapping()
    public ResponseEntity<Page<BlogDTO>> getAllBlog(Pageable pageable) {
        Page<BlogDTO> blogPage = blogService.getAllBlogs(pageable);
        if (blogPage.hasContent()) {
            return ResponseEntity.ok(blogPage);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogDTO> getBlogByID(@PathVariable Integer id) {
        BlogDTO blogDTO = blogService.getBlogByID(id);
        if (blogDTO != null) {
            return ResponseEntity.ok(blogDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<BlogDTO>> GetAllBlogByCategoryID(@PathVariable Integer categoryId,Pageable pageable) {
        Page<BlogDTO> blogPage = blogService.getBlogsByCategoryId(categoryId,pageable);
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
    public ResponseEntity<Page<AdminBlogDTO>> getPaginatedBlogs(
            @RequestParam(defaultValue = "0") int page,   // Default page is 0
            @RequestParam(defaultValue = "10") int size    // Default page size is 10
    ) {
        // Create a Pageable object with page number and size
        Pageable pageable = PageRequest.of(page, size);

        // Call the service method to fetch paginated data
        Page<AdminBlogDTO> blogPage = blogService.getPaginatedBlogDetails(page, size);

        // Return the data in a ResponseEntity with HTTP status OK
        return ResponseEntity.ok(blogPage);
    }
    @PostMapping("/admin/add")
    public ResponseEntity<Blog> addBlog(@RequestBody AdminBlogAddDTO blogAddDTO) {
        Blog blog = blogService.addBlogAdmin(blogAddDTO);
        return ResponseEntity.ok(blog);
    }
    @PutMapping("admin/update/{blogId}")
    public ResponseEntity<Blog> updateBlog(
            @PathVariable("blogId") int blogId,
            @RequestBody AdminBlogAddDTO blogAddDTO) {
        try {
            Blog updatedBlog = blogService.updateBlogAdmin(blogId, blogAddDTO);
            return new ResponseEntity<>(updatedBlog, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("admin/detail/{id}")
    public ResponseEntity<AdminBlogGetOneDTO> getBlogById(@PathVariable Integer id) {
        Optional<AdminBlogGetOneDTO> blogDTO = blogService.getBlogByIdAdmin(id);

        if (blogDTO.isPresent()) {
            return ResponseEntity.ok(blogDTO.get());
        } else {
            return ResponseEntity.notFound().build();  // If no blog found, return 404
        }
    }
}
