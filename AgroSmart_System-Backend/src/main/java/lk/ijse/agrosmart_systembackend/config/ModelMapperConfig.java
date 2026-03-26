//package lk.ijse.agrosmart_systembackend.config;
//
//import lk.ijse.agrosmart_systembackend.dto.FieldCropDTO;
//import lk.ijse.agrosmart_systembackend.dto.FieldStaffDTO;
//import lk.ijse.agrosmart_systembackend.entity.FieldCrop;
//import lk.ijse.agrosmart_systembackend.entity.FieldStaff;
//import org.modelmapper.ModelMapper;
//import org.modelmapper.PropertyMap;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ModelMapperConfig {
//
//    @Bean
//    public ModelMapper modelMapper() {
//        ModelMapper modelMapper = new ModelMapper();
//
//        // Configure FieldStaff to FieldStaffDTO mapping
//        modelMapper.addMappings(new PropertyMap<FieldStaff, FieldStaffDTO>() {
//            @Override
//            protected void configure() {
//                map().setFieldId(source.getField().getFieldId());
//                map().setStaffId(source.getStaff().getStaffId());
//                map().setAssignedDate(source.getAssignedDate());
//                map().setFieldStaffId(source.getFieldStaffId());
//            }
//        });
//
//        // Configure FieldStaffDTO to FieldStaff mapping
//        modelMapper.addMappings(new PropertyMap<FieldStaffDTO, FieldStaff>() {
//            @Override
//            protected void configure() {
//                skip(destination.getField());
//                skip(destination.getStaff());
//                map().setFieldStaffId(source.getFieldStaffId());
//                map().setAssignedDate(source.getAssignedDate());
//            }
//        });
//
//        // Configure FieldCrop to FieldCropDTO mapping
//        modelMapper.addMappings(new PropertyMap<FieldCrop, FieldCropDTO>() {
//            @Override
//            protected void configure() {
//                map().setFieldId(source.getField().getFieldId());
//                map().setCropId(source.getCrop().getCropId());
//                map().setAssignedDate(source.getAssignedDate());
//                map().setFieldCropId(source.getFieldCropId());
//            }
//        });
//
//        // Configure FieldCropDTO to FieldCrop mapping
//        modelMapper.addMappings(new PropertyMap<FieldCropDTO, FieldCrop>() {
//            @Override
//            protected void configure() {
//                skip(destination.getField());
//                skip(destination.getCrop());
//                map().setFieldCropId(source.getFieldCropId());
//                map().setAssignedDate(source.getAssignedDate());
//            }
//        });
//
//        return modelMapper;
//    }
//}