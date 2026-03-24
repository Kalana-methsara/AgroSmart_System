
package lk.ijse.agrosmart_systembackend.controller;

import lk.ijse.agrosmart_systembackend.dto.InventoryDTO;
import lk.ijse.agrosmart_systembackend.service.InventoryService;
import lk.ijse.agrosmart_systembackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // CREATE
    @PostMapping
    public ResponseEntity<ApiResponse> saveInventory(@RequestBody InventoryDTO inventoryDTO){
        return new ResponseEntity<>(
                new ApiResponse(201,"Inventory Saved Successfully", inventoryService.saveInventory(inventoryDTO)),
                HttpStatus.CREATED
        );
    }

    // GET ALL
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllInventories() {
        return new ResponseEntity<>(
                new ApiResponse(200, "Success", inventoryService.getAllInventories()),
                HttpStatus.OK
        );
    }

    // UPDATE
    @PutMapping
    public ResponseEntity<ApiResponse> updateInventory(@RequestBody InventoryDTO inventoryDTO) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Inventory Updated Successfully", inventoryService.updateInventory(inventoryDTO)),
                HttpStatus.OK
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteInventory(@PathVariable String id) {
        return new ResponseEntity<>(
                new ApiResponse(200, "Inventory Deleted Successfully", inventoryService.deleteInventory(id)),
                HttpStatus.OK
        );
    }
}
