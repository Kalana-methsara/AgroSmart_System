package lk.ijse.agrosmart_systembackend.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.agrosmart_systembackend.entity.Crop;
import lk.ijse.agrosmart_systembackend.entity.CropTaskTemplate;
import lk.ijse.agrosmart_systembackend.entity.FieldCrop;
import lk.ijse.agrosmart_systembackend.entity.PlantingTask;
import lk.ijse.agrosmart_systembackend.repository.CropTaskTemplateRepository;
import lk.ijse.agrosmart_systembackend.repository.PlantingTaskRepository;
import lk.ijse.agrosmart_systembackend.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor

public class TaskServiceImpl implements TaskService {

    private final PlantingTaskRepository plantingTaskRepo;
    private final CropTaskTemplateRepository templateRepo;

    @Override
    public void generateTasksForFieldCrop(FieldCrop fieldCrop, Double plantingArea) {
        int multiplier = (int) Math.round(plantingArea);
        if (multiplier < 1) multiplier = 1;

        // ✅ fieldCrop.getCrop().getCropId() — correct, Crop entity has getCropId()
        List<CropTaskTemplate> templates = templateRepo.findByCropId(fieldCrop.getCrop().getCropId());

        double finalMultiplier = multiplier;
        List<PlantingTask> tasks = templates.stream().map(template -> {
            PlantingTask task = new PlantingTask();
            task.setTaskId("TASK-" + UUID.randomUUID().toString().substring(0, 5));
            task.setTaskName(template.getTaskName());

            // ✅ fieldCrop has assignedDate
            if (fieldCrop.getAssignedDate() != null) {
                task.setDueDate(fieldCrop.getAssignedDate().plusDays(template.getStartDay()));
            }

            task.setCalculatedDuration(template.getStandardDuration() * finalMultiplier);
            task.setStatus("PENDING");
            task.setFieldCrop(fieldCrop);
            return task;
        }).toList();

        // ✅ saveAll instead of save() in a loop
        plantingTaskRepo.saveAll(tasks);
    }

    @Override
    public String markTaskAsCompleted(String taskId) {
        PlantingTask task = plantingTaskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        task.setStatus("COMPLETED");
        plantingTaskRepo.save(task);

        return "Task " + taskId + " marked as COMPLETED successfully!";
    }
}