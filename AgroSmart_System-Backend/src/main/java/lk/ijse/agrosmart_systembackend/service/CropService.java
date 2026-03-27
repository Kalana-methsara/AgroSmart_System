package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.dto.CropDTO;

import java.util.List;

public interface CropService {

    String saveCrop(CropDTO cropDTO);
    String updateCrop(CropDTO cropDTO);
    String deleteCrop(String id);
    CropDTO getCrop(String id);
    List<CropDTO> getAllCrops();

    // --- Smart Features / Decision Support Methods ---

    /**
     * අස්වැන්න පිළිබඳ අනාවැකි ලබා ගැනීම (Harvest Prediction)
     * වගා කරන බිම් ප්‍රමාණය අනුව ලැබිය හැකි දළ අස්වැන්න ගණනය කරයි.
     */
    double predictYield(String cropId, double landSize);

    /**
     * ඉදිරි වැඩපිළිවෙළ (Task Schedule) ගණනය කිරීම
     * සිටුවූ දිනය අනුව ඊළඟට පොහොර යෙදිය යුතු දිනයන් ලබා දෙයි.
     */
    List<String> getUpcomingTasks(String cropId, String startDate);

    /**
     * කාලගුණය මත පදනම් වූ උපදෙස් (Weather-based advice)
     * වත්මන් කාලගුණය පරීක්ෂා කර අදාළ භෝගයට ගැලපෙන Alert එකක් සාදයි.
     */
    String generateWeatherAlert(String cropId, String location);
}