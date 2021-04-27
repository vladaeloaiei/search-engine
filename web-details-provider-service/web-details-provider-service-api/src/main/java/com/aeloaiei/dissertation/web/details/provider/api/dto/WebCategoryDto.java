package com.aeloaiei.dissertation.web.details.provider.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebCategoryDto {
    private String name;
    private Set<String> urls;
}
