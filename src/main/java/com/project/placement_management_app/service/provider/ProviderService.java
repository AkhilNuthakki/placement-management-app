package com.project.placement_management_app.service.provider;

import com.project.placement_management_app.dto.request.ProviderFormRequestDto;
import com.project.placement_management_app.dto.request.RegisterProviderRequestDto;
import com.project.placement_management_app.model.ProviderForm;
import com.project.placement_management_app.model.User;

public interface ProviderService {

    ProviderForm saveProviderForm(ProviderFormRequestDto providerFormRequestDto);
    ProviderForm submitProviderForm(ProviderFormRequestDto providerFormRequestDto);
    ProviderForm getProviderFromById(String providerFormId);
    User registerAndNotifyProvider(RegisterProviderRequestDto registerProviderRequestDto);
}
