package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.entity.Crop;
import lk.ijse.agrosmart_systembackend.entity.FieldCrop;

public interface TaskService {
    // ඕනෑම බෝගයකට tasks generate කිරීම
    void generateTasksForFieldCrop(FieldCrop fieldCrop, Double plantingArea);

    // Task එකක් complete කළ බව සලකුණු කිරීම
    String markTaskAsCompleted(String taskId);
}