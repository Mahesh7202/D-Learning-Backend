package com.elearning.resourcesservice.dto;


import com.elearning.resourcesservice.model.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourcesDto {
    private String sucode;
    private List<Resource> resource;
}
