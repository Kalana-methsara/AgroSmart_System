package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.dto.CropTaskTemplateDTO;

import java.util.List;

public interface CropTaskTemplateService {

    String saveTemplate(CropTaskTemplateDTO dto);

    String updateTemplate(CropTaskTemplateDTO dto);

    String deleteTemplate(String templateId);

    List<CropTaskTemplateDTO> getAllTemplates();

    CropTaskTemplateDTO getTemplateById(String templateId);

    List<CropTaskTemplateDTO> getTemplatesByCropId(String cropId);
}