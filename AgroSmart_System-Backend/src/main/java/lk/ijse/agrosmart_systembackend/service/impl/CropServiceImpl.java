package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.CropDTO;
import lk.ijse.agrosmart_systembackend.entity.Crop;
import lk.ijse.agrosmart_systembackend.repository.CropRepository;
import lk.ijse.agrosmart_systembackend.service.CropService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CropServiceImpl implements CropService {

    private final CropRepository cropRepository;
    private final ModelMapper modelMapper;

    @Override
    public String saveCrop(CropDTO cropDTO) {

        String newId = generateCropID();

        if (cropRepository.existsById(newId)) {
            throw new RuntimeException("Generated Crop ID already exists: " + newId);
        }

        if (cropRepository.existsByCommonName(cropDTO.getCommonName())) {
            throw new RuntimeException("Crop common name already exists");
        }

        if (cropRepository.existsByScientificName(cropDTO.getScientificName())) {
            throw new RuntimeException("Scientific name already exists");
        }

        Crop crop = Crop.builder()
                .cropId(newId)
                .commonName(cropDTO.getCommonName())
                .scientificName(cropDTO.getScientificName())
                .category(cropDTO.getCategory())
                .season(cropDTO.getSeason())
                .cropImg(cropDTO.getCropImg())
                .build();

        cropRepository.save(crop);
        return "Crop saved successfully";
    }

    @Override
    public String updateCrop(CropDTO cropDTO) {

        if (!cropRepository.existsById(cropDTO.getCropId())) {
            throw new RuntimeException("Crop not found");
        }

        Crop existing = cropRepository.findById(cropDTO.getCropId()).get();

        if (!existing.getCommonName().equals(cropDTO.getCommonName()) &&
                cropRepository.existsByCommonName(cropDTO.getCommonName())) {
            throw new RuntimeException("Crop common name already exists");
        }

        if (!existing.getScientificName().equals(cropDTO.getScientificName()) &&
                cropRepository.existsByScientificName(cropDTO.getScientificName())) {
            throw new RuntimeException("Scientific name already exists");
        }

        Crop crop = Crop.builder()
                .cropId(cropDTO.getCropId())
                .commonName(cropDTO.getCommonName())
                .scientificName(cropDTO.getScientificName())
                .category(cropDTO.getCategory())
                .season(cropDTO.getSeason())
                .cropImg(cropDTO.getCropImg())
                .build();

        cropRepository.save(crop);
        return "Crop updated successfully";
    }

    @Override
    public String deleteCrop(String id) {

        if (!cropRepository.existsById(id)) {
            throw new RuntimeException("Crop not found");
        }

        cropRepository.deleteById(id);
        return "Crop deleted successfully";
    }

    @Override
    public CropDTO getCrop(String id) {

        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crop not found"));

        return modelMapper.map(crop, CropDTO.class);
    }

    @Override
    public List<CropDTO> getAllCrops() {

        return modelMapper.map(
                cropRepository.findAll(),
                new TypeToken<List<CropDTO>>() {}.getType()
        );
    }

    private String generateCropID() {

        List<Crop> cropList = cropRepository.findAll();

        if (cropList.isEmpty()) {
            return "C001";
        }

        String lastId = cropList.get(cropList.size() - 1).getCropId();
        int newId = Integer.parseInt(lastId.substring(1)) + 1;

        return String.format("C%03d", newId);
    }
}
