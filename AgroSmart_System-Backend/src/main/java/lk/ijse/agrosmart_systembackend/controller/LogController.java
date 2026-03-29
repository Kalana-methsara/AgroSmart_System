package lk.ijse.agrosmart_systembackend.controller;

import lk.ijse.agrosmart_systembackend.dto.LogDTO;
import lk.ijse.agrosmart_systembackend.service.LogService;
import lk.ijse.agrosmart_systembackend.service.TaskService;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/log")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;
    private final TaskService taskService; // TaskService එක Inject කරගත්තා

    // CREATE
    @PostMapping
    public ResponseEntity<ApiResponse> saveLog(@RequestBody LogDTO logDTO){
        return new ResponseEntity<>(
                new ApiResponse(201,"Log Saved Successfully", logService.saveLog(logDTO)),
                HttpStatus.CREATED
        );
    }
    @PatchMapping("/complete/{taskId}")
    public ResponseEntity<ApiResponse> completeTask(@PathVariable String taskId) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Task Marked as Completed",taskService.markTaskAsCompleted(taskId)),
                HttpStatus.OK
        );
    }
    // GET ALL
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllLogs() {
        return new ResponseEntity<>(
                new ApiResponse(200, "Success", logService.getAllLogs()),
                HttpStatus.OK
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getLogById(@PathVariable String id) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Log Found", logService.searchLog(id)),
                HttpStatus.OK
        );
    }

    // UPDATE
    @PutMapping
    public ResponseEntity<ApiResponse> updateLog(@RequestBody LogDTO logDTO) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Log Updated Successfully", logService.updateLog(logDTO)),
                HttpStatus.OK
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteLog(@PathVariable String id) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Log Deleted Successfully", logService.deleteLog(id)),
                HttpStatus.OK
        );
    }
}
