package lk.ijse.agrosmart_systembackend.controller;

import lk.ijse.agrosmart_systembackend.dto.RegisterDTO;
import lk.ijse.agrosmart_systembackend.dto.UserDTO;
import lk.ijse.agrosmart_systembackend.service.UserService;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // CREATE / REGISTER USER
    @PostMapping
    public ResponseEntity<ApiResponse> saveUser(@RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(new ApiResponse
                (200, "User Saved Successfully", userService.saveUser(registerDTO)));
    }

    // GET ALL USERS
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(
                new ApiResponse(200, "Success", users),
                HttpStatus.OK
        );
    }

    //  GET USER BY USERNAME OR EMAIL
    @GetMapping("/{identifier}")
    public ResponseEntity<ApiResponse> getUserDetails(@PathVariable String identifier) {
        UserDTO userDTO = userService.getUserDetails(identifier);
        return new ResponseEntity<>(
                new ApiResponse(200, "User details retrieved successfully", userDTO),
                HttpStatus.OK
        );
    }

    // UPDATE USER
    @PutMapping
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(
                new ApiResponse(200, "User Updated Successfully", userService.updateUser(userDTO)),
                HttpStatus.OK
        );
    }

    // DELETE USER
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<>(
                new ApiResponse(200, "User Deleted Successfully", userService.deleteUser(id)),
                HttpStatus.OK
        );
    }
}