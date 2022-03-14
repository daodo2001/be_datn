package com.fpt.hotel.owner.controller;

import com.fpt.hotel.model.User;
import com.fpt.hotel.owner.dto.response.OwnerResponse;
import com.fpt.hotel.owner.service.OwnerService;
import com.fpt.hotel.payload.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/owner")
@CrossOrigin("*")
public class OwnerController {

    @Autowired
    OwnerService ownerService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("user")
    public ResponseEntity<ResponseObject> getAll(@RequestParam("role_name") String roleName) {

        List<OwnerResponse> findAll = ownerService.findAll(roleName);
        if (findAll.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Không có user nào", null));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "Trả về dữ liệu user thành công", findAll));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "user/{folder}", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseObject> create(@PathVariable("folder") String folder, @RequestPart("user") String user,
                                                 @RequestPart(name = "file", required = false) List<MultipartFile> files) {

        OwnerResponse userRes = ownerService.save(folder, user, files);

        if (userRes == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Thêm thất bại , đã có username hoặc email này!!", userRes));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.name(), "Thêm user thành công", userRes));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("user/{id}")
    public ResponseEntity<ResponseObject> updateIsEnabled(@PathVariable("id") Integer id) {

        OwnerResponse update = ownerService.updateIsEnabled(id);

        if (update == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("Not found", "Không có user này", null));
        }
        String lockUser = "Khóa tài khoản " + update.getUsername().toUpperCase() + " thành công!";

        String openUser = "Mở khóa tài khoản " + update.getUsername().toUpperCase() + " thành công!";

        String message = update.getEnabled() == 1 ? openUser : lockUser;

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Ok", message, update));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("user/{idUser}/{idHotel}")
    public ResponseEntity<ResponseObject> tranferHotel(@PathVariable("idUser") Integer idUser,
                                                       @PathVariable("idHotel") Long idHotel) {

        OwnerResponse update = ownerService.updateHotel(idUser, idHotel);

        if (update == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("Not found", "Không có user này", null));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Ok", "Chuyển cơ sở thành công", update));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/{id}")
    public ResponseEntity<ResponseObject> getUserById(@PathVariable("id") Integer id) {

        OwnerResponse ownerResponse = ownerService.findById(id);
        if (ownerResponse == null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Không có user nào", null));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "Trả về dữ liệu user thành công", ownerResponse));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "user", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseObject>
    updateUser(@RequestPart("user") String user,
               @RequestPart(value = "file", required = false) List<MultipartFile> files) {

        OwnerResponse update = ownerService.updateUser(user, files);

        if (update == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("Not found", "Không tìm thấy user này", null));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Ok", "Cập nhật user thành công", update));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("user/change-password/{id}")
    public ResponseEntity<ResponseObject> changePassword(@PathVariable("id") Integer idUser,
                                                         @RequestParam("oldPassword") String oldPassword ,
                                                         @RequestParam("newPassword") String newPassword
    ) {
        OwnerResponse changePassword = ownerService.changePassword(idUser , oldPassword, newPassword);

        if (changePassword == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(HttpStatus.BAD_REQUEST.name(), "Mật khẩu cũ không đúng", null));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK.name(), "Đổi mật khẩu thành công", changePassword));
    }


}
