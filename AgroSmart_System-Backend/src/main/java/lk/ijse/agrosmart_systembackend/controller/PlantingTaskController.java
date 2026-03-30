package lk.ijse.agrosmart_systembackend.controller;

import lk.ijse.agrosmart_systembackend.dto.PlantingTaskDTO;
import lk.ijse.agrosmart_systembackend.service.TaskService;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/planting-task")
@RequiredArgsConstructor
public class PlantingTaskController {

    private final TaskService taskService;

    // GET ALL TASKS
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllTasks() {
        List<PlantingTaskDTO> tasks = taskService.getAllTask();
        return new ResponseEntity<>(
                new ApiResponse(200, "Tasks Retrieved Successfully", tasks),
                HttpStatus.OK
        );
    }

    // GET TASK BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTaskById(@PathVariable String id) {
        PlantingTaskDTO task = taskService.getTaskById(id);
        return new ResponseEntity<>(
                new ApiResponse(200, "Task Found", task),
                HttpStatus.OK
        );
    }

    // GET TASKS BY FIELD CROP
    @GetMapping("/field-crop/{fieldCropId}")
    public ResponseEntity<ApiResponse> getTasksByFieldCrop(@PathVariable String fieldCropId) {
        List<PlantingTaskDTO> tasks = taskService.getTasksByFieldCrop(fieldCropId);
        return new ResponseEntity<>(
                new ApiResponse(200, "Tasks Retrieved Successfully", tasks),
                HttpStatus.OK
        );
    }

    // GET TASKS BY STATUS
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse> getTasksByStatus(@PathVariable String status) {
        List<PlantingTaskDTO> tasks = taskService.getTasksByStatus(status);
        return new ResponseEntity<>(
                new ApiResponse(200, "Tasks Retrieved Successfully", tasks),
                HttpStatus.OK
        );
    }

    // GET OVERDUE TASKS
    @GetMapping("/overdue")
    public ResponseEntity<ApiResponse> getOverdueTasks() {
        List<PlantingTaskDTO> tasks = taskService.getOverdueTasks();
        return new ResponseEntity<>(
                new ApiResponse(200, "Overdue Tasks Retrieved", tasks),
                HttpStatus.OK
        );
    }

    // GET PENDING TASK COUNT
    @GetMapping("/pending/count")
    public ResponseEntity<ApiResponse> getPendingTasksCount() {
        long count = taskService.getPendingTasksCount();
        return new ResponseEntity<>(
                new ApiResponse(200, "Pending Tasks Count", count),
                HttpStatus.OK
        );
    }

    // MARK AS COMPLETED ✅ Moved from template controller, correct path
    @PutMapping("/{id}/complete")
    public ResponseEntity<ApiResponse> markTaskAsCompleted(@PathVariable String id) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Task Marked As Completed",
                        taskService.markTaskAsCompleted(id)),
                HttpStatus.OK
        );
    }

    // DELETE TASK
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(
                new ApiResponse(200, "Task Deleted Successfully", null),
                HttpStatus.OK
        );
    }
}