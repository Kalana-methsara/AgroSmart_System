package lk.ijse.agrosmart_systembackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "crop_task_template")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CropTaskTemplate implements Serializable {
    @Id
    private String templateId;

    private String taskName;

    private Integer startDay;

    private Integer repeatInterval;

    // මේ field එකත් දාගන්න. එතකොට අක්කරයකට යන පැය ගණන වගේ දේවල් calculate කරන්න ලේසියි.
    private Double standardDuration;

    private String cropId;
}
/*
-- C002: Cinnamon
INSERT INTO crop_task_template (template_id, task_name, start_day, repeat_interval, standard_duration, crop_id) VALUES
('T-C002-01', 'Land Preparation', 0, 0, 10.0, 'C002'),
        ('T-C002-02', 'Fertilizer Application', 21, 60, 4.0, 'C002'),
        ('T-C002-03', 'Pruning', 180, 180, 12.0, 'C002'),
        ('T-C002-04', 'Harvesting (Peeling)', 365, 365, 20.0, 'C002');

        -- C003: Paddy
INSERT INTO crop_task_template (template_id, task_name, start_day, repeat_interval, standard_duration, crop_id) VALUES
('T-C003-01', 'Ploughing', 0, 0, 8.0, 'C003'),
        ('T-C003-02', 'Sowing', 1, 0, 6.0, 'C003'),
        ('T-C003-03', 'First Fertilizer', 10, 20, 3.0, 'C003'),
        ('T-C003-04', 'Weed Control', 30, 0, 5.0, 'C003'),
        ('T-C003-05', 'Harvesting', 115, 0, 15.0, 'C003');

        -- C004: Tea
INSERT INTO crop_task_template (template_id, task_name, start_day, repeat_interval, standard_duration, crop_id) VALUES
('T-C004-01', 'Hole Preparation', 0, 0, 12.0, 'C004'),
        ('T-C004-02', 'Planting', 7, 0, 10.0, 'C004'),
        ('T-C004-03', 'Fertilizer Application', 30, 45, 5.0, 'C004'),
        ('T-C004-04', 'Plucking', 200, 7, 8.0, 'C004');

        -- C005: Coconut
INSERT INTO crop_task_template (template_id, task_name, start_day, repeat_interval, standard_duration, crop_id) VALUES
('T-C005-01', 'Pit Preparation', 0, 0, 15.0, 'C005'),
        ('T-C005-02', 'Fertilizer Application', 90, 180, 6.0, 'C005'),
        ('T-C005-03', 'Circle Cleaning', 30, 90, 4.0, 'C005'),
        ('T-C005-04', 'Harvesting', 1000, 45, 10.0, 'C005');

        -- C006: Rubber
INSERT INTO crop_task_template (template_id, task_name, start_day, repeat_interval, standard_duration, crop_id) VALUES
('T-C006-01', 'Lining and Pitting', 0, 0, 12.0, 'C006'),
        ('T-C006-02', 'Fertilizer Application', 60, 120, 5.0, 'C006'),
        ('T-C006-03', 'Disease Inspection', 30, 30, 2.0, 'C006');

        -- C007: Coffee
INSERT INTO crop_task_template (template_id, task_name, start_day, repeat_interval, standard_duration, crop_id) VALUES
('T-C007-01', 'Shade Tree Planting', 0, 0, 5.0, 'C007'),
        ('T-C007-02', 'Fertilizer Application', 45, 90, 4.0, 'C007'),
        ('T-C007-03', 'Pruning', 150, 180, 8.0, 'C007');

        -- C008: Pepper
INSERT INTO crop_task_template (template_id, task_name, start_day, repeat_interval, standard_duration, crop_id) VALUES
('T-C008-01', 'Support Tree Planting', 0, 0, 6.0, 'C008'),
        ('T-C008-02', 'Fertilizer Application', 14, 30, 3.0, 'C008'),
        ('T-C008-03', 'Tying Vines', 21, 14, 2.0, 'C008');

        -- C009: Turmeric
INSERT INTO crop_task_template (template_id, task_name, start_day, repeat_interval, standard_duration, crop_id) VALUES
('T-C009-01', 'Bed Preparation', 0, 0, 10.0, 'C009'),
        ('T-C009-02', 'Fertilizer Application', 30, 30, 4.0, 'C009'),
        ('T-C009-03', 'Mulching', 15, 0, 5.0, 'C009'),
        ('T-C009-04', 'Harvesting', 260, 0, 15.0, 'C009');

        -- C010: Banana
INSERT INTO crop_task_template (template_id, task_name, start_day, repeat_interval, standard_duration, crop_id) VALUES
('T-C010-01', 'Pit Digging', 0, 0, 8.0, 'C010'),
        ('T-C010-02', 'Fertilizer Application', 15, 45, 5.0, 'C010'),
        ('T-C010-03', 'De-suckering', 60, 30, 3.0, 'C010'),
        ('T-C010-04', 'Harvesting', 360, 0, 10.0, 'C010');

 */