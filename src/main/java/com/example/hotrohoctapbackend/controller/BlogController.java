package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.BlogDTO;
import com.example.hotrohoctapbackend.DTO.CourseDTO;
import com.example.hotrohoctapbackend.entity.Blog;
import com.example.hotrohoctapbackend.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
