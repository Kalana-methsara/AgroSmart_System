package lk.ijse.agrosmart_systembackend.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.agrosmart_systembackend.dto.PlantingTaskDTO;
import lk.ijse.agrosmart_systembackend.entity.CropTaskTemplate;
import lk.ijse.agrosmart_systembackend.entity.FieldCrop;
import lk.ijse.agrosmart_systembackend.entity.PlantingTask;
import lk.ijse.agrosmart_systembackend.exceptions.TaskNotFoundException;
import lk.ijse.agrosmart_systembackend.repository.CropTaskTemplateRepository;
import lk.ijse.agrosmart_systembackend.repository.PlantingTaskRepository;
import lk.ijse.agrosmart_systembackend.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final PlantingTaskRepository plantingTaskRepo;
    private final CropTaskTemplateRepository templateRepo;

    @Override
    public void generateTasksForFieldCrop(FieldCrop fieldCrop, Double plantingArea) {
        log.info("Generating tasks for field crop: {} with area: {}",
                fieldCrop.getFieldCropId(), plantingArea);

        int multiplier = (int) Math.round(plantingArea);
        if (multiplier < 1) multiplier = 1;

        List<CropTaskTemplate> templates = templateRepo.findByCropId(
                fieldCrop.getCrop().getCropId()
        );

        if (templates.isEmpty()) {
            log.warn("No task templates found for crop: {}",
                    fieldCrop.getCrop().getCropId());
            return;
        }

        double finalMultiplier = multiplier;
        List<PlantingTask> tasks = templates.stream().map(template -> {
            PlantingTask task = new PlantingTask();
            task.setTaskId(generateTaskId());
            task.setTaskName(template.getTaskName());

            if (fieldCrop.getAssignedDate() != null && template.getStartDay() != null) {
                task.setDueDate(fieldCrop.getAssignedDate().plusDays(template.getStartDay()));
            }

            task.setCalculatedDuration(template.getStandardDuration() * finalMultiplier);
            task.setStatus("PENDING");
            task.setFieldCrop(fieldCrop);
            return task;
        }).toList();

        plantingTaskRepo.saveAll(tasks);
        log.info("Generated {} tasks for field crop: {}",
                tasks.size(), fieldCrop.getFieldCropId());
    }

    @Override
    public String markTaskAsCompleted(String taskId) {
        System.out.println(taskId);
        log.info("Marking task as completed: {}", taskId);

        PlantingTask task = plantingTaskRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(
                        "Task not found with ID: " + taskId));

        if ("COMPLETED".equals(task.getStatus())) {
            throw new IllegalStateException("Task is already completed: " + taskId);
        }

        task.setStatus("COMPLETED");
        plantingTaskRepo.save(task);

        log.info("Task {} marked as COMPLETED successfully", taskId);
        return "Task " + taskId + " marked as COMPLETED successfully!";
    }

    @Override
    public List<PlantingTaskDTO> getAllTask() {
        List<PlantingTask> plantingTasks = plantingTaskRepo.findAll();

        return plantingTasks.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    public PlantingTaskDTO toDTO(PlantingTask task) {
        PlantingTaskDTO dto = new PlantingTaskDTO();
        dto.setTaskId(task.getTaskId());
        dto.setTaskName(task.getTaskName());
        dto.setDueDate(task.getDueDate());
        dto.setStatus(task.getStatus());
        dto.setCalculatedDuration(task.getCalculatedDuration());

        if (task.getFieldCrop() != null) {
            dto.setFieldCropId(task.getFieldCrop().getFieldCropId());
            if (task.getFieldCrop().getCrop() != null) {
                dto.setCropId(task.getFieldCrop().getCrop().getCropId());
            }
        }
        return dto;
    }

    @Override
    public List<PlantingTaskDTO> getTasksByFieldCrop(String fieldCropId) {
        log.debug("Fetching tasks for field crop: {}", fieldCropId);

        if (fieldCropId == null || fieldCropId.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Field crop ID cannot be null or empty");
        }

        List<PlantingTask> tasks =
                plantingTaskRepo.findByFieldCrop_FieldCropId(fieldCropId);
        return tasks.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlantingTaskDTO> getOverdueTasks() {
        log.debug("Fetching overdue tasks");

        LocalDate today = LocalDate.now();
        List<PlantingTask> overdueTasks =
                plantingTaskRepo.findByDueDateBeforeAndStatus(today, "PENDING");

        return overdueTasks.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PlantingTaskDTO getTaskById(String taskId) {
        log.debug("Fetching task by ID: {}", taskId);

        if (taskId == null || taskId.trim().isEmpty()) {
            throw new IllegalArgumentException("Task ID cannot be null or empty");
        }

        PlantingTask task = plantingTaskRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(
                        "Task not found with ID: " + taskId));

        return toDTO(task);
    }

    @Override
    public List<PlantingTaskDTO> getTasksByStatus(String status) {
        log.debug("Fetching tasks with status: {}", status);

        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }

        List<PlantingTask> tasks = plantingTaskRepo.findByStatus(status);
        return tasks.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long getPendingTasksCount() {
        log.debug("Counting pending tasks");
        return plantingTaskRepo.countByStatus("PENDING");
    }

    @Override
    public void deleteTask(String taskId) {
        log.info("Deleting task: {}", taskId);

        if (!plantingTaskRepo.existsById(taskId)) {
            throw new TaskNotFoundException("Task not found with ID: " + taskId);
        }

        plantingTaskRepo.deleteById(taskId);
        log.info("Task {} deleted successfully", taskId);
    }

    private String generateTaskId() {
        return "TASK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}