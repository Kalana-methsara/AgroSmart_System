package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.dto.CropTaskTemplateDTO;
import lk.ijse.agrosmart_systembackend.dto.PlantingTaskDTO;
import lk.ijse.agrosmart_systembackend.entity.Crop;
import lk.ijse.agrosmart_systembackend.entity.FieldCrop;

import java.util.List;

public interface TaskService {
    void generateTasksForFieldCrop(FieldCrop fieldCrop, Double plantingArea);
    String markTaskAsCompleted(String taskId);
    List<PlantingTaskDTO> getAllTask();
    List<PlantingTaskDTO> getTasksByFieldCrop(String fieldCropId);
    List<PlantingTaskDTO> getOverdueTasks();
    PlantingTaskDTO getTaskById(String taskId);
    List<PlantingTaskDTO> getTasksByStatus(String status);
    long getPendingTasksCount();
    void deleteTask(String taskId);
}