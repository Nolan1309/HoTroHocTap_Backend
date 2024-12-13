package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.DTO.CategoryDTO;
import com.example.hotrohoctapbackend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "categorys")
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    //Đóng
//    @Query(value = "SELECT * FROM categories WHERE level = :level", nativeQuery = true)
    @Query(value = "SELECT c.id_category, c.level, c.name, c.parent_id, c.deleted_date, c.is_deleted FROM categories c WHERE c.level = :level", nativeQuery = true)
    List<Object[]> findByLevel2(@Param("level") int level);

    //Đóng
//    @Query(value = "SELECT * FROM categories WHERE level = :level AND parent_id = :parentId", nativeQuery = true)
    @Query(value = "SELECT c.id_category, c.level, c.name, c.parent_id, c.deleted_date, c.is_deleted FROM categories c WHERE c.level = :level AND c.parent_id = :parentId", nativeQuery = true)
    List<Category> findCategoriesByLevelAndParentId(@Param("level") int level, @Param("parentId") long parentId);


    // Lấy tất cả các danh mục theo cấp bậc (level)
    List<Category> findByLevel(int level);

    // Lấy tất cả các danh mục con của một danh mục cha
    List<Category> findByCategory_Id(int parentId);

    //    @Query(value = "Select * from categories", nativeQuery = true)
    @Query(value = "SELECT c.id_category, c.level, c.name, c.parent_id, c.deleted_date, c.is_deleted FROM categories c", nativeQuery = true)
    List<Object[]> getAllCategory();

    //    @Query(value = "SELECT * FROM categories WHERE parent_id = :id_category", nativeQuery = true)
    @Query(value = "SELECT c.id_category, c.level, c.name, c.parent_id, c.deleted_date, c.is_deleted FROM categories c WHERE c.parent_id = :id_category", nativeQuery = true)
    List<Category> findByParentId(@Param("id_category") int id_category);

    //    @Query(value = "SELECT * FROM categories WHERE id_category = :id_category", nativeQuery = true)
    @Query(value = "SELECT c.id_category, c.level, c.name, c.parent_id, c.deleted_date, c.is_deleted FROM categories c WHERE c.id_category = :id_category", nativeQuery = true)
    List<Category> findCategoryNameByIdCategory(@Param("id_category") int id_category);

}
