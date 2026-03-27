
package lk.ijse.agrosmart_systembackend.service.impl;

import lk.ijse.agrosmart_systembackend.dto.InventoryDTO;
import lk.ijse.agrosmart_systembackend.entity.Inventory;
import lk.ijse.agrosmart_systembackend.repository.InventoryRepository;
import lk.ijse.agrosmart_systembackend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public String saveInventory(InventoryDTO inventoryDTO) {
        // 1. නම සහ Category එක ගැලපෙන එකක් තිබේදැයි බලන්න
        Optional<Inventory> existingInventory = inventoryRepository
                .findByItemNameAndCategory(inventoryDTO.getItemName(), inventoryDTO.getCategory());

        if (existingInventory.isPresent()) {
            // 2. තිබේ නම් - පවතින ප්‍රමාණයට අලුත් ප්‍රමාණය එකතු කර Update කරන්න
            Inventory inventory = existingInventory.get();
            inventory.setQuantity(inventory.getQuantity() + inventoryDTO.getQuantity());
            inventory.setLastUpdated(LocalDateTime.now().now()); // අද දිනයට Update කිරීම

            inventoryRepository.save(inventory);
            return "Existing inventory updated with new quantity";
        } else {
            // 3. නොමැති නම් - අලුත් ID එකක් සාදා අලුතින් Save කරන්න
            String newId = generateInventoryID();
            Inventory inventory = Inventory.builder()
                    .inventoryId(newId)
                    .itemName(inventoryDTO.getItemName())
                    .category(inventoryDTO.getCategory())
                    .quantity(inventoryDTO.getQuantity())
                    .unit(inventoryDTO.getUnit())
                    .lastUpdated(LocalDateTime.now()) // පළමු වරට අද දිනය යෙදීම
                    .build();

            inventoryRepository.save(inventory);
            return "New inventory saved successfully";
        }
    }

    @Override
    public String updateInventory(InventoryDTO inventoryDTO) {
        if (!inventoryRepository.existsById(inventoryDTO.getInventoryId())) {
            throw new RuntimeException("Inventory with ID " + inventoryDTO.getInventoryId() + " does not exist");
        }
        Inventory inventory = Inventory.builder()
                .inventoryId(inventoryDTO.getInventoryId())
                .itemName(inventoryDTO.getItemName())
                .category(inventoryDTO.getCategory())
                .quantity(inventoryDTO.getQuantity())
                .unit(inventoryDTO.getUnit())
                .lastUpdated(inventoryDTO.getLastUpdated())
                .build();
        inventoryRepository.save(inventory);
        return "Inventory updated successfully";
    }

    @Override
    public String deleteInventory(String inventoryId) {
        if (!inventoryRepository.existsById(inventoryId)) {
            throw new RuntimeException("Inventory with ID " + inventoryId + " does not exist");
        }
        inventoryRepository.deleteById(inventoryId);
        return "Inventory deleted successfully";
    }

    @Override
    public List<InventoryDTO> getAllInventories() {
        return modelMapper.map(inventoryRepository.findAll(), new TypeToken<List<InventoryDTO>>() {}.getType());
    }

    private String generateInventoryID() {
        List<Inventory> inventories = inventoryRepository.findAll();
        if (inventories.isEmpty()) {
            return "I001";
        }
        String lastId = inventories.get(inventories.size() - 1).getInventoryId();
        int newId = Integer.parseInt(lastId.substring(1)) + 1;
        return String.format("I%03d", newId);
    }
}
