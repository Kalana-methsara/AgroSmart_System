package lk.ijse.agrosmart_systembackend.service.impl;

import jakarta.transaction.Transactional;
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
        // Multiplier එක අක්කර ප්‍රමාණය අනුව (e.g., 4.6 -> 5)
        int multiplier = (int) Math.round(plantingArea);
        if (multiplier < 1) multiplier = 1;

        // DB එකෙන් අදාළ බෝගයට (Crop) අයිති templates list එක ගන්නවා
        List<CropTaskTemplate> templates = templateRepo.findByCropId(fieldCrop.getCrop().getCropId());

        for (CropTaskTemplate template : templates) {
            PlantingTask task = new PlantingTask();

            // Task ID generate කිරීම
            task.setTaskId("TASK-" + UUID.randomUUID().toString().substring(0, 5));
            task.setTaskName(template.getTaskName());

            // Assigned Date එකට template එකේ startDay එකතු කර dueDate එක සෑදීම
            if (fieldCrop.getAssignedDate() != null) {
                task.setDueDate(fieldCrop.getAssignedDate().plusDays(template.getStartDay()));
            }

            // කාලය = standard_duration * multiplier
            task.setCalculatedDuration(template.getStandardDuration() * multiplier);

            task.setStatus("PENDING");
            task.setFieldCrop(fieldCrop);

            plantingTaskRepo.save(task);
        }
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