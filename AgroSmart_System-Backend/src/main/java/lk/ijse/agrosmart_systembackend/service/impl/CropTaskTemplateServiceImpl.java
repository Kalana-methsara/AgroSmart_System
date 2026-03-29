package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.CropTaskTemplateDTO;
import lk.ijse.agrosmart_systembackend.entity.Crop;
import lk.ijse.agrosmart_systembackend.entity.CropTaskTemplate;
import lk.ijse.agrosmart_systembackend.repository.CropRepository;
import lk.ijse.agrosmart_systembackend.repository.CropTaskTemplateRepository;
import lk.ijse.agrosmart_systembackend.service.CropTaskTemplateService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CropTaskTemplateServiceImpl implements CropTaskTemplateService {

    private final CropTaskTemplateRepository templateRepository;
    private final ModelMapper modelMapper;

    @Override
    public String saveTemplate(CropTaskTemplateDTO dto) {
        String newId = generateTemplateId();
        if (templateRepository.existsById(newId)) {
            throw new RuntimeException("Generated Template ID already exists: " + newId);
        }

        CropTaskTemplate entity = modelMapper.map(dto, CropTaskTemplate.class);
        entity.setTemplateId(newId);

        templateRepository.save(entity);
        return "Crop task template saved successfully with ID: " + newId;
    }

    @Override
    public String updateTemplate(CropTaskTemplateDTO dto) {
        CropTaskTemplate existing = templateRepository.findById(dto.getTemplateId())
                .orElseThrow(() -> new RuntimeException("Template not found"));

        existing.setTaskName(dto.getTaskName());
        existing.setStartDay(dto.getStartDay());
        existing.setRepeatInterval(dto.getRepeatInterval());
        existing.setStandardDuration(dto.getStandardDuration());
        existing.setCropId(dto.getCropId()); // just update cropId

        templateRepository.save(existing);
        return "Crop task template updated successfully";
    }

    @Override
    public String deleteTemplate(String templateId) {
        if (!templateRepository.existsById(templateId)) {
            throw new RuntimeException("Template with ID " + templateId + " does not exist");
        }
        templateRepository.deleteById(templateId);
        return "Crop task template deleted successfully";
    }

    @Override
    public List<CropTaskTemplateDTO> getAllTemplates() {
        return modelMapper.map(templateRepository.findAll(),
                new TypeToken<List<CropTaskTemplateDTO>>() {}.getType());
    }

    @Override
    public CropTaskTemplateDTO getTemplateById(String templateId) {
        CropTaskTemplate entity = templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));
        return modelMapper.map(entity, CropTaskTemplateDTO.class);
    }

    @Override
    public List<CropTaskTemplateDTO> getTemplatesByCropId(String cropId) {
        List<CropTaskTemplate> templates = templateRepository.findByCropId(cropId);
        return modelMapper.map(templates, new TypeToken<List<CropTaskTemplateDTO>>() {}.getType());
    }

    // --- Private Helpers ---
    private String generateTemplateId() {
        List<CropTaskTemplate> templates = templateRepository.findAll();
        if (templates.isEmpty()) {
            return "T001";
        }
        String lastId = templates.get(templates.size() - 1).getTemplateId();
        int newId = Integer.parseInt(lastId.replaceAll("[^0-9]", "")) + 1;
        return String.format("T%03d", newId);
    }
}