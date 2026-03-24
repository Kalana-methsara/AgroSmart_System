package lk.ijse.agrosmart_systembackend.service;

import lk.ijse.agrosmart_systembackend.dto.InventoryDTO;

import java.util.List;

public interface InventoryService {
    String saveInventory(InventoryDTO inventoryDTO);
    String updateInventory(InventoryDTO inventoryDTO);
    String deleteInventory(String inventoryId);
    List<InventoryDTO> getAllInventories();
}
