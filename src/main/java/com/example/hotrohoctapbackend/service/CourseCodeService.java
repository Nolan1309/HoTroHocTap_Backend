package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.AdminV2.CourseCodeRequestDTO;
import com.example.hotrohoctapbackend.DTO.AdminV2.CourseCodeResponseDTO;
import com.example.hotrohoctapbackend.DTO.AdminV2.CourseCodeStatusResponse;
import com.example.hotrohoctapbackend.DTO.AdminV2.StudentCourseDataDTO;
import com.example.hotrohoctapbackend.DTO.User.CourseCodeActivationRequestDTO;
import com.example.hotrohoctapbackend.DTO.User.StudentBehaviorRequestDTO;
import com.example.hotrohoctapbackend.dao.*;
import com.example.hotrohoctapbackend.entity.*;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.services.EmailService;
import com.google.cloud.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CourseCodeService {
    @Autowired
    private CourseCodeRepository courseCodeRepository;

    @Autowired
    private StudentCourseDataRepository studentCourseDataRepository;

    @Autowired
    private Enrolled_CoursesRepository enrolledCoursesRepository;

    @Autowired
    private StudentCourseDataService studentCourseDataService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleUserRepository roleUserRepository;

    @Autowired
    private EmailService emailService;

    // Phương thức tạo mã khóa học , tạo theo so luong ( chua gan account )
    public List<CourseCodeResponseDTO> createCourseCodes(int quantity, int courseId, Integer accountId, int expiryDays) {
        // Lấy khóa học từ courseId
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));
        List<CourseCode> courseCodes = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            CourseCode courseCode = new CourseCode();
            courseCode.setCourse(course);
            courseCode.setAccount(null);
            courseCode.setCode(generateRandomCode());
            courseCode.setStatus(false);
            courseCode.setExpiryDate(LocalDateTime.now().plus(expiryDays, ChronoUnit.DAYS));
            courseCode.setCreatedAt(LocalDateTime.now());
            courseCodes.add(courseCode);
        }
        List<CourseCode> savedCourseCodes = courseCodeRepository.saveAll(courseCodes);
        return savedCourseCodes.stream()
                .map(courseCode -> new CourseCodeResponseDTO(
                        courseCode.getId(),
                        courseCode.getCode(),
                        courseCode.getCreatedAt(),
                        courseCode.getUsedAt(),
                        courseCode.getStatus(),
                        courseCode.getExpiryDate(),
                        courseCode.getAccount() != null ? courseCode.getAccount().getId() : null,
                        courseCode.getCourse().getId()
                )).collect(Collectors.toList());
    }

    // Phương thức tạo mã ngẫu nhiên
    private String generateRandomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {  // Mã dài 8 ký tự
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }
        return code.toString();
    }

    public Page<CourseCodeResponseDTO> getCourseCodes(Integer courseId, int page, int size, String codeSearch) {
        Pageable pageable = PageRequest.of(page, size); // Phân trang: page, size
        Page<CourseCode> courseCodesPage = courseCodeRepository.findByCodeContaining(courseId, codeSearch, pageable);

        // Chuyển từ Page<CourseCode> sang List<CourseCodeResponseDTO>
        return courseCodesPage.map(courseCode -> new CourseCodeResponseDTO(
                courseCode.getId(),
                courseCode.getCode(),
                courseCode.getCreatedAt(),
                courseCode.getUsedAt(),
                courseCode.getStatus(),
                courseCode.getExpiryDate(),
                courseCode.getAccount() != null ? courseCode.getAccount().getId() : null,
                courseCode.getCourse().getId()
        ));
    }

    public void deleteCourseCode(int id) {
        Optional<CourseCode> courseCode = courseCodeRepository.findById(id);
        if (courseCode.isPresent()) {
            courseCodeRepository.delete(courseCode.get());  // Delete the course code by ID
        } else {
            throw new RuntimeException("CourseCode not found with id: " + id);
        }
    }

    public String activateCourseCode(StudentBehaviorRequestDTO studentBehaviorRequestDTO, String code, Integer accountId) {

        StudentCourseData studentCourseData = studentCourseDataRepository.findStudentCourseDataByEmail(studentBehaviorRequestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Không phải sinh viên HUIT"));

        // Step 1: Find the course code by the provided code
        CourseCode courseCode = courseCodeRepository.findCourseCodeByCode(code)
                .orElseThrow(() -> new RuntimeException("Mã khóa học không tồn tại"));

        // Step 2: Check if the course code is already used
        if (courseCode.getStatus()) {
            throw new RuntimeException("Mã khóa học đã được kích hoạt");
        }

        // Step 3: Check if the course code has expired
        if (courseCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Mã khóa học đã hết hạn");
        }
        Optional<Enrolled_Courses> enrolledCoursesCheck = enrolledCoursesRepository
                .findByAccountIdAndCourseId(accountId, courseCode.getCourse().getId());  // Assuming you have a method like this

        if (enrolledCoursesCheck.isPresent()) {
            throw new RuntimeException("Tài khoản đã đăng ký khóa học này.");
        }

        // Step 5: Find the account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        StudentCourseData studentCourseData1 = studentCourseDataService.saveStudentCourseDataActive(studentBehaviorRequestDTO, studentCourseData, accountId);


        // Step 6: Find the course from the course code
        Course course = courseCode.getCourse();

        // Step 7: Create a new EnrolledCourses entry to register the user
        Enrolled_Courses enrolledCourses = new Enrolled_Courses();
        enrolledCourses.setAccount(account);
        enrolledCourses.setCourse(course);
        enrolledCourses.setEnrollmentDate(LocalDateTime.now());
        enrolledCourses.setStatus("Actived");

        Account account1 = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
        RoleUser roleUser = roleUserRepository.findByRoleName("HUITSTUDENT");

        account1.setRole(roleUser);

        accountRepository.saveAndFlush(account1);
        // Save the enrollment to the repository
        enrolledCoursesRepository.save(enrolledCourses);

        // Step 8: Update the CourseCode status to mark it as used
        courseCode.setStatus(true);  // Mark as activated
        courseCode.setUsedAt(LocalDateTime.now());  // Set the date when the code was used
        courseCode.setAccount(account);  // Assign the account to the code

        // Save the updated course code
        courseCodeRepository.save(courseCode);

        // Step 9: Return success message
        return "Khóa học đã được kích hoạt và bạn đã đăng ký thành công!";
    }

    public String activateCourseCodeNotHuit(String code, Integer accountId) {

        // Step 1: Find the course code by the provided code
        CourseCode courseCode = courseCodeRepository.findCourseCodeByCode(code)
                .orElseThrow(() -> new RuntimeException("Mã khóa học không tồn tại"));

        // Step 2: Check if the course code is already used
        if (courseCode.getStatus()) {
            throw new RuntimeException("Mã khóa học đã được kích hoạt");
        }

        // Step 3: Check if the course code has expired
        if (courseCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Mã khóa học đã hết hạn");
        }
        Optional<Enrolled_Courses> enrolledCoursesCheck = enrolledCoursesRepository
                .findByAccountIdAndCourseId(accountId, courseCode.getCourse().getId());  // Assuming you have a method like this

        if (enrolledCoursesCheck.isPresent()) {
            throw new RuntimeException("Tài khoản đã đăng ký khóa học này.");
        }

        // Step 5: Find the account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        // Step 6: Find the course from the course code
        Course course = courseCode.getCourse();

        // Step 7: Create a new EnrolledCourses entry to register the user
        Enrolled_Courses enrolledCourses = new Enrolled_Courses();
        enrolledCourses.setAccount(account);
        enrolledCourses.setCourse(course);
        enrolledCourses.setEnrollmentDate(LocalDateTime.now());
        enrolledCourses.setStatus("Actived");

        enrolledCoursesRepository.save(enrolledCourses);
        courseCode.setStatus(true);  // Mark as activated
        courseCode.setUsedAt(LocalDateTime.now());  // Set the date when the code was used
        courseCode.setAccount(account);  // Assign the account to the code
        courseCodeRepository.save(courseCode);
        return "Khóa học đã được kích hoạt và bạn đã đăng ký thành công!";
    }

    public CourseCodeStatusResponse checkCourseCode(String code) {
        // Step 1: Find the course code by the provided code
        CourseCode courseCode = courseCodeRepository.findCourseCodeByCode(code)
                .orElseThrow(() -> new RuntimeException("Mã khóa học không tồn tại"));

        // Step 2: Check if the course code is already used
        if (courseCode.getStatus()) {
            throw new RuntimeException("Mã khóa học đã được kích hoạt");
        }

        // Step 3: Check if the course code has expired
        if (courseCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Mã khóa học đã hết hạn");
        }

        // Return the status of the course code
        return new CourseCodeStatusResponse(true, "Mã khóa học hợp lệ và chưa được kích hoạt");
    }


    public ApiResponse<CourseCodeResponseDTO> updateCourseCode(int id, CourseCodeRequestDTO courseCodeRequestDTO) {
        // Tìm kiếm CourseCode theo id
        CourseCode existingCourseCode = courseCodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course code not found"));

        // Kiểm tra ngày hết hạn phải lớn hơn ngày hiện tại ít nhất 1 ngày
        if (courseCodeRequestDTO.getExpiryDate() != null) {
            LocalDateTime currentDate = LocalDateTime.now();
            LocalDateTime expiryDate = courseCodeRequestDTO.getExpiryDate();

            // Kiểm tra ngày hết hạn phải lớn hơn ngày hiện tại ít nhất 1 ngày
            if (expiryDate.isBefore(currentDate.plusDays(1))) {
                return new ApiResponse<>(400, "Ngày hết hạn phải lớn hơn ngày hiện tại ít nhất 1 ngày", null);
            }

            // Cập nhật ngày hết hạn
            existingCourseCode.setExpiryDate(expiryDate);
        }

        // Cập nhật các trường cần thiết
        if (courseCodeRequestDTO.getStatus() != null) {
            existingCourseCode.setStatus(courseCodeRequestDTO.getStatus());
        }

        // Cập nhật thời gian sửa đổi nếu cần
        existingCourseCode.setUsedAt(LocalDateTime.now());

        // Lưu cập nhật vào cơ sở dữ liệu
        CourseCode savedCourseCode = courseCodeRepository.save(existingCourseCode);

        // Chuyển đổi từ CourseCode sang CourseCodeResponseDTO
        CourseCodeResponseDTO responseDTO = new CourseCodeResponseDTO(
                savedCourseCode.getId(),
                savedCourseCode.getCode(),
                savedCourseCode.getCreatedAt(),
                savedCourseCode.getUsedAt(),
                savedCourseCode.getStatus(),
                savedCourseCode.getExpiryDate(),
                savedCourseCode.getAccount() != null ? savedCourseCode.getAccount().getId() : null,
                savedCourseCode.getCourse().getId()
        );

        // Trả về ApiResponse với status 200 và dữ liệu
        return new ApiResponse<>(200, "Cập nhật mã khóa học thành công", responseDTO);
    }


    public void sendCourseCodesToSelectedStudents(List<StudentCourseDataDTO> selectedStudents, int courseId, Integer accountId) {
        // Step 1: Generate Course Codes
        List<CourseCodeResponseDTO> courseCodes = createCourseCodes(selectedStudents.size(), courseId, accountId, 7);  // Expiry set to 7 days

        // Check if there are enough course codes
        if (courseCodes.size() < selectedStudents.size()) {
            throw new RuntimeException("Not enough course codes generated for the selected students.");
        }

        // Step 2: Iterate through selected students and send email
        int index = 0;  // Initialize a counter to track the current index in courseCodes
        for (StudentCourseDataDTO item : selectedStudents) {
            // Send email logic here (you can use JavaMailSender or other service)
            String email = item.getEmail();
            String subject = "Your Course Code";
            String body = "Hello " + item.getFullname() + ",\n\n" +
                    "Here is your course code for the course " + "" + ":\n" +
                    courseCodes.get(index).getCode() + "\n\n" +
                    "This code is valid for 7 days.\n\n" +
                    "Best regards,\n" +
                    "The Team";

            // Send email
            emailService.sendNotificationEmail(email, subject, body);

            // Increment the index to move to the next course code
            index++;
        }
    }

}
