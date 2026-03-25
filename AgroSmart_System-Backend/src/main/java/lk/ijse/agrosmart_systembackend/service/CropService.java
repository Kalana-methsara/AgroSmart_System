package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.dto.CropDTO;

import java.util.List;

public interface CropService {

    String saveCrop(CropDTO cropDTO);
    String updateCrop(CropDTO cropDTO);
    String deleteCrop(String id);
    CropDTO getCrop(String id);
    List<CropDTO> getAllCrops();
}