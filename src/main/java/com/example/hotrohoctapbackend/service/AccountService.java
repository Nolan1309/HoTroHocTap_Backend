package com.example.hotrohoctapbackend.service;


import com.example.hotrohoctapbackend.DTO.AccountDTO;
import com.example.hotrohoctapbackend.DTO.AccountDTO_Proflie;
import com.example.hotrohoctapbackend.DTO.Admin.AddAccountDTOAdmin;
import com.example.hotrohoctapbackend.DTO.Admin.UpdateAccountDTOAdmin;
import com.example.hotrohoctapbackend.DTO.AdminV2.AccountDetailsDTO_V2;
import com.example.hotrohoctapbackend.DTO.AdminV2.AccountTeacherDTO_V2;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminAccount_V2;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminLesssonDTORestoreList;
import com.example.hotrohoctapbackend.DTO.AdminV3.Account.AccountDTOAdmin;
import com.example.hotrohoctapbackend.DTO.AdminV3.AuthorAdmin;
import com.example.hotrohoctapbackend.DTO.AdminV3.Overview;
import com.example.hotrohoctapbackend.DTO.ResponsiveDTOJWT;
import com.example.hotrohoctapbackend.DTO.UpdateAccountDTO;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.RoleUserRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Activity_History;
import com.example.hotrohoctapbackend.entity.RoleUser;
import com.example.hotrohoctapbackend.exception.AccountNotFoundException;
import com.example.hotrohoctapbackend.service.services.ActivityHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleUserRepository roleUserRepository;
    @Autowired
    private ActivityHistoryService activityHistoryService;

    public AccountDTO findByAccount(int id) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent()) {
            AccountDTO dto = new AccountDTO();
            dto.setEmail(account.get().getEmail());
            dto.setFullname(account.get().getFullname());
            dto.setPhone(account.get().getPhone());
            dto.setBirthday(account.get().getBirthday().toString());
            dto.setImage(account.get().getImage());
            dto.setRoleId(account.get().getRole().getId());
            dto.setCreatedAt(account.get().getCreatedAt());
            dto.setUpdatedAt(account.get().getUpdatedAt());
            return dto;
        } else {
            return null;
        }
    }

    public List<AdminAccount_V2> getAccountsByRoles() {

        List<Object[]> results = accountRepository.findAccountsByRoles();
        return results.stream().map(row -> new AdminAccount_V2(
                (int) row[0],                        // id
                convertTimestampToLocalDateTime(row[1]),              // birthday
                convertTimestampToLocalDateTime(row[2]),              // createdAt
                convertTimestampToLocalDateTime(row[3]),              // deletedDate
                (String) row[4],                     // email
                (String) row[5],                     // fullname
                (String) row[6],                     // gender
                (String) row[7],                     // googleId
                (boolean) row[8],                    // isDeleted
                (boolean) row[9],                    // isGoogleAccount
                (String) row[10],                    // phone
                convertTimestampToLocalDateTime(row[11]),             // updatedAt
                (Integer) row[12]                    // roleId
        )).collect(Collectors.toList());
    }

    public Account findAccountByID(int id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Không tìm thấy tài khoản với ID: " + id));
    }

    public Account updatePassword(Account account) {
        return accountRepository.save(account);
    }

    public Boolean checkExistEmail(String email) {
        if (accountRepository.existsByEmail(email)) {
            return true;
        }
        return false;
    }

    public AccountDTO_Proflie updateAccountUser(int accountId, UpdateAccountDTO updateAccountDTO) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountId));

        account.setFullname(updateAccountDTO.getFullname());
        account.setEmail(updateAccountDTO.getEmail());
        account.setPhone(updateAccountDTO.getPhone());
        account.setGender(updateAccountDTO.getGender());
        account.setBirthday(updateAccountDTO.getBirthday());
        if (updateAccountDTO.getImage() != null) {
            account.setImage(updateAccountDTO.getImage());
        }

        Account updatedAccount = accountRepository.save(account);

        AccountDTO_Proflie profile = new AccountDTO_Proflie();
        profile.setId(updatedAccount.getId());
        profile.setFullname(updatedAccount.getFullname());
        profile.setEmail(updatedAccount.getEmail());
        profile.setPhone(updatedAccount.getPhone());
        profile.setGender(updatedAccount.getGender());
        profile.setBirthday(updatedAccount.getBirthday());
        profile.setImage(updatedAccount.getImage());
        profile.setCreatedAt(updatedAccount.getCreatedAt());
        profile.setUpdatedAt(updatedAccount.getUpdatedAt());
        profile.setRoleId(updatedAccount.getRole().getId());

        return profile;
    }


    public AccountDTO_Proflie findByAccountProfile(int id) {
        Optional<Account> account = accountRepository.findById(id);

        AccountDTO_Proflie dto = new AccountDTO_Proflie();
        dto.setId(account.get().getId());
        dto.setImage(account.get().getImage());
        dto.setCreatedAt(account.get().getCreatedAt());
        dto.setUpdatedAt(account.get().getUpdatedAt());
        dto.setRoleId(account.get().getRole().getId());
        dto.setEmail(account.get().getEmail());
        dto.setFullname(account.get().getFullname());
        dto.setPhone(account.get().getPhone());
        dto.setBirthday(account.get().getBirthday());
        dto.setGender(account.get().getGender());

        return dto;
    }

    public ResponsiveDTOJWT findByAccount(String email) {
        Account account = accountRepository.findByEmail(email);
        RoleUser role = account.getRole();
        return new ResponsiveDTOJWT(account.getId(), account.getFullname(), account.getEmail(), role.getId());
    }

    public ResponseEntity<Map<String, String>> dangkyAccount(AccountDTO user) {
        if (accountRepository.existsByEmail(user.getEmail())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Email đã tồn tại!");
            return ResponseEntity.badRequest().body(errorResponse);
        }


        Account user1 = new Account();

        user1.setFullname(user.getFullname());
        user1.setEmail(user.getEmail());
        user1.setPassword(passwordEncoder.encode(user.getPassword()));
        user1.setPhone(user.getPhone());

        user1.setCreatedAt(LocalDateTime.now());
        user1.setUpdatedAt(LocalDateTime.now());
        LocalDateTime dateTime = LocalDateTime.parse(user.getBirthday());

        user1.setBirthday(dateTime);

        RoleUser roleUser = new RoleUser();
        roleUser.setId(2);
        user1.setRole(roleUser);

        Account account = accountRepository.save(user1);
        if (account != null) {
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Đăng ký thành công!");
            successResponse.put("accountID", String.valueOf(account.getId()));
            successResponse.put("email", account.getEmail());
            successResponse.put("fullname", account.getFullname());
            successResponse.put("birthday", account.getBirthday().toString());
            successResponse.put("phone", account.getPhone());
            return ResponseEntity.ok(successResponse);
        } else {
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Đăng ký không thành công!");
            return ResponseEntity.ok(successResponse);
        }
    }

    public boolean checkEmailExists(String email) {
        return accountRepository.existsByEmail(email);
    }

    public boolean checkPhoneExists(String phone) {
        return accountRepository.existsByPhone(phone);
    }

    public boolean checkPhoneExists(String phone, int accountId) {
        return accountRepository.existsByPhoneAndIdNot(phone, accountId);
    }

    public Account createAccount(Account account, String password) {
        account.setPassword(passwordEncoder.encode(password));
        return accountRepository.save(account);
    }

    public Account updateAccount(Account account, String password) {
        if (password != null) {
            account.setPassword(passwordEncoder.encode(password));
        }

        return accountRepository.save(account);
    }

    public Account saveTaiKhoan(String fullname, String password, String phone, LocalDateTime birthday, String email) {

        Account account = new Account();

        account.setFullname(fullname);
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(password));
        account.setPhone(phone);
        account.setStatus(Account.AccountStatus.ACTIVE);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        account.setBirthday(birthday);
        account.setTotalPoint("0");
        RoleUser roleUser = new RoleUser();
        roleUser.setId(2);
        account.setRole(roleUser);

        return accountRepository.save(account);

    }

    public UpdateAccountDTOAdmin getAccountById(Integer id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));

        return convertToDTOAdmin(account);
    }

    public ResponseEntity<?> updateAccountAdmin(UpdateAccountDTOAdmin updatedAccountDTO) {
        Optional<Account> existingAccountOpt = accountRepository.findById(updatedAccountDTO.getId());

        if (existingAccountOpt.isPresent()) {
            Account existingAccount = existingAccountOpt.get();
            existingAccount.setFullname(updatedAccountDTO.getFullname());
            existingAccount.setEmail(updatedAccountDTO.getEmail());
            existingAccount.setPhone(updatedAccountDTO.getPhone());
            existingAccount.setGender(updatedAccountDTO.getGender());

            if (updatedAccountDTO.getImage() != null) {
                existingAccount.setImage(updatedAccountDTO.getImage());
            } else {
                existingAccount.setImage(existingAccountOpt.get().getImage());
            }


            existingAccount.setBirthday(updatedAccountDTO.getBirthday());
            existingAccount.setUpdatedAt(LocalDateTime.now());

            // Kiểm tra và cập nhật role nếu có
            if (updatedAccountDTO.getRoleId() != null) {
                RoleUser role = roleUserRepository.findById(updatedAccountDTO.getRoleId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy Role với id: " + updatedAccountDTO.getRoleId()));
                existingAccount.setRole(role);
            }

            Account updatedAccount = accountRepository.save(existingAccount);
            return ResponseEntity.ok(updatedAccount);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy Account với id: " + updatedAccountDTO.getId());
        }
    }

    public Account deleteAccountAdmin(int accountId) {
        // Tìm tài khoản theo ID
        Optional<Account> accountOpt = accountRepository.findById(accountId);

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            account.setDeleted(true);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return accountRepository.save(account);
        } else {
            throw new RuntimeException("Account not found with id: " + accountId);
        }
    }

    public Account activeAccountAdmin(int accountId) {
        // Tìm tài khoản theo ID
        Optional<Account> accountOpt = accountRepository.findById(accountId);

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            account.setDeleted(false);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return accountRepository.save(account);
        } else {
            throw new RuntimeException("Account not found with id: " + accountId);
        }
    }

    public List<AuthorAdmin> getAuthorsByRole() {
        return accountRepository.findAuthorsByRole();
    }

    public Account addAccountAdmin(AddAccountDTOAdmin accountDTOAdmin) {
        // 1. Kiểm tra xem email đã tồn tại hay chưa
        Optional<Account> existingAccount = accountRepository.findByEmailOptional(accountDTOAdmin.getEmail());
        if (existingAccount.isPresent()) {
            // Bạn có thể ném ra một ngoại lệ tùy chỉnh hoặc sử dụng một ngoại lệ phù hợp
            throw new IllegalArgumentException("Email đã được sử dụng. Vui lòng chọn email khác.");
        }

        // 2. Tạo đối tượng Account mới
        Account newAccount = new Account();

        // 3. Đặt các thuộc tính cho Account từ DTO
        newAccount.setFullname(accountDTOAdmin.getFullname());
        newAccount.setEmail(accountDTOAdmin.getEmail());
        newAccount.setPhone(accountDTOAdmin.getPhone());
        newAccount.setGender(accountDTOAdmin.getGender());
        newAccount.setBirthday(accountDTOAdmin.getBirthday());
        newAccount.setImage(accountDTOAdmin.getImage());

        // 4. Mặc định ban đầu cho trường createdAt và updatedAt là thời điểm hiện tại
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setUpdatedAt(LocalDateTime.now());

        // 5. Thiết lập role cho tài khoản mới
        if (accountDTOAdmin.getRoleId() != null) {
            RoleUser role = roleUserRepository.findById(accountDTOAdmin.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role không tồn tại với ID: " + accountDTOAdmin.getRoleId()));
            newAccount.setRole(role);
        }

        // 6. Thiết lập trạng thái ban đầu cho isDeleted và deletedDate
        newAccount.setDeleted(false);
        // Nếu isDeleted là false, có lẽ deletedDate nên là null
        newAccount.setDeletedDate(null);

        // 7. Mã hóa mật khẩu
        String encodedPassword = passwordEncoder.encode(accountDTOAdmin.getPassword());
        newAccount.setPassword(encodedPassword);

        // 8. Lưu đối tượng Account mới vào cơ sở dữ liệu
        return accountRepository.save(newAccount);
    }

    private UpdateAccountDTOAdmin convertToDTOAdmin(Account account) {
        UpdateAccountDTOAdmin dto = new UpdateAccountDTOAdmin();
        dto.setId(account.getId());
        dto.setFullname(account.getFullname());
        dto.setEmail(account.getEmail());
        dto.setPhone(account.getPhone());
        dto.setGender(account.getGender());
        dto.setImage(account.getImage());
        dto.setBirthday(account.getBirthday());
        dto.setRoleId(account.getRole().getId());
        return dto;
    }

    public Account updatePasswordResetUser(Integer accountId, String passwordReset) {
        Optional<Account> userOptional = accountRepository.findById(accountId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Account with ID " + accountId + " not found");
        }
        Account user = userOptional.get();
        user.setPassword(passwordReset);
        return accountRepository.saveAndFlush(user);
    }

    private LocalDateTime convertTimestampToLocalDateTime(Object timestampObj) {
        if (timestampObj instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) timestampObj;
            return timestamp.toLocalDateTime();
        }
        return null;
    }

    public List<AccountTeacherDTO_V2> getActiveAccountsByRole(int roleId) {
        List<Object[]> results = accountRepository.findActiveAccountsByRoleId(3);

        return results.stream()
                .map(obj -> new AccountTeacherDTO_V2(
                        (int) obj[0],  // id
                        convertTimestampToLocalDateTime(obj[1]),  // birthday
                        convertTimestampToLocalDateTime(obj[2]),  // createdAt
                        convertTimestampToLocalDateTime(obj[3]),  // deletedDate
                        (String) obj[4],  // email
                        (String) obj[5],  // fullname
                        (String) obj[6],  // gender
                        (boolean) obj[7], // isDeleted
                        (String) obj[8],  // phone
                        convertTimestampToLocalDateTime(obj[9]), // updatedAt
                        (int) obj[10]  // roleId
                )).toList();
    }

    public Page<AccountDetailsDTO_V2> getAllListAccountSearch(Integer roleId, String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created_at").descending());
        Page<Object[]> results = accountRepository.searchAccountsWithPagination(roleId, searchTerm, pageable);

        return results.map(result -> {
            AccountDetailsDTO_V2 dto = new AccountDetailsDTO_V2();
            dto.setId((Integer) result[0]); // id
            dto.setBirthday(result[1] != null ? result[1].toString() : null); // birthday
            dto.setCreatedAt(result[2] != null ? result[2].toString() : null); // created_at
            dto.setDeletedDate(result[3] != null ? result[3].toString() : null); // deleted_date
            dto.setEmail((String) result[4]); // email
            dto.setFullname((String) result[5]); // fullname
            dto.setGender((String) result[6]); // gender
            dto.setGoogleId((String) result[7]); // google_id
            dto.setImage((String) result[8]); // image
            dto.setIsDeleted((Boolean) result[9]); // is_deleted
            dto.setIsGoogleAccount((Boolean) result[10]); // is_google_account
            dto.setPhone((String) result[11]); // phone
            dto.setUpdatedAt(result[12] != null ? result[12].toString() : null); // updated_at
            dto.setRoleId((Integer) result[13]);
            return dto;
        });
    }

    public Page<AccountDetailsDTO_V2> getAllListAccountRestore(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created_at").descending());
        Page<Object[]> results = accountRepository.findAllDeletedAccounts(pageable);

        return results.map(result -> {
            AccountDetailsDTO_V2 dto = new AccountDetailsDTO_V2();
            dto.setId((Integer) result[0]); // id
            dto.setBirthday(result[1] != null ? result[1].toString() : null); // birthday
            dto.setCreatedAt(result[2] != null ? result[2].toString() : null); // created_at
            dto.setDeletedDate(result[3] != null ? result[3].toString() : null); // deleted_date
            dto.setEmail((String) result[4]); // email
            dto.setFullname((String) result[5]); // fullname
            dto.setGender((String) result[6]); // gender
            dto.setGoogleId((String) result[7]); // google_id
            dto.setImage((String) result[8]); // image
            dto.setIsDeleted((Boolean) result[9]); // is_deleted
            dto.setIsGoogleAccount((Boolean) result[10]); // is_google_account
            dto.setPhone((String) result[11]); // phone
            dto.setUpdatedAt(result[12] != null ? result[12].toString() : null); // updated_at
            dto.setRoleId((Integer) result[13]);
            return dto;
        });
    }

    public Page<AccountDetailsDTO_V2> getAllListAccountRestoreSearch(String fullName, String deletedDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created_at").descending());
        Page<Object[]> results = accountRepository.searchAccountsByFullNameAndDeletedDate(fullName, deletedDate, pageable);
        if (fullName != null && !fullName.isEmpty() && deletedDate != null && !deletedDate.isEmpty()) {
            results = accountRepository.searchAccountsByFullNameAndDeletedDate(fullName, deletedDate, pageable);
        } else if (fullName != null && !fullName.isEmpty()) {
            results = accountRepository.searchAccountsByFullName(fullName, pageable);
        } else if (deletedDate != null && !deletedDate.isEmpty()) {
            results = accountRepository.searchAccountsByDeletedDate(deletedDate, pageable);
        }
        return results.map(result -> {
            AccountDetailsDTO_V2 dto = new AccountDetailsDTO_V2();
            dto.setId((Integer) result[0]); // id
            dto.setBirthday(result[1] != null ? result[1].toString() : null); // birthday
            dto.setCreatedAt(result[2] != null ? result[2].toString() : null); // created_at
            dto.setDeletedDate(result[3] != null ? result[3].toString() : null); // deleted_date
            dto.setEmail((String) result[4]); // email
            dto.setFullname((String) result[5]); // fullname
            dto.setGender((String) result[6]); // gender
            dto.setGoogleId((String) result[7]); // google_id
            dto.setImage((String) result[8]); // image
            dto.setIsDeleted((Boolean) result[9]); // is_deleted
            dto.setIsGoogleAccount((Boolean) result[10]); // is_google_account
            dto.setPhone((String) result[11]); // phone
            dto.setUpdatedAt(result[12] != null ? result[12].toString() : null); // updated_at
            dto.setRoleId((Integer) result[13]);
            return dto;
        });
    }

    public List<AccountDetailsDTO_V2> getAllListAccountAdminAndTeacher() {

        List<Object[]> results = accountRepository.findAccountRestoreListAdminAndTeacher();
        List<AccountDetailsDTO_V2> accountDetailsDTOV2s = new ArrayList<>();
        for (Object[] result : results) {
            AccountDetailsDTO_V2 dto = new AccountDetailsDTO_V2();
            dto.setId((Integer) result[0]); // id
            dto.setBirthday(result[1] != null ? result[1].toString() : null); // birthday
            dto.setCreatedAt(result[2] != null ? result[2].toString() : null); // created_at
            dto.setDeletedDate(result[3] != null ? result[3].toString() : null); // deleted_date
            dto.setEmail((String) result[4]); // email
            dto.setFullname((String) result[5]); // fullname
            dto.setGender((String) result[6]); // gender
            dto.setGoogleId((String) result[7]); // google_id
            dto.setImage((String) result[8]); // image
            dto.setIsDeleted((Boolean) result[9]); // is_deleted
            dto.setIsGoogleAccount((Boolean) result[10]); // is_google_account
            dto.setPhone((String) result[11]); // phone
            dto.setUpdatedAt(result[12] != null ? result[12].toString() : null); // updated_at
            dto.setRoleId((Integer) result[13]);
            accountDetailsDTOV2s.add(dto);

        }
        return accountDetailsDTOV2s;
    }

    public Account updateRestoreAccount(AccountDetailsDTO_V2 accountDetailsDTOV2) {
        Optional<Account> accountOptional = accountRepository.findById(accountDetailsDTOV2.getId());
        if (accountOptional.isEmpty()) {
            throw new RuntimeException("Account not found with id: " + accountDetailsDTOV2.getId());
        } else {
            Account account = accountOptional.get();
            account.setDeleted(false);

            return accountRepository.save(account);
        }
    }

    public void deleteRestoreAccount(AccountDetailsDTO_V2 accountDetailsDTOV2) {
        Optional<Account> accountOptional = accountRepository.findById(accountDetailsDTOV2.getId());
        if (accountOptional.isEmpty()) {
            throw new RuntimeException("Account not found with id: " + accountDetailsDTOV2.getId());
        } else {
            accountRepository.delete(accountOptional.get());
        }
    }

    public Page<AccountDetailsDTO_V2> getAccountStudentByCourseId(Integer courseId, Integer roleId, String fullName, String enrollmentDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> results = accountRepository.getAccountStudentByCourseIdAndRoleId(courseId, roleId, fullName, enrollmentDate, pageable);

        return results.map(result -> {
            AccountDetailsDTO_V2 dto = new AccountDetailsDTO_V2();
            dto.setId((Integer) result[0]); // id
            dto.setBirthday(result[1] != null ? result[1].toString() : null); // birthday
            dto.setCreatedAt(result[2] != null ? result[2].toString() : null); // created_at
            dto.setDeletedDate(result[3] != null ? result[3].toString() : null); // deleted_date
            dto.setEmail((String) result[4]); // email
            dto.setFullname((String) result[5]); // fullname
            dto.setGender((String) result[6]); // gender
            dto.setGoogleId((String) result[7]); // google_id
            dto.setImage((String) result[8]); // image
            dto.setIsDeleted((Boolean) result[9]); // is_deleted
            dto.setIsGoogleAccount((Boolean) result[10]); // is_google_account
            dto.setPhone((String) result[11]); // phone
            dto.setUpdatedAt(result[12] != null ? result[12].toString() : null); // updated_at
            dto.setRoleId((Integer) result[13]);
            return dto;
        });
    }

    // Phương thức lấy danh sách tài khoản với phân trang và lọc theo tên và trạng thái
    public Page<AccountDTOAdmin> getAccounts(String fullname, Account.AccountStatus status, Pageable pageable) {
        Page<Account> accountPage = accountRepository.findAccountsByFilters(fullname, status, pageable);

        return accountPage.map(this::convertToDTO); // Chuyển từ Account entity sang AccountDTOAdmin
    }


    // Chuyển đổi từ Account entity sang AccountDTOAdmin
    private AccountDTOAdmin convertToDTO(Account account) {
        return new AccountDTOAdmin(
                account.getId(),
                account.getFullname(),
                account.getEmail(),
                account.getPhone(),
                account.getRole().getRoleName(),
                account.getRole().getId(), // Lấy ID của role
                account.getStatus().toString(),
                account.getImage(),
                account.getLastLogin(),
                account.getCreatedAt(),
                account.getUpdatedAt()
        );
    }

    public Overview getOverviewByAccountId(Integer accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountId));

        Integer dayStreak = activityHistoryService.calculateLoginStreak(accountId);
        Overview overview = new Overview();
        overview.setAccountId(accountId);
        overview.setAccountName(account.getFullname());
        overview.setEmail(account.getEmail());
        overview.setTotalPoints(Integer.parseInt(account.getTotalPoint()));
        overview.setCountCourse(account.getEnrolledCourses().size());
        overview.setCountDocument(account.getGeneralDocumentAcounts().size());
        overview.setDayStreak(dayStreak);

        // Kiểm tra nếu danh sách wallets không rỗng
        if (account.getWallets() != null && !account.getWallets().isEmpty()) {
            overview.setBalanceWallet(account.getWallets().get(0).getBalance());
            overview.setWalletId(account.getWallets().get(0).getId());
        } else {
            // Nếu không có ví, bạn có thể chọn trả về giá trị mặc định hoặc thông báo lỗi
            overview.setBalanceWallet(new BigDecimal(0));  // Giá trị mặc định, nếu không có ví
            overview.setWalletId(null);       // ID null nếu không có ví
        }

        return overview;
    }
}
